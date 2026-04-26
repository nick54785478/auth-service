import { GroupRoleQueried } from './group-role-query.model';

export class GroupRolesGottenResource {
  code!: string; // 資料種類
  mwssage!: string;
  data: GroupRoleQueried[] = [];
}
