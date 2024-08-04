import { Component, OnInit, signal } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroBuildingOffice2,
  heroMoon,
  heroReceiptRefund,
  heroSquares2x2,
  heroSun,
} from '@ng-icons/heroicons/outline';
import { NavbarComponent } from '../../navbar/navbar.component';

@Component({
  selector: 'app-drawer',
  standalone: true,
  imports: [NavbarComponent, NgIconComponent],
  providers: [
    provideIcons({
      heroSquares2x2,
      heroBuildingOffice2,
      heroReceiptRefund,
      heroMoon,
      heroSun,
    }),
  ],
  templateUrl: './drawer.component.html',
  styleUrl: './drawer.component.css',
})
export class DrawerComponent implements OnInit {
  theme = signal<'light' | 'dark'>('light');

  ngOnInit(): void {
    let systemIsDarkTheme = window.matchMedia(
      '(prefers-color-scheme: dark)'
    ).matches;
    const theme = localStorage.getItem('theme') as 'light' | 'dark';
    !!theme
      ? this.theme.set(theme)
      : this.theme.set(systemIsDarkTheme ? 'dark' : 'light');

    document.querySelector('html')?.setAttribute('data-theme', theme);
  }

  toggleTheme($event: Event) {
    const theme = ($event.target as HTMLInputElement).checked
      ? 'light'
      : 'dark';
    localStorage.setItem('theme', theme);
    document.querySelector('html')?.setAttribute('data-theme', theme);
  }
}
