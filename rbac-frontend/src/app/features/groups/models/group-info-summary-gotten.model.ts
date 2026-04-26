import { RoleQueried } from '../../roles/models/role-query.model';
import { GroupInfoQueried } from './group-info-query.model';

export class GroupSummaryQueriedResource {
  code!: string;
  message!: string;
  data: GroupInfoQueried[] = [];
}
