import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroMinus, heroPlus, heroXMark } from '@ng-icons/heroicons/outline';
import { needConfirmation } from '../../../core/services/dialog.service';
import { PortfolioService } from '../../../core/services/portfolio.service';
import { OrderFormComponent } from '../../../shared/components/order-form/order-form.component';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import {
  Portfolio,
  PortfolioState,
} from '../../../shared/models/portfolio.model';
import { Stock } from '../../../shared/models/stock.model';
import { ToastService, ToastVariant } from '../../../core/services/toast.service';

@Component({
  selector: 'app-portfolio-details',
  standalone: true,
  imports: [
    CurrencyPipe,
    CommonModule,
    PaginationComponent,
    ReactiveFormsModule,
    NgIconComponent,
    OrderFormComponent,
  ],
  templateUrl: './portfolio-details.component.html',
  styleUrl: './portfolio-details.component.css',
  providers: [provideIcons({ heroMinus, heroPlus, heroXMark })],
})
export class PortfolioDetailsComponent implements OnInit {
  portfolioForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    description: new FormControl(''),
  });

  portfolio = signal<Portfolio>({
    id: 1,
    name: '',
    state: PortfolioState.Active,
    value: 10000,
    description: '',
    createdAt: new Date(),
    updatedOn: new Date(),
  });

  selectedStock = signal<Stock | null>(null);
  selectedSide = signal<'BUY' | 'SELL' | null>(null);

  dialogOpenSignal = signal<boolean>(false);

  stocks: (Stock & { quantity: number })[] = [
    {
      name: 'Amazon',
      price: 161.46,
      symbol: 'AMZN',
      change: -0.1228,
      pic: 'assets/amazon.svg',
      quantity: 10,
    },
    {
      name: 'Apple',
      price: 222.92,
      symbol: 'AAPL',
      change: 0.0209,
      pic: 'assets/apple.svg',
      quantity: 20,
    },
    {
      name: 'Google',
      price: 166.63,
      symbol: 'GOOGL',
      change: -0.0337,
      pic: 'assets/google.svg',
      quantity: 15,
    },
    {
      name: 'IBM',
      price: 186.32,
      symbol: 'IBM',
      change: -0.0176,
      pic: 'assets/ibm.svg',
      quantity: 5,
    },
    {
      name: 'Microsoft',
      price: 405.76,
      symbol: 'MSFT',
      change: -0.0272,
      pic: 'assets/microsoft.svg',
      quantity: 12,
    },
    {
      name: 'Netflix',
      price: 613.28,
      symbol: 'NFLX',
      change: -0.0185,
      pic: 'assets/netflix.svg',
      quantity: 8,
    },
    {
      name: 'Oracle',
      price: 132.59,
      symbol: 'ORCL',
      change: -0.0358,
      pic: 'assets/oracle.svg',
      quantity: 7,
    },
    {
      name: 'Tesla',
      price: 211.26,
      symbol: 'TSLA',
      change: -0.0258,
      pic: 'assets/tesla.svg',
      quantity: 3,
    },
  ];

  constructor(
    private _activatedRoute: ActivatedRoute,
    private portfolioService: PortfolioService,
    private router: Router,
    private toastService: ToastService
  ) {
    this._activatedRoute.params.subscribe((params) => {});
  }

  ngOnInit(): void {
    this.loadPortfolio();
  }

  loadPortfolio() {
    const id = this._activatedRoute.snapshot.params['porfolioId'];
    this.portfolioService.getPortfolio(id).subscribe({
      next: (portfolio) => {
        this.portfolio.set(portfolio);
      },
      error: (err) => {
        this.toastService.initiate({
          message: err.error.detail,
          variant: ToastVariant.Error,
        });
      },
    });
  }

  showDialog() {
    this.dialogOpenSignal.set(true);
  }

  closeDialog() {
    this.dialogOpenSignal.set(false);
  }

  createPortfolio() {
    if (this.portfolioForm.invalid) {
      this.portfolioForm.markAllAsTouched();
      return;
    }
  }

  buyStock(stock: Stock) {
    this.selectedSide.set('BUY');
    this.selectedStock.set(stock);
    this.showDialog();
  }

  sellStock(stock: Stock) {
    this.selectedSide.set('SELL');
    this.selectedStock.set(stock);
    this.showDialog();
  }

  @needConfirmation({
    message: 'Are you sure you want to delete this portfolio?',
    title: 'Delete Portfolio',
  })
  deletePortfolio() {
    this.portfolioService.deletePortfolio(this.portfolio().id).subscribe({
      next: () => {
        this.router.navigate(['/portfolios']);
      },
      error: (err) => {
        this.toastService.initiate({
          message: err.error.detail || 'Failed to delete portfolio',
          variant: ToastVariant.Error,
        });
      },
    });
  }
}
