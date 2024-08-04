import { APP_INITIALIZER, ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import {
  provideHttpClient,
  withInterceptorsFromDi
} from '@angular/common/http';
import { routes } from './app.routes';
import { initializeTheme, ThemeService } from './core/services/theme.service';


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeTheme,
      multi: true,
      deps: [ThemeService],
    }
  ],
};
