import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { UnauthGuard } from './core/guards/unauth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./shared/components/main-layout/main-layout.component').then(
        (m) => m.MainLayoutComponent
      ),
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: '/login',
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component').then(
            (m) => m.DashboardComponent
          ),
      },
      {
        path: 'portfolios',
        loadComponent: () =>
          import('./features/portfolio/portfolio.component').then(
            (m) => m.PortfolioComponent
          ),
      },
      {
        path: 'portfolios/:porfolioId',
        loadComponent: () =>
          import(
            './features/portfolio/portfolio-details/portfolio-details.component'
          ).then((m) => m.PortfolioDetailsComponent),
      },
      {
        path: 'trade-history',
        loadComponent: () =>
          import('./features/trade-history/trade-history.component').then(
            (m) => m.TradeHistoryComponent
          ),
      },
    ],
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/login/login.component').then((m) => m.LoginComponent),
    canActivate: [UnauthGuard],
  },
  {
    path: 'signup',
    loadComponent: () =>
      import('./features/signup/signup.component').then(
        (m) => m.SignupComponent
      ),
    canActivate: [UnauthGuard],
  },
];
