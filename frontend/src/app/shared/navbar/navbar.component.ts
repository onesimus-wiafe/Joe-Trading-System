import { Component } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroArrowRightStartOnRectangle,
  heroBars3,
  heroBell,
  heroMagnifyingGlass,
  heroUser,
} from '@ng-icons/heroicons/outline';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [NgIconComponent],
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
  constructor(private authService: AuthService, private router: Router) {}

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
