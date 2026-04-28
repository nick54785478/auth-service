import { Component, DoCheck, OnDestroy, OnInit } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { CoreModule } from '../../../../core/core.module';
import { Subject } from 'rxjs/internal/Subject';
import { CommonService } from '../../../../shared/services/common.service';
import { debounceTime, finalize, switchMap, takeUntil } from 'rxjs/operators';
import { LoadingMaskService } from '../../../../core/services/loading-mask.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { OptionService } from '../../../../shared/services/option.service';
import { RoleInfoOption } from '../../../../shared/models/role-info-option.model';
import { GroupRolesService } from '../../services/group-roles.service';
import { SystemMessageService } from '../../../../core/services/system-message.service';
import { BasePickListCompoent } from '../../../../shared/component/base/base-pickList.component';
import { GroupsService } from '../../services/groups.service';
import { UpdateGroupRoles } from '../../models/update-group-roles-request.model';
import { GroupQueried } from '../../models/group-query.model';
import { GroupInfoOption } from '../../../../shared/models/group-info-option.model';
import { Option } from '../../../../shared/models/option.model';
import { SettingType } from '../../../../core/enums/setting-type.enum';
import { group } from 'node:console';
import { of } from 'rxjs/internal/observable/of';

@Component({
  selector: 'app-group-roles',
  standalone: true,
  imports: [SharedModule, CoreModule],
  providers: [CommonService, SystemMessageService],
  templateUrl: './group-roles.component.html',
  styleUrl: './group-roles.component.scss',
})
export class GroupRolesComponent
  extends BasePickListCompoent
  implements OnInit, DoCheck, OnDestroy
{
  groupOptions: GroupInfoOption[] = [];
  services: Option[] = [];

  // AutoComplete 與其下拉欄位值變動的 Subject，用來避免前次查詢較慢返回覆蓋後次資料
  private dataSubject$ = new Subject<string>();

  selected!: GroupQueried; // 被查詢的角色資料

  queriedStr!: string;

  /**
   * 用來取消訂閱
   */
  readonly _destroying$ = new Subject<void>();

  constructor(
    private groupRolesService: GroupRolesService,
    private optionService: OptionService,
    private messageService: SystemMessageService,
    private loadMaskService: LoadingMaskService,
  ) {
    super();
  }
  ngDoCheck(): void {
    this.detailTabs = [
      {
        label: '提交',
        icon: 'pi pi-save',
        command: () => {
          this.onSubmit();
        },
        disabled: this.sourceList.length === 0 && this.targetList.length === 0,
      },
      {
        label: '取消',
        icon: 'pi pi-times',
        command: () => {
          this.cancel();
        },
        disabled: this.sourceList.length === 0 && this.targetList.length === 0,
      },
    ];
  }

  ngOnInit(): void {
    this.formGroup = new FormGroup({
      service: new FormControl('', [Validators.required]),
      group: new FormControl({ value: '', disabled: true }, [
        Validators.required,
      ]),
    });

    this.dataSubject$
      .pipe(
        takeUntil(this._destroying$),
        debounceTime(300), // 防抖
        switchMap((keyword) => {
          // 【關鍵修復】：每次查詢時，動態去抓「目前最新」的 service 值
          const currentService = this.formGroup.get('service')?.value;

          // 如果沒有選 service，直接回傳空陣列中斷查詢
          if (!currentService) {
            return of([]);
          }

          return this.optionService.getGroupOptions(currentService, keyword);
        }),
      )
      .subscribe((res) => {
        // 收到結果，更新下拉選單
        this.groupOptions = res.map((item: any) => ({
          id: item.id,
          code: item.code,
          name: item.name,
          displayName: `${item.code} (${item.name})`,
        }));
      });

    // 監聽 service 變更
    this.formGroup.get('service')?.valueChanges.subscribe((serviceValue) => {
      const groupControl = this.formGroup.get('group');

      // 清空防護網
      this.dataSubject$.next(''); // 發送空字串打斷舊 API
      this.groupOptions = []; // 清空選項
      this.queriedStr = ''; // 清空暫存字串
      this.sourceList = [];
      this.targetList = [];

      if (serviceValue) {
        groupControl?.enable();
        groupControl?.setValue(null); // 徹底清空 AutoComplete 綁定
        groupControl?.markAsUntouched();
        groupControl?.markAsPristine();

        // 直接透過 Subject 觸發空字串查詢，拉取新 Service 的預設群組
        this.dataSubject$.next('');
      } else {
        groupControl?.setValue(null);
        groupControl?.disable();
      }
    });

    this.detailTabs = [
      {
        label: '提交',
        icon: 'pi pi-save',
        command: () => {
          this.onSubmit();
        },
        disabled: this.sourceList.length === 0 && this.targetList.length === 0,
      },
      {
        label: '取消',
        icon: 'pi pi-times',
        command: () => {
          this.cancel();
        },
        disabled: this.sourceList.length === 0 && this.targetList.length === 0,
      },
    ];

    this.optionService
      .getSettingTypes('AUTH_SERVICE', SettingType.SERVICE)
      .subscribe({
        next: (res) => {
          this.services = res;
        },
        error: (error) => {
          this.messageService.error('取得資料發生錯誤', error.message);
        },
      });
  }

  ngOnDestroy(): void {
    this.dataSubject$.closed;
  }

  /**
   * 提交資料，變更角色相關資料
   */
  onSubmit() {
    let groupId = this.formGroup.value.group.id;
    console.log(this.targetList);
    let roleIds = this.targetList
      ? this.targetList
          .map((e) => e.id)
          .filter((id): id is number => id !== undefined) // 過濾 undefined 值
      : [];

    let requestData: UpdateGroupRoles = {
      groupId: groupId,
      roleIds: roleIds,
    };
    console.log(requestData);
    this.submitted = true;
    this.loadMaskService.show();
    this.groupRolesService
      .update(requestData)
      .pipe(
        finalize(() => {
          this.loadMaskService.hide();
          this.submitted = false;
          this.query();
        }),
      )
      .subscribe({
        next: (res) => {
          if (res.code !== 'VALIDATE_FAILED') {
            this.messageService.success(res.message);
          } else {
            this.messageService.error(res.message);
          }
        },
        error: (error) => {
          this.messageService.error(error);
        },
      });
  }

  /**
   * 清空所有
   */
  cancel() {
    this.formGroup.reset();
    this.sourceList = [];
    this.targetList = [];
  }

  /**
   * 提交資料，查詢群組相關角色資料 (顯示於 PickList 左側)
   */
  query() {
    let formData = this.formGroup.value;
    this.submitted = true;
    if (!this.formGroup.valid || !this.submitted) {
      return;
    }

    this.loadMaskService.show();
    this.groupRolesService
      .queryByIdAndService(formData.group.id, formData.service)
      .pipe(
        finalize(() => {
          this.loadMaskService.hide();
          this.submitted = false;
        }),
      )
      .subscribe((res) => {
        let roleList = res;
        if (roleList) {
          this.targetList = roleList.map((role: any) => ({
            id: role.id,
            service: role.service,
            code: role.code,
            name: role.name,
            displayName: `${role.code} (${role.name})`, // 生成 displayName
          }));
          console.log(this.targetList);
        }
      });
    // 查詢不屬於該 群組 (id) 的角色資料
    this.getOtherRoles(formData.group.id, formData.service);
  }

  /**
   * 查詢群組下拉式選單 (綁定在 AutoComplete 的 completeMethod)
   * @param event
   */
  getGroupOptions(event: any) {
    const query = event.query || '';

    // 防呆：如果輸入長度是 1，不發請求 (0 是為了點擊下拉箭頭時載入全部)
    if (query.length === 1) {
      return;
    }

    // 避免重複查詢一樣的字串
    if (query === this.queriedStr && query.length !== 0) {
      return;
    }

    this.queriedStr = query;

    // 把字串丟進管線，讓 ngOnInit 裡面的 switchMap 去接手處理
    this.dataSubject$.next(query);
  }

  /**
   * 取得不屬於該群組的角色
   * @param id
   */
  getOtherRoles(id: number, service: string) {
    this.groupRolesService
      .queryOthers(id, service)
      .pipe(
        finalize(() => {
          // 無論成功或失敗都會執行
        }),
      )
      .subscribe((res) => {
        console.log(res);
        if (res) {
          this.sourceList = res.map((item: any) => ({
            id: item.id, // 保留 id
            code: item.code, // 保留 name
            name: item.name, // 保留 nameEn
            displayName: `${item.code} (${item.name})`, // 生成 displayName
          }));
        }
      });
  }
}
