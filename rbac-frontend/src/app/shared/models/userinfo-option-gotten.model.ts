import { UserInfoOption } from './userinfo-option.model';

export interface UserOptionGottenResource {
  code?: string;
  message?: string;
  data: UserInfoOption[];
}
