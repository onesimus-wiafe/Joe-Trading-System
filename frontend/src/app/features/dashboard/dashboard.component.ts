import { Component } from '@angular/core';
import { DrawerComponent } from "../../shared/components/drawer/drawer.component";
import { StockPriceCardComponent } from "../../shared/components/stock-price-card/stock-price-card.component";
import { Stock } from '../../shared/components/stock-price-card/stock.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DrawerComponent, StockPriceCardComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  stocks: Stock[] = [
    {
      name: 'Amazon',
      price: 161.46,
      symbol: 'AMZN',
      change: -0.1228,
      pic: 'assets/amazon.svg',
    },
    {
      name: 'Apple',
      price: 222.92,
      symbol: 'AAPL',
      change: 0.0209,
      pic: 'assets/apple.svg',
    },
    {
      name: 'Google',
      price: 166.63,
      symbol: 'GOOGL',
      change: -0.0337,
      pic: 'assets/google.svg',
    },
    {
      name: 'IBM',
      price: 186.32,
      symbol: 'IBM',
      change: -0.0176,
      pic: 'assets/ibm.svg',
    },
    {
      name: 'Microsoft',
      price: 405.76,
      symbol: 'MSFT',
      change: -0.0272,
      pic: 'assets/microsoft.svg',
    },
    {
      name: 'Netflix',
      price: 613.28,
      symbol: 'NFLX',
      change: -0.0185,
      pic: 'assets/netflix.svg',
    },
    {
      name: 'Oracle',
      price: 132.59,
      symbol: 'ORCL',
      change: -0.0358,
      pic: 'assets/oracle.svg',
    },
    {
      name: 'Tesla',
      price: 211.26,
      symbol: 'TSLA',
      change: -0.0258,
      pic: 'assets/tesla.svg',
    },
  ];
}
