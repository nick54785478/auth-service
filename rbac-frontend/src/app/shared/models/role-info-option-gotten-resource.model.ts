import { RoleInfoOption } from './role-info-option.model';

export interface RoleInfoOptionGottenResource {
  code?: string;
  message?: string;
  data: RoleInfoOption[];
}
