import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from '../../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { UserDetailQueried } from '../models/user-detail-query.model';
import { map } from 'rxjs/internal/operators/map';
import { StorageService } from '../../../core/services/storage.service';
import { SystemStorageKey } from '../../../core/enums/system-storage.enum';
import { UpdateUserInfoResource } from '../models/update-user-request.model';
import { UserDetailGottenResource } from '../models/user-detail-queried-respeonse.model';

@Injectable({
  providedIn: 'root',
})
export class UsersService {
  private readonly baseApiUrl = environment.apiEndpoint;

  constructor(
    private http: HttpClient,
    private storageService: StorageService,
  ) {}

  /**
   * 查詢使用者詳細資訊
   * @param username
   * @returns
   */
  getUserDetail(
    username: string,
    service?: string,
  ): Observable<UserDetailQueried> {
    const url = this.baseApiUrl + '/users' + '/' + username + '/details';
    let params = new HttpParams().set('service', service ? service : '');
    return this.http.get<UserDetailGottenResource>(url, { params }).pipe(
      map((res) => {
        return res?.data;
      }),
    );
  }

  /**
   * 取得該使用者個人資料
   *  @param username
   */
  public getPersonality() {
    const username =
      this.storageService.getSessionStorageItem(SystemStorageKey.USERNAME) ||
      this.storageService.getLocalStorageItem(SystemStorageKey.USERNAME);
    const url = this.baseApiUrl + '/users/' + username + '/details';

    if (environment.apiMock) {
      return this.http.get<UserDetailQueried>('/user-data.json').pipe(
        map((response) => {
          return response;
        }),
      );
    }
    return this.http.get<UserDetailGottenResource>(url).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }

  /**
   * 更新使用者資料
   * @param id
   * @param request
   */
  public update(id: Number, request: UpdateUserInfoResource): Observable<any> {
    const url = this.baseApiUrl + '/users/' + id;
    return this.http.put(url, request);
  }
}
