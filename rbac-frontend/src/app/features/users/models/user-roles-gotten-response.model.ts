import { UserRoleQueried } from './user-roles-query.model';

export class UserRoleGottenResource {
  code!: string;
  message!: string;
  data: UserRoleQueried[] = [];
}
