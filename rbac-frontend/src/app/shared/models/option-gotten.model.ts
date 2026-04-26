import { Option } from './option.model';
export interface OptionGottenResource {
  code?: string;
  message?: string;
  data: Option[];
}
