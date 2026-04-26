import { SettingQueried } from './setting-query.model';

export class SettingSummaryGottenResoruce {
  code?: string;
  message?: string;
  data: SettingQueried[] = [];
}
