import { Component, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroBuildingOffice2,
  heroMoon,
  heroReceiptRefund,
  heroSquares2x2,
  heroSun,
  heroUser,
} from '@ng-icons/heroicons/outline';
import { ThemeService } from '../../../core/services/theme.service';
import { NavbarComponent } from '../../navbar/navbar.component';
import { AuthService } from '../../../core/services/auth.service';
import { AccountType } from '../../models/user.model';

@Component({
  selector: 'app-drawer',
  standalone: true,
  imports: [NavbarComponent, NgIconComponent, RouterLink],
  providers: [
    provideIcons({
      heroSquares2x2,
      heroBuildingOffice2,
      heroReceiptRefund,
      heroMoon,
      heroSun,
      heroUser,
    }),
  ],
  templateUrl: './drawer.component.html',
  styleUrl: './drawer.component.css',
})
export class DrawerComponent {
  constructor(
    private themeService: ThemeService,
    private authService: AuthService
  ) {}

  theme = computed(() => this.themeService.theme());
  user = computed(() => this.authService.authInfo()?.user);
  private menuItems = signal<
    {
      link: string;
      icon: string;
      label: string;
      allowedRoles?: AccountType[];
    }[]
  >([
    {
      link: '/dashboard',
      icon: 'heroSquares2x2',
      label: 'Dashboard',
      allowedRoles: [AccountType.ADMIN, AccountType.USER],
    },
    {
      link: '/portfolios',
      icon: 'heroBuildingOffice2',
      label: 'Portfolios',
      allowedRoles: [AccountType.USER],
    },
    {
      link: '/trade-history',
      icon: 'heroReceiptRefund',
      label: 'Trade History',
      allowedRoles: [AccountType.USER],
    },
    {
      link: '/users',
      icon: 'heroUser',
      label: 'Users',
      allowedRoles: [AccountType.ADMIN],
    },
  ]);

  get menuItems$() {
    const accountType = this.user()?.accountType;
    if (!accountType) {
      return [];
    }

    return this.menuItems().filter((item) =>
      item.allowedRoles?.includes(accountType)
    );
  }

  toggleTheme($event: Event) {
    $event.preventDefault();
    this.themeService.toggleTheme();
  }
}
