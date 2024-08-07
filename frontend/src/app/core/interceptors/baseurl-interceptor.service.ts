import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BaseUrlInterceptorService implements HttpInterceptor {
  constructor() {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const apiReq = req.clone({
      url: `${environment.apiUrl}${this.removeLeadingSlash(req.url)}`,
    });

    return next.handle(apiReq);
  }

  private removeLeadingSlash(url: string): string {
    return url.replace(/^\//, '');
  }
}
