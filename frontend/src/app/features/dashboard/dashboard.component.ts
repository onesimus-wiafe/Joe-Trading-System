import { CurrencyPipe } from '@angular/common';
import { Component, computed } from '@angular/core';
import { CanvasJSAngularChartsModule } from '@canvasjs/angular-charts';
import { OrderService } from '../../core/services/order.service';
import { ThemeService } from '../../core/services/theme.service';
import { DrawerComponent } from '../../shared/components/drawer/drawer.component';
import { OrderFormComponent } from '../../shared/components/order-form/order-form.component';
import { StockPriceCardComponent } from '../../shared/components/stock-price-card/stock-price-card.component';
import { ToastService, ToastVariant } from '../../core/services/toast.service';
import { Stock } from '../../shared/models/stock.model';
import { OrderRequest } from '../../shared/models/order.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    DrawerComponent,
    StockPriceCardComponent,
    CurrencyPipe,
    CanvasJSAngularChartsModule,
    OrderFormComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
})
export class DashboardComponent {
  constructor(
    private themeService: ThemeService,
    private orderService: OrderService,
    private toastService: ToastService
  ) {}

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

  placeOrder($event: OrderRequest) {
    console.log("placing order", $event);
    this.orderService.createOrder($event).subscribe({
      next: () => {
        this.toastService.initiate({
          message: 'Order placed successfully',
          variant: ToastVariant.Success,
        });
      },
      error: () => {
        this.toastService.initiate({
          message: 'Failed to place order',
          variant: ToastVariant.Error,
        });
      },
    });
  }

  stockChartOptions = computed(() => {
    return {
      exportEnabled: true,
      title: {
        text: 'Ethereum Price',
      },
      theme: this.themeService.theme() == 'dark' ? 'dark2' : 'light1',
      axisX: {
        valueFormatString: 'MMM',
        crosshair: {
          enabled: true,
          valueFormatString: 'MMM YYYY',
          snapToDataPoint: true,
        },
      },
      axisY: {
        title: 'Price in USD',
        prefix: '$',
        crosshair: {
          enabled: true,
        },
      },
      data: [
        {
          type: 'candlestick',
          yValueFormatString: '$##.##',
          xValueFormatString: 'MMM YYYY',
          dataPoints: [
            {
              x: new Date(2021, 0, 1),
              y: [737.708374, 1467.784912, 718.109497, 1314.986206],
            },
            {
              x: new Date(2021, 1, 1),
              y: [1314.855225, 2036.286499, 1274.357788, 1416.04895],
            },
            {
              x: new Date(2021, 2, 1),
              y: [1417.151123, 1947.837769, 1416.416138, 1918.362061],
            },
            {
              x: new Date(2021, 3, 1),
              y: [1919.157227, 2797.972412, 1912.178467, 2773.207031],
            },
            {
              x: new Date(2021, 4, 1),
              y: [2772.838379, 4362.350586, 1737.46875, 2714.945313],
            },
            {
              x: new Date(2021, 5, 1),
              y: [2707.560547, 2891.254883, 1707.600586, 2274.547607],
            },
            {
              x: new Date(2021, 6, 1),
              y: [2274.397461, 2551.161133, 1722.050781, 2536.209961],
            },
            {
              x: new Date(2021, 7, 1),
              y: [2530.462891, 3466.992188, 2449.353516, 3433.732666],
            },
            {
              x: new Date(2021, 8, 1),
              y: [3430.762451, 4022.469238, 2676.407471, 3001.678955],
            },
            {
              x: new Date(2021, 9, 1),
              y: [3001.129395, 4455.735352, 2978.654297, 4288.074219],
            },
            {
              x: new Date(2021, 10, 1),
              y: [4288.217285, 4891.70459, 3933.506592, 4631.479004],
            },
            {
              x: new Date(2021, 11, 1),
              y: [4623.679688, 4780.732422, 3525.494141, 3682.632813],
            },
          ],
        },
      ],
    };
  });
}
