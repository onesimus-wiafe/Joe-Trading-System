import { Routes } from '@angular/router';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { LoginComponent } from './features/login/login.component';
import { SignupComponent } from './features/signup/signup.component';
import { PortfolioComponent } from './features/portfolio/portfolio.component';
import { MainLayoutComponent } from './shared/components/main-layout/main-layout.component';
import { PortfolioDetailsComponent } from './features/portfolio/portfolio-details/portfolio-details.component';
import { AuthGuard } from './core/guards/auth.guard';
import { UnauthGuard } from './core/guards/unauth.guard';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        redirectTo: '/login',
      },
      {
        path: 'dashboard',
        component: DashboardComponent,
      },
      {
        path: 'portfolios',
        component: PortfolioComponent,
      },
      {
        path: 'portfolios/:porfolioId',
        component: PortfolioDetailsComponent,
      }
    ],
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [UnauthGuard],
  },
  {
    path: 'signup',
    component: SignupComponent,
    canActivate: [UnauthGuard],
  },
];
