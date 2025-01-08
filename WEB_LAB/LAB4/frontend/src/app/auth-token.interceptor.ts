import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse,
  HttpClient,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

interface RefreshDTO {
  data: { accessToken: string; refreshToken: string };
  message: string | null;
}

@Injectable()
export class AuthTokenInterceptor implements HttpInterceptor {
  private isRefreshing = false;

  constructor(private http: HttpClient) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const accessToken = localStorage.getItem('accessToken');
    const isAuthUrl = req.url.includes('/auth/');
    if (accessToken && !isAuthUrl) {
      req = req.clone({ setHeaders: { Authorization: `Bearer ${accessToken}` } });
    }
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401 && !isAuthUrl && !this.isRefreshing) {
          this.isRefreshing = true;
          const refreshToken = localStorage.getItem('refreshToken') || '';
          return this.http.post<RefreshDTO>('/auth/refresh', { refreshToken }, { withCredentials: true }).pipe(
            switchMap(res => {
              this.isRefreshing = false;
              if (res.data) {
                localStorage.setItem('accessToken', res.data.accessToken);
                localStorage.setItem('refreshToken', res.data.refreshToken);
                const newReq = req.clone({
                  setHeaders: { Authorization: `Bearer ${res.data.accessToken}` }
                });
                return next.handle(newReq);
              }
              return throwError(() => error);
            }),
            catchError(err => {
              this.isRefreshing = false;
              localStorage.removeItem('accessToken');
              localStorage.removeItem('refreshToken');
              return throwError(() => err);
            })
          );
        }
        return throwError(() => error);
      })
    );
  }
}