import { RoleFunctionQueried } from './role-function-query.model';

export class RoleFunctionGottenResource {
  code!: string;
  message!: string;
  data: RoleFunctionQueried[] = [];
}
