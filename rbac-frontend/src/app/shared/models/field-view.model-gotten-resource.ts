import { FieldView } from './field-view.model';

/**
 * 個人化表格欄位可視化設定
 */
export interface FieldViewCustomisationGottenResource {
  code: string;
  message: string;
  data: FieldView[];
}
