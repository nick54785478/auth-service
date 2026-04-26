import { UserDetailQueried } from './user-detail-query.model';
import { UserQueried } from './user-query.model';

export class UserDetailGottenResource {
  code!: string; // 資料種類
  message!: string; // 種類
  data!: UserDetailQueried;
}
