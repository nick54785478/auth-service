import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Option } from '../models/option.model';
import { Observable } from 'rxjs/internal/Observable';
import { map } from 'rxjs/internal/operators/map';
import { environment } from '../../../environments/environment';
import { userInfo } from 'os';
import { UserInfoOption } from '../models/userinfo-option.model';
import { RoleInfoOption } from '../models/role-info-option.model';
import { GroupInfoOption } from '../models/group-info-option.model';
import { OptionGottenResource } from '../models/option-gotten-resource.model';
import { UserOptionGottenResource } from '../models/userinfo-option-gotten.model';
import { GroupInfoOptionGottenResource } from '../models/group-info-option-gotten-resource.model';
import { RoleInfoOptionGottenResource } from '../models/role-info-option-gotten-resource.model';

@Injectable({
  providedIn: 'root',
})
export class OptionService {
  private readonly baseApiUrl = environment.apiEndpoint;

  constructor(private http: HttpClient) {}

  /**
   * 取得 Data Type 配置資料
   * @return Observable<MenuItem[]
   */
  public getDataTypes(): Observable<Option[]> {
    return this.http.get<Option[]>('/data-type.json').pipe(
      map((response) => {
        return response;
      }),
    );
  }

  // /**
  //  * 取得 Service 配置資料
  //  * @return Observable<MenuItem[]
  //  */
  // public getServiceList(): Observable<Option[]> {
  //   return this.http.get<Option[]>('/services.json').pipe(
  //     map((response) => {
  //       return response;
  //     })
  //   );
  // }

  /**
   * 取得 Setting Type 種類
   * @param type
   * @return  Observable<Option[]>
   */
  public getSettingTypes(service: string, type: string): Observable<Option[]> {
    const url = this.baseApiUrl + '/options/query';
    let params = new HttpParams()
      .set('type', type ? type : '')
      .set('service', service ? service : '');
    return this.http.get<OptionGottenResource>(url, { params }).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }

  /**
   * 取得 UserInfo AutoComplete 資料
   * @param keyword
   * @returns
   */
  public getUserOptions(keyword: string): Observable<UserInfoOption[]> {
    const url = this.baseApiUrl + '/options/getUserOptions';
    let params = new HttpParams().set('keyword', keyword ? keyword : '');
    return this.http.get<UserOptionGottenResource>(url, { params }).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }

  /**
   * 取得 RoleInfo AutoComplete 資料
   * @param keyword
   * @returns
   */
  public getRoleOptions(
    service: string,
    keyword: string,
  ): Observable<RoleInfoOption[]> {
    const url = this.baseApiUrl + '/options/roles';
    let params = new HttpParams()
      .set('service', service ? service : '')
      .set('keyword', keyword ? keyword : '');
    return this.http.get<RoleInfoOptionGottenResource>(url, { params }).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }

  /**
   * 取得 Group AutoComplete 資料
   * @param keyword
   * @returns
   */
  public getGroupOptions(
    service: string,
    keyword: string,
  ): Observable<GroupInfoOption[]> {
    const url = this.baseApiUrl + '/options/groups';
    let params = new HttpParams()
      .set('service', service ? service : '')
      .set('keyword', keyword ? keyword : '');
    return this.http.get<GroupInfoOptionGottenResource>(url, { params }).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }

  /**
   * 取得 GroupInfo Dropdown 資料
   * @param service 服務
   * @returns
   */
  public getGroupDropdownOptions(service: string): Observable<Option[]> {
    const url = this.baseApiUrl + '/options/groups/types';
    let params = new HttpParams().set('service', service ? service : '');
    return this.http.get<OptionGottenResource>(url, { params }).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }

  /**
   * 取得 RoleInfo Dropdown 資料
   * @param service 服務
   * @returns
   */
  public getRoleDropdownOptions(service: string): Observable<Option[]> {
    const url = this.baseApiUrl + '/options/roles/types';
    let params = new HttpParams().set('service', service ? service : '');
    return this.http.get<OptionGottenResource>(url, { params }).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }

  /**
   * 取得 FunctionInfo Dropdown 資料
   * @param service 服務
   * @returns
   */
  public getFunctionDropdownOptions(service: string): Observable<Option[]> {
    const url = this.baseApiUrl + '/options/functions/types';
    let params = new HttpParams().set('service', service ? service : '');
    return this.http.get<OptionGottenResource>(url, { params }).pipe(
      map((response) => {
        return response?.data;
      }),
    );
  }
}
