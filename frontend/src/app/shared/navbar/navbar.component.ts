import { Component } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroArrowRightStartOnRectangle,
  heroBars3,
  heroBell,
  heroMagnifyingGlass,
  heroUser,
} from '@ng-icons/heroicons/outline';

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

}
