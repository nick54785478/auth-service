import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { UpdateGroupRoles } from '../models/update-group-roles-request.model';
import { BaseResponse } from '../../../shared/models/base-response.model';
import { GroupRoleQueried } from '../models/group-role-query.model';
import { GroupRolesGottenResource } from '../models/group-roles-gotten-response.model';
import { map } from 'rxjs';
import { GroupQueried } from '../models/group-query.model';
import { GroupRoleQueriedResource } from '../models/group-role-queried-response.model';

@Injectable({
  providedIn: 'root',
})
export class GroupRolesService {
  private readonly baseApiUrl = environment.apiEndpoint;

  constructor(private http: HttpClient) {}

  /**
   * 透過 ID 與服務查詢不屬於該群組的角色資料
   * @param id
   * @param service
   * @returns
   */
  queryOthers(id: number, service: string): Observable<GroupRoleQueried[]> {
    const url = this.baseApiUrl + '/groups/roles/' + id + '/others';
    let params = new HttpParams().set('service', service ? service : '');
    return this.http.get<GroupRolesGottenResource>(url, { params }).pipe(
      map((res) => {
        return res?.data;
      }),
    );
  }

  /**
   * 提交更新或新增角色資料
   * @param requestData
   */
  update(requestData: UpdateGroupRoles): Observable<BaseResponse> {
    const url = this.baseApiUrl + '/groups/roles/update';
    return this.http.post<BaseResponse>(url, requestData);
  }

  /**
   * 透過群組 ID 查詢群組角色資料 (By Service)
   * @param id
   * @param service
   */
  queryByIdAndService(
    id: number,
    service: string,
  ): Observable<GroupRoleQueried[]> {
    const url = this.baseApiUrl + '/groups/roles' + '/' + id;
    let params = new HttpParams().set('service', service ? service : '');
    return this.http.get<GroupRoleQueriedResource>(url, { params }).pipe(
      map((res) => {
        return res?.data;
      }),
    );
  }
}
