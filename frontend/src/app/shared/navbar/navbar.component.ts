import { Component, computed } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroArrowRightStartOnRectangle,
  heroBars3,
  heroBell,
  heroMagnifyingGlass,
  heroUser,
} from '@ng-icons/heroicons/outline';
import { AuthService } from '../../core/services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { UpperCasePipe } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [NgIconComponent, UpperCasePipe, RouterLink],
  providers: [
    provideIcons({
      heroBars3,
      heroBell,
      heroUser,
      heroArrowRightStartOnRectangle,
      heroMagnifyingGlass,
    }),
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
})
export class NavbarComponent {
  constructor(private authService: AuthService) {}

  user = computed(() => this.authService.authInfo()?.user);

  logout() {
    this.authService.logout();
  }
}
