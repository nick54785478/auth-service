import { GroupInfoOption } from './group-info-option.model';

export interface GroupInfoOptionGottenResource {
  code?: string;
  message?: string;
  data: GroupInfoOption[];
}
