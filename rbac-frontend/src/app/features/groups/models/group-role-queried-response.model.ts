import { GroupRoleQueried } from './group-role-query.model';

export class GroupRoleQueriedResource {
  code!: string; // 資料種類
  message!: string;
  data: GroupRoleQueried[] = [];
}
