import { APP_INITIALIZER, ApplicationConfig, ENVIRONMENT_INITIALIZER, inject, provideZoneChangeDetection, ViewContainerRef } from '@angular/core';
import { provideRouter } from '@angular/router';

import {
  provideHttpClient,
  withInterceptorsFromDi
} from '@angular/common/http';
import { routes } from './app.routes';
import { DialogService } from './core/services/dialog.service';
import { initializeTheme, ThemeService } from './core/services/theme.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

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
  ],
};
