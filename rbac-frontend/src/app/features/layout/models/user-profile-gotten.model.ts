import { UserProfileQueried } from './user-profile.model';

export class UserInfoGottenResource {
  code!: string;
  message!: string;
  data!: UserProfileQueried;
}
