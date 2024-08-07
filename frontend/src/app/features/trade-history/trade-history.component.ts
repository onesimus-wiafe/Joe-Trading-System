import { Component, signal } from '@angular/core';
import { PaginationComponent } from '../../shared/components/pagination/pagination.component';
import { OrderFormComponent } from '../../shared/components/order-form/order-form.component';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroXMark } from '@ng-icons/heroicons/outline';
import { CurrencyPipe, DatePipe, PercentPipe } from '@angular/common';
import { OrderStatus, Trade } from '../../shared/models/order.model';

@Component({
  selector: 'app-trade-history',
  standalone: true,
  imports: [
    PaginationComponent,
    OrderFormComponent,
    NgIconComponent,
    CurrencyPipe,
    PercentPipe,
    DatePipe,
  ],
  templateUrl: './trade-history.component.html',
  styleUrl: './trade-history.component.css',
  providers: [provideIcons({ heroXMark })],
})
export class TradeHistoryComponent {
  dialogOpenSignal = signal<boolean>(false);

  showDialog() {
    this.dialogOpenSignal.set(true);
  }

  closeDialog() {
    this.dialogOpenSignal.set(false);
  }

  trades: Trade[] = [
    {
      id: 1,
      symbol: 'AMZN',
      name: 'Amazon',
      price: 161.46,
      quantity: 10,
      side: 'BUY',
      splitted: false,
      splitAmounts: [],
      status: OrderStatus.OPEN,
      date: new Date(),
      completedDate: new Date(),
    },
  ];

  getIconForSymbol(symbol: string) {
    return `assets/${symbol.toLowerCase()}.svg`;
  }
}
