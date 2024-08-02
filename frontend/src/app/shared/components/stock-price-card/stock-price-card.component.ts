import { CommonModule } from '@angular/common';
import { Component, input } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { remixArrowDownSFill, remixArrowUpSFill } from '@ng-icons/remixicon';
import { Stock } from './stock.model';

@Component({
  selector: 'app-stock-price-card',
  standalone: true,
  imports: [CommonModule, NgIconComponent],
  providers: [
    provideIcons({
      remixArrowDownSFill,
      remixArrowUpSFill,
    }),
  ],
  templateUrl: './stock-price-card.component.html',
  styleUrl: './stock-price-card.component.css',
})
export class StockPriceCardComponent {
  stock = input.required<Stock>();
}
