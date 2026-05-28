import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseFormCompoent } from '../../../../../shared/component/base/base-form.component';
import { CommonModule, Location } from '@angular/common';
import { SharedModule } from '../../../../../shared/shared.module';
import { CoreModule } from '../../../../../core/core.module';
import { DynamicDialogRef } from 'primeng/dynamicdialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Option } from '../../../../../shared/models/option.model';
import { OptionService } from '../../../../../shared/services/option.service';
import { SettingType } from '../../../../../core/enums/setting-type.enum';
import {
  catchError,
  finalize,
  of,
  Subject,
  switchMap,
  takeUntil,
  tap,
} from 'rxjs';
import { LoadingMaskService } from '../../../../../core/services/loading-mask.service';
import { SystemMessageService } from '../../../../../core/services/system-message.service';
import { FunctionsService } from '../../../services/functions.service';

@Component({
  selector: 'app-function-adding',
  standalone: true,
  imports: [SharedModule, CoreModule, CommonModule],
  templateUrl: './function-adding.component.html',
  styleUrl: './function-adding.component.scss',
})
export class FunctionAddingComponent
  extends BaseFormCompoent
  implements OnInit, OnDestroy
{
  services: Option[] = [];
  types: Option[] = [];
  actionTypes: Option[] = [];
  readonly _destroying$ = new Subject<void>(); // 用來取消訂閱

  constructor(
    private location: Location,
    public ref: DynamicDialogRef,
    private functionService: FunctionsService,
    private optionService: OptionService,
    private loadingMaskService: LoadingMaskService,
    private messageService: SystemMessageService,
  ) {
    super();
  }

  ngOnInit(): void {
    // 監聽上一頁切換，關閉 Dialog
    this.location.onUrlChange(() => {
      this.onCloseForm();
    });

    this.formGroup = new FormGroup({
      service: new FormControl('', [Validators.required]),
      type: new FormControl({ value: '', disabled: true }, [
        Validators.required,
      ]),
      actionType: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      code: new FormControl('', [Validators.required]),
      description: new FormControl(''),
    });

    // 取得 Service 下拉選單
    this.optionService
      .getSettingTypes('AUTH_SERVICE', SettingType.SERVICE)
      .subscribe((res) => {
        this.services = res;
      });

    this.optionService
      .getSettingTypes('AUTH_SERVICE', SettingType.ACTION_TYPE)
      .subscribe((res) => {
        this.actionTypes = res;
      });

    // 監聽 service 變更，連動更新 type 選單
    this.formGroup
      .get('service')
      ?.valueChanges.pipe(
        takeUntil(this._destroying$),

        // 【步驟 1：同步副作用】利用 tap 在打 API 前先清理畫面狀態
        tap((serviceValue) => {
          const typeControl = this.formGroup.get('type');

          if (serviceValue) {
            typeControl?.enable();
            // 微調 2：將 setValue 改為空字串 ''，與你定義的 FormControl 預設值保持一致，
            // PrimeNG dropdown 通常吃 null 或 '' 都可以，但統一用 '' 比較不會有型別警告
            typeControl?.setValue('');
            typeControl?.markAsUntouched();
            typeControl?.markAsPristine();
          } else {
            typeControl?.setValue('');
            typeControl?.disable();
            this.types = []; // Service 沒選，清空選項
          }
        }),

        // 【步驟 2：非同步 API】利用 switchMap 打斷舊請求，避免 Race Condition
        switchMap((serviceValue) => {
          if (!serviceValue) {
            return of([]);
          }

          return this.optionService
            .getSettingTypes(serviceValue, SettingType.FUNCTION)
            .pipe(
              // 關鍵防護：錯誤攔截寫在內部，維持管線存活
              catchError((error) => {
                console.error('取得角色種類發生錯誤:', error);
                return of([]);
              }),
            );
        }),
      )
      .subscribe({
        // 【步驟 3：接收最終乾淨的資料】
        next: (res) => {
          this.types = res;
        },
      });
  }

  ngOnDestroy(): void {}

  /**
   * 關閉 Dialog
   */
  onCloseForm() {
    console.log('關閉 Dialog');
    this.ref.close();
    this.clear();
  }

  /**
   * 清除表單資料
   */
  clear() {
    this.formGroup.reset();
  }

  /**
   * 資料提交
   */
  onSubmit(): void {
    console.log(this.formGroup.value);
    this.submitted = true;
    if (!this.submitted || this.formGroup.invalid) {
      return;
    }
    this.loadingMaskService.show();
    const requestData = this.formGroup.value;
    console.log(requestData);
    this.functionService
      .createFunction(requestData)
      .pipe(
        finalize(() => {
          this.submitted = false;
          this.loadingMaskService.hide();
        }),
      )
      .subscribe({
        next: (res) => {
          if (res?.code === 'VALIDATE_FAILED') {
            this.messageService.error(res.message);
          } else {
            this.messageService.success(res.message);
            this.onCloseForm();
          }
        },
        error: (error) => {
          this.messageService.error(error.message);
        },
      });
  }
}
