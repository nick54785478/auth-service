import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { CreateSetting } from '../models/create-setting-request.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { SettingQueried } from '../models/setting-query.model';
import { BaseResponse } from '../../../shared/models/base-response.model';
import { UpdateSettingResource } from '../models/update-setting-request.model';
import { map } from 'rxjs';
import { SettingSummaryGottenResoruce } from '../models/setting-summary-gotten-response.model';

@Injectable({
  providedIn: 'root',
})
export class SettingService {
  private readonly baseApiUrl = environment.apiEndpoint;

  constructor(private http: HttpClient) {}

  /**
   * 新增一筆 Setting 資料
   * @param request
   * @return Observable<any>
   */
  create(request: CreateSetting): Observable<any> {
    const url = this.baseApiUrl + '/settings';
    return this.http.post(url, request);
  }

  /**
   * 更新一筆資料
   * @param request
   * @return Observable<any>
   */
  update(id: number, request: UpdateSettingResource): Observable<any> {
    const url = this.baseApiUrl + '/settings/' + id;
    return this.http.put(url, request);
  }

  /**
   * 查詢
   * @param dataType
   * @param type
   * @param name
   */
  summary(
    service: string,
    dataType: string,
    type: string,
    name: string,
    activeFlag: string,
  ): Observable<SettingQueried[]> {
    const url = this.baseApiUrl + '/settings/summary';
    let params = new HttpParams()
      .set('service', service ? service : '')
      .set('dataType', dataType ? dataType : '')
      .set('type', type ? type : '')
      .set('name', name ? name : '')
      .set('activeFlag', activeFlag ? activeFlag : 'Y');
    return this.http.get<SettingSummaryGottenResoruce>(url, { params }).pipe(
      map((res) => {
        return res?.data;
      }),
    );
  }

  /**
   * 刪除
   *
   * @param id
   */
  delete(id: number): Observable<BaseResponse> {
    const url = this.baseApiUrl + '/settings/' + id;
    return this.http.delete<BaseResponse>(url);
  }
}
