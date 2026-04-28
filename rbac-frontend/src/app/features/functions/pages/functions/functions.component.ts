import {
  Component,
  DoCheck,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { FunctionsService } from '../../services/functions.service';
import {
  FormControl,
  FormGroup,
  FormsModule,
  Validators,
} from '@angular/forms';
import { Option } from '../../../../shared/models/option.model';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../../../shared/shared.module';
import { BaseInlineEditeTableCompoent } from '../../../../shared/component/base/base-inline-edit-table.component';
import { SystemMessageService } from '../../../../core/services/system-message.service';
import { OptionService } from '../../../../shared/services/option.service';
import { SettingType } from '../../../../core/enums/setting-type.enum';
import { CoreModule } from '../../../../core/core.module';
import { finalize } from 'rxjs/internal/operators/finalize';
import { LoadingMaskService } from '../../../../core/services/loading-mask.service';
import { SaveFunction } from '../../models/save-functions-request.model';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { OverlayPanel } from 'primeng/overlaypanel';
import { CustomisationService } from '../../../../shared/services/customisation.service';
import { SystemStorageKey } from '../../../../core/enums/system-storage.enum';
import { StorageService } from '../../../../core/services/storage.service';
import { UpdateCustomisation } from '../../../../shared/models/update-customisation-request.model';
import { catchError } from 'rxjs/internal/operators/catchError';
import { of } from 'rxjs/internal/observable/of';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { tap } from 'rxjs/internal/operators/tap';

@Component({
  selector: 'app-functions',
  standalone: true,
  imports: [CommonModule, SharedModule, CoreModule],
  providers: [SystemMessageService, OptionService],
  templateUrl: './functions.component.html',
  styleUrl: './functions.component.scss',
})
export class FunctionsComponent
  extends BaseInlineEditeTableCompoent
  implements OnInit, OnDestroy
{
  static readonly COMPONENT_NAME = 'FunctionsComponent'; // Component 名稱
  activeFlags: Option[] = []; // Active Flag 的下拉式選單
  types: Option[] = []; // 配置種類的下拉式選單
  actionTypes: Option[] = []; // ActionTypes 的下拉式選單
  services: Option[] = [];

  // 控制 OverlayPanel
  @ViewChild('fieldPanel') fieldPanel!: OverlayPanel;

  // Field 顯示設定清單
  fields: any[] = [];
  selectedFields: any[] = []; // 被選中的欄位
  fieldViews: any[] = [];
  filteredCols: any[] = [];

  constructor(
    private storageService: StorageService,
    private customisationService: CustomisationService,
    private loadingMaskService: LoadingMaskService,
    private optionService: OptionService,
    private functionService: FunctionsService,
    private messageService: SystemMessageService,
  ) {
    super();
  }
  ngOnInit(): void {
    // 初始化表單
    this.formGroup = new FormGroup({
      service: new FormControl('', Validators.required), // 種類
      actionType: new FormControl(''), // 動作種類
      type: new FormControl({ value: '', disabled: true }), // 種類
      name: new FormControl(''), // 功能名稱
      activeFlag: new FormControl(''), // 是否生效
    });

    // 1. 初始化表格上方 Tab 按鈕
    this.initTabs();

    // 監聽 service 變更，變更後要更新 Type 的下拉選單資料
    this.formGroup
      .get('service')
      ?.valueChanges.pipe(
        // 【步驟 1：同步清理與狀態更新】
        tap((serviceValue) => {
          // 刷新上方 Tab 按鈕狀態
          this.refreshTabs(serviceValue);

          const typeControl = this.formGroup.get('type');

          // 徹底清空下方的查詢結果，避免切換 Service 時幽靈資料殘留
          this.tableData = [];
          this.selectedData = [];
          if (this.dataTable) {
            // 注意：你的基礎類別似乎是用 dataTable 或 dt，請依實際變數名稱調整
            this.dataTable.reset();
          }

          if (serviceValue) {
            typeControl?.enable();
            typeControl?.setValue(null); // 清空下拉選單目前的值
            typeControl?.markAsUntouched();
            typeControl?.markAsPristine();
          } else {
            typeControl?.setValue(null);
            typeControl?.disable();
            this.types = []; // Service 為空時，一併清空選項陣列
          }
        }),

        // 【步驟 2：非同步取得下拉選單資料】
        switchMap((serviceValue) => {
          // 如果沒有選擇 Service，直接回傳空陣列中斷流程
          if (!serviceValue) {
            return of([]);
          }

          return this.optionService
            .getFunctionDropdownOptions(serviceValue)
            .pipe(
              // 攔截 API 錯誤，避免 RxJS 管線崩潰
              catchError((error) => {
                this.messageService.error('取得資料發生錯誤', error.message);
                return of([]); // 報錯時回傳空陣列，維持流程運作
              }),
            );
        }),
      )
      .subscribe({
        // 【步驟 3：接收並綁定乾淨的資料】
        next: (res) => {
          this.types = res;
        },
      });

    // 初始化 Table 配置
    this.cols = [
      {
        field: 'service',
        header: '服務',
        type: 'dropdown',
        width: '8rem',
        required: true,
        readOnly: true,
      },
      {
        field: 'type',
        header: '種類',
        type: 'dropdown',
        width: '10rem',
        required: true,
        readOnly: false,
      },
      {
        field: 'actionType',
        header: '動作',
        type: 'dropdown',
        width: '5rem',
        required: true,
        readOnly: false,
      },
      {
        field: 'code',
        header: '代碼',
        type: 'inputText',
        width: '10rem',
        required: true,
        readOnly: false,
      },
      {
        field: 'name',
        header: '名稱',
        type: 'inputText',
        width: '8rem',
        required: true,
        readOnly: false,
      },
      {
        field: 'description',
        header: '說明',
        type: 'textArea',
        width: '15rem',
        required: true,
        readOnly: false,
      },
      {
        field: 'activeFlag',
        header: '生效',
        type: 'dropdown',
        width: '4rem',
        required: true,
        readOnly: false,
      },
    ];

    // 將 cols 映射成 fields
    this.fields = this.cols.map((col) => ({
      field: col.field,
      label: col.header,
    }));

    // 初始化下拉選單資料
    forkJoin({
      activeFlags: this.optionService.getSettingTypes(
        'AUTH_SERVICE',
        SettingType.YES_NO,
      ),
      actionTypes: this.optionService.getSettingTypes(
        'AUTH_SERVICE',
        SettingType.ACTION_TYPE,
      ),
      services: this.optionService.getSettingTypes(
        'AUTH_SERVICE',
        SettingType.SERVICE,
      ),
    }).subscribe({
      next: (res) => {
        this.activeFlags = res.activeFlags;
        this.actionTypes = res.actionTypes;
        this.services = res.services;
      },
      error: (error) => {
        this.messageService.error(error);
      },
    });

    // 取出 Field View 個人化設定
    this.getFieldViewCustomisation();
  }

  ngOnDestroy() {}

  /**
   * 表格上方按鈕初始化
   * */
  initTabs() {
    this.detailTabs = [
      {
        label: '欄位',
        icon: 'pi pi-filter',
        command: () => {
          this.fieldPanel.toggle(event);
        },
        disabled: false,
      },
      {
        label: '新增',
        icon: 'pi pi-plus',
        command: () => {
          this.addNewRow();
        },
        disabled: !this.formGroup?.value?.service,
      },
      {
        label: '提交',
        icon: 'pi pi-save',
        command: () => {
          this.submit();
        },
        disabled: false,
      },
      {
        label: '放棄',
        icon: 'pi pi-times',
        command: () => {
          this.cancelAll();
        },
        disabled: false,
      },
    ];
  }

  /**
   * 刷新 Tab 狀態
   * */
  refreshTabs(serviceValue: any) {
    // 找到「新增」按鈕 (索引為 1)
    if (this.detailTabs && this.detailTabs[1]) {
      this.detailTabs[1].disabled = !serviceValue;

      // 重要：PrimeNG 的 MenuItem 通常需要重新賦值（改變引用）才會觸發畫面更新
      this.detailTabs = [...this.detailTabs];
    }
  }

  /**
   * 清除表單資料
   */
  override clear() {
    this.tableData = [];
    this.minGivenIndex = -1;
    this.formGroup.reset();
  }

  // 提交資料
  override submit() {
    console.log(this.tableData);
    this.submitted = true;
    const requestData: SaveFunction[] = this.tableData.map((data) => {
      return {
        id: data.id,
        service: data.service,
        actionType: data.actionType,
        code: data.code,
        type: data.type,
        name: data.name,
        description: data.description,
        activeFlag: data.activeFlag,
      };
    });

    if (!this.submitted || this.mode) {
      return;
    }
    this.loadingMaskService.show();
    this.functionService
      .submit(requestData)
      .pipe(
        finalize(() => {
          this.submitted = false;
          this.loadingMaskService.hide();
          // 無論成功或失敗都會執行
          this.query();
        }),
      )
      .subscribe({
        next: (res) => {
          if (res?.code === 'VALIDATION_FAILED') {
            this.messageService.error(res.message);
          } else {
            this.messageService.success(res.message);
          }
        },
        error: (error) => {
          this.messageService.error(error.message);
        },
      });
  }

  /**
   * 透過特定條件查詢設定資料
   */
  query() {
    this.submitted = true;
    if (this.formGroup.invalid) {
      return;
    }

    this.loadingMaskService.show();
    // 初始化前端表格的 index 值
    this.initTableIndex();
    // 查詢前先取消所有
    this.cancelAll();
    let formData = this.formGroup.value;
    this.functionService
      .query(
        formData.service,
        formData.type,
        formData.name,
        formData.activeFlag,
      )
      .pipe(
        finalize(() => {
          this.submitted = false;
          // 無論成功或失敗都會執行
          this.loadingMaskService.hide();
        }),
      )
      .subscribe({
        next: (res) => {
          this.messageService.success('查詢成功');
          this.tableData = res;
          // 對所有資料進行編號
          for (var i = 0; i < this.tableData.length; i++) {
            this.tableData[i].givenIndex = i;
          }

          console.log(this.tableData);
        },
        error: (error) => {
          this.messageService.error(error.message);
        },
      });
  }

  /**
   * 切換 編輯模式
   * @param givenIndex
   * @returns
   */
  onEdit(rowData: any) {
    this.mode = 'edit';
    console.log(rowData);
    this.clonedData[rowData.givenIndex] = { ...rowData };
  }

  /**
   * 判斷是否為編輯模式
   * @param givenIndex
   * */
  isEditing(givenIndex: any): boolean {
    return this.editingIndex === givenIndex;
  }

  /**
   * 取消編輯/新增
   * */
  cancel(rowData: any, rowIndex: number) {
    console.log(rowIndex);
    this.tableData[rowIndex] = this.clonedData[rowData.givenIndex];
    delete this.clonedData[rowData.givenIndex];
    this.mode = '';
    console.log(this.mode);
  }

  /**
   * 回歸原狀，原先新增的資料全部放棄。
   */
  cancelAll() {
    // 基本上 givenIndex < 0 者都是新增的資料
    this.dataTable.editingRowKeys = {};
    this.tableData = this.tableData.filter((data) => data.givenIdex >= 0);
  }

  /**
   * Table Action 按鈕按下去的時候要把該筆資料記錄下來。
   * @param rowData 點選的資料
   */
  clickRowActionMenu(rowData: any): void {
    this.selectedData = rowData;
  }

  /**
   * 新增一筆空的 row 資料
   * */
  addNewRow(): void {
    // 未進行查詢，不予新增 ( tableData 為空且 service 尚未填寫資料)
    if (this.tableData.length === 0 && !this.formGroup?.value?.service) {
      console.log('尚未查詢資料');
      return;
    }
    // 計算目前「尚未提交到資料庫」的新資料數量 (通常 id 為空的)
    const newItemsCount = this.tableData.filter((d) => !d.id).length;

    if (newItemsCount >= 5) {
      this.messageService.warn(
        '一次最多只能新增 5 筆未儲存資料，請先提交後再繼續。',
      );
      return;
    }
    this.mode = 'add';

    this.newRow = {
      id: null,
      service: this.formGroup.get('service')?.value
        ? this.formGroup.get('service')?.value
        : '',
      actionType: '',
      name: '',
      type: this.formGroup.get('type')?.value
        ? this.formGroup.get('type')?.value
        : '',
      description: '',
      givenIndex: this.minGivenIndex--, // 前端給予的編號資料
    };
    console.log(this.minGivenIndex);
    this.tableData.unshift(this.newRow);
    // this.onEdit(this.newRow);
    // 觸發該 row 的編輯模式
    setTimeout(() => {
      this.dataTable.initRowEdit(this.newRow);
    });
  }

  /**
   *  刪除幾列資料
   * */
  override delete(ids: number[], event?: Event) {
    this.functionService
      .delete(ids)
      .pipe(
        finalize(() => {
          // 無論成功或失敗都會執行
          this.query();
        }),
      )
      .subscribe({
        next: (res) => {
          if (res?.code === 'VALIDATION_FAILED') {
            this.messageService.error(res.message);
          } else {
            this.messageService.success(res.message);
          }
        },
        error: (error) => {
          this.messageService.error(error.message);
        },
      });
  }

  /**
   * 檢查 row 資料是否有未填欄位
   * */
  override checkRowData(selectedData: any): void {
    if (
      !selectedData.service ||
      !selectedData.type ||
      !selectedData.actionType ||
      !selectedData.name ||
      !selectedData.code ||
      !selectedData.description ||
      !selectedData.activeFlag
    ) {
      this.dataTable.initRowEdit(selectedData);
    } else {
      this.mode = '';
    }
    console.log(this.mode);
  }

  /**
   * 判斷Type欄位是否可修改
   * @param rowData 該 row 的資料
   * @param field 欄位名稱
   * @returns
   */
  isFieldDisabled(rowData: any, field: string): boolean {
    if (
      (field === 'type' &&
        rowData.id !== null &&
        this.formGroup.get('type')?.value !== '') ||
      (rowData.id !== null && this.formGroup.get('name')?.value !== '')
    ) {
      return true;
    }
    return false;
  }

  /**
   * 載入 dropdown 資料
   * @param col
   * @returns
   */
  override loadDropdownData(col: any): any[] {
    // 如果已經載入過資料，則不再重新請求
    switch (col.field) {
      case 'type':
        return this.types;
      case 'activeFlag':
        return this.activeFlags;
      case 'actionType':
        return this.actionTypes;
      case 'service':
        return this.services;
      default:
        return [];
    }
  }

  // 隱藏 Field 設定清單
  closePanel() {
    this.fieldPanel.hide();
  }

  /**
   * 提交 Fields 設定
   */
  submitFields() {
    console.log(this.selectedFields);
    const username =
      this.storageService.getSessionStorageItem(SystemStorageKey.USERNAME) ||
      this.storageService.getLocalStorageItem(SystemStorageKey.USERNAME) ||
      '';
    // 取得 Component 名稱
    let component = FunctionsComponent.COMPONENT_NAME;

    let requestData: UpdateCustomisation = {
      username: username,
      component: component,
      type: 'FieldView',
      valueList: this.selectedFields,
    };

    this.customisationService
      .updateCustomisation(requestData)
      .pipe(
        finalize(() => {
          // 無論成功或失敗都會執行
          this.getFieldViewCustomisation();
        }),
      )
      .subscribe({
        next: (res) => {
          if (res?.code === 'VALIDATION_FAILED') {
            this.messageService.error(res.message);
          } else {
            this.messageService.success(res.message);
          }
        },
        error: (error) => {
          this.messageService.error(error.message);
        },
      });
  }

  /**
   * 取得 Table Field View 配置
   */
  getFieldViewCustomisation() {
    const username =
      this.storageService.getSessionStorageItem(SystemStorageKey.USERNAME) ||
      this.storageService.getLocalStorageItem(SystemStorageKey.USERNAME) ||
      '';
    // 取得 Component 名稱
    let component = FunctionsComponent.COMPONENT_NAME;
    console.log(username, component);
    this.customisationService
      .getFieldViewCustomisations(username, component)
      .subscribe((res) => {
        this.fieldViews = res.map((data) => data.field);
        // 將查出的資料設置進 selectedFields
        this.selectedFields = res;
        // 只保留在 viewCols 中的欄位
        this.filteredCols = this.cols.filter((col) =>
          this.fieldViews.includes(col.field),
        );
      });
    this.closePanel();
  }
}
