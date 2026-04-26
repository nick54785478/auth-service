import { FunctionQueried } from './function-query.model';

export class FunctionsSummaryQueriedResource {
  code!: string;
  message?: string;
  data: FunctionQueried[] = [];
}
