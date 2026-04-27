import { Injectable } from '@angular/core';
import { RoleFunctionQueried } from '../models/role-function-query.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs/internal/Observable';
import { UpdateRoleFunction } from '../models/update-role-function-request.model';
import { BaseResponse } from '../../../shared/models/base-response.model';
import { RoleFunctionGottenResource } from '../models/role-function-queried-response.model';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RoleFunctionsService {
  private readonly baseApiUrl = environment.apiEndpoint;

  constructor(private http: HttpClient) {}

  /**
   * 透過 ID 查詢其他角色資料
   * @param id
   * @param service
   */
  getOtherRoleFunctions(
    id: number,
    service: string,
  ): Observable<RoleFunctionQueried[]> {
    const url = this.baseApiUrl + '/roles/functions/' + id + '/others';
    let params = new HttpParams().set('service', service ? service : '');
    return this.http.get<RoleFunctionGottenResource>(url, { params }).pipe(
      map((res) => {
        return res?.data;
      }),
    );
  }

  /**
   * 透過 ID 查詢角色資料
   * @param id
   * @param service
   */
  getRoleFunctionsByIdAndService(
    id: number,
    service: string,
  ): Observable<RoleFunctionQueried[]> {
    const url = this.baseApiUrl + '/roles/functions/' + id;
    let params = new HttpParams().set('service', service ? service : '');
    return this.http
      .get<RoleFunctionGottenResource>(url, { params })
      .pipe(map((res) => res.data));
  }

  /**
   * 提交更新或新增角色資料
   * @param requestData
   */
  update(requestData: UpdateRoleFunction): Observable<BaseResponse> {
    const url = this.baseApiUrl + '/roles/functions/update';
    return this.http.post<BaseResponse>(url, requestData);
  }
}
