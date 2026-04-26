import { UserGroupQueried } from './user-group-query.model';

export class UserGroupGottenResource {
  code!: string; // 資料種類
  message!: string;
  data: UserGroupQueried[] = [];
}
