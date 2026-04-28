import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { JwtToken } from '../models/jwt-token.model';
import { Observable } from 'rxjs/internal/Observable';
import { url } from 'inspector';
import { Login } from '../models/login-request.model';
import { UserProfileQueried } from '../models/user-profile.model';
import { UserInfoGottenResource } from '../models/user-profile-gotten.model';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private readonly baseApiUrl = environment.apiEndpoint;

  constructor(private http: HttpClient) {}

  /**
   * 使用者登入
   * @param request
   * @returns JwtToken
   */
  login(request: Login, serviceHeader?: string): Observable<any> {
    // 1. 建立自訂的 HttpHeaders
    const customHeaders = new HttpHeaders({
      'service-header': serviceHeader ? serviceHeader : 'AUTH_SERVICE',
    });

    const url = this.baseApiUrl + '/login';
    return this.http.post<JwtToken>(url, request, {
      headers: customHeaders,
    });
  }

  /**
   * 取得使用者資料
   * @param username
   * @returns Observable<UserProfile>
   */
  getUserInfo(username: string): Observable<UserProfileQueried> {
    const url = this.baseApiUrl + '/users/' + username;
    return this.http
      .get<UserInfoGottenResource>(url)
      .pipe(map((res) => res?.data));
  }
}
