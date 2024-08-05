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
      heroUser
    }),
  ],
  templateUrl: './drawer.component.html',
  styleUrl: './drawer.component.css',
})
export class DrawerComponent {
  constructor(private themeService: ThemeService) {
  }

  theme = computed(() => this.themeService.theme());

  toggleTheme($event: Event) {
    $event.preventDefault();
    this.themeService.toggleTheme();
  }
}
