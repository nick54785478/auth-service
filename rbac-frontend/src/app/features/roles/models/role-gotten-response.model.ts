import { RoleQueried } from './role-query.model';

export class RoleQueriedResource {
  code!: string;
  message!: string;
  data: RoleQueried[] = [];
}
