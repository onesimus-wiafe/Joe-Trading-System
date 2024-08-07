import {
  APP_INITIALIZER,
  ApplicationConfig,
  ENVIRONMENT_INITIALIZER,
  inject,
  provideZoneChangeDetection
} from '@angular/core';
import { provideRouter } from '@angular/router';

import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { routes } from './app.routes';
import { BaseUrlInterceptorService } from './core/interceptors/baseurl-interceptor.service';
import { TokenInterceptorService } from './core/interceptors/token-interceptor.service';
import { DialogService } from './core/services/dialog.service';
import { initializeTheme, ThemeService } from './core/services/theme.service';

export function initializeDialogService() {
  return () => {
    inject(DialogService);
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptorService,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: BaseUrlInterceptorService,
      multi: true,
    },
    {
      provide: APP_INITIALIZER,
      useFactory: initializeTheme,
      multi: true,
      deps: [ThemeService],
    },
    {
      provide: ENVIRONMENT_INITIALIZER,
      useFactory: initializeDialogService,
      deps: [],
      multi: true,
    },
    provideAnimations(),
  ],
};
