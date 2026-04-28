import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import {
  BehaviorSubject,
  catchError,
  EMPTY,
  filter,
  switchMap,
  take,
  tap,
  throwError,
} from 'rxjs';
import { LoginService } from '../../features/layout/services/login.service';
import { StorageService } from '../services/storage.service';
import { SystemStorageKey } from '../enums/system-storage.enum';

// 【全域變數】用來控制併發請求
let isRefreshing = false;
let refreshTokenSubject = new BehaviorSubject<string | null>(null);

/**
 * JWT 攔截器
 */
export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const storageService = inject(StorageService);

  console.log('攔截請求' + req.url);

  // 1. 放行不需要 Token 的請求 (例如登入、刷新 Token 的 API)
  if (req.url.includes('/api/v1/refresh') || req.url.includes('/login')) {
    return next(req);
  }

  return authService.getJwtToken().pipe(
    switchMap((token) => {
      // 2. 檢查 Token 是否過期
      const isExpired = authService.checkExpired(token);

      if (token && isExpired) {
        // --- 進入 Token 刷新流程 ---

        if (!isRefreshing) {
          // 【情境 A：我是第一支發現過期的 API】
          isRefreshing = true;
          refreshTokenSubject.next(null); // 清空佇列，擋住後面的請求

          const refreshToken = storageService.getLocalStorageItem(
            SystemStorageKey.REFRESH_TOKEN,
          );

          // 防呆：確保 refreshToken 不是 null 也不是字串 'undefined'
          if (refreshToken && refreshToken !== 'undefined') {
            console.log('Token 過期，開始刷新流程...');

            // 注意：這裡是用 return .pipe()，代表必須【等待】刷新完畢才會繼續
            return authService.refreshToken(refreshToken).pipe(
              switchMap((res) => {
                isRefreshing = false; // 刷新成功，解鎖

                // 更新 Storage
                storageService.setLocalStorageItem(
                  SystemStorageKey.JWT_TOKEN,
                  res?.token,
                );
                storageService.setSessionStorageItem(
                  SystemStorageKey.JWT_TOKEN,
                  res?.token,
                );
                storageService.setLocalStorageItem(
                  SystemStorageKey.REFRESH_TOKEN,
                  res?.refreshToken,
                );
                storageService.setSessionStorageItem(
                  SystemStorageKey.REFRESH_TOKEN,
                  res?.refreshToken,
                );

                // 通知佇列中其他正在排隊的 API：「拿到新 Token 囉，大家可以走了！」
                refreshTokenSubject.next(res?.token);

                // 把第一支 API 的 Header 換上新 Token 並送出
                const clonedReq = req.clone({
                  setHeaders: { Authorization: `Bearer ${res?.token}` },
                });
                return next(clonedReq);
              }),
              catchError((err) => {
                // 如果 RefreshToken 也過期了，或是後端報錯
                isRefreshing = false;
                console.error('刷新 Token 失敗，請重新登入', err);

                // 這裡通常會加上 authService.logout() 將使用者踢回登入頁
                storageService.clearAll();

                return EMPTY;
              }),
            );
          } else {
            // 根本沒有 RefreshToken，直接讓他過，讓後端回傳 401
            return next(req);
          }
        } else {
          // 【情境 B：已經有別的 API 正在刷新 Token，我必須排隊等待】
          console.log('正在刷新中，將請求放入排隊佇列...');
          return refreshTokenSubject.pipe(
            filter((newToken) => newToken !== null), // 等待直到有人把新 Token 丟進佇列
            take(1), // 只拿一次新 Token 就結束等待
            switchMap((newToken) => {
              // 拿到新 Token，換裝上陣，放行請求！
              const clonedReq = req.clone({
                setHeaders: { Authorization: `Bearer ${newToken}` },
              });
              return next(clonedReq);
            }),
          );
        }
      }

      // 3. Token 沒過期，正常附加 Token 放行
      if (token) {
        req = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` },
        });
      }

      return next(req).pipe(
        catchError((error) => {
          if (error.status === 500) {
            console.error('伺服器錯誤:', error.message);
          } else {
            console.error('其他錯誤:', error.message);
          }
          return throwError(() => error); // 保持錯誤向外拋出，不要吃掉錯誤
        }),
      );
    }),
  );
};
