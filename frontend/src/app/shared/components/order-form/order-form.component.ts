import {
  Component,
  computed,
  effect,
  input,
  OnInit,
  output,
  signal,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import * as v from 'valibot';
import { PortfolioService } from '../../../core/services/portfolio.service';
import { PortfolioListResponse } from '../../models/portfolio.model';
import {
  OrderRequest,
  OrderRequestSchema,
  OrderType,
} from '../../models/order.model';
import { validateField } from '../../../core/validators/validate';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css',
})
export class OrderFormComponent implements OnInit {
  formSubmit = output<OrderRequest>();

  portfolios = signal<PortfolioListResponse>({
    data: [],
    totalElements: 0,
    totalPages: 0,
    currentPage: 0,
  });

  defaultportfolio = input<number | undefined>(1);
  defaultstock = input<string | undefined>('MSFT');
  defaultside = input<'BUY' | 'SELL' | undefined | null>('BUY');

  orderForm: FormGroup = new FormGroup({
    portfolioId: new FormControl(
      '',
      validateField(OrderRequestSchema.entries.portfolioId)
    ),
    ticker: new FormControl(
      '',
      validateField(OrderRequestSchema.entries.ticker)
    ),
    orderType: new FormControl(
      'MARKET',
      validateField(OrderRequestSchema.entries.orderType)
    ),
    side: new FormControl('', validateField(OrderRequestSchema.entries.side)),
    unitPrice: new FormControl(
      '',
      validateField(OrderRequestSchema.entries.unitPrice)
    ),
    quantity: new FormControl(
      '',
      validateField(OrderRequestSchema.entries.quantity)
    ),
  });

  constructor(private portfolioService: PortfolioService) {
    effect(() => {
      this.orderForm = new FormGroup({
        portfolioId: new FormControl(
          this.defaultportfolio() || undefined,
          validateField(OrderRequestSchema.entries.portfolioId)
        ),
        ticker: new FormControl(
          this.defaultstock() || 'MSFT',
          validateField(OrderRequestSchema.entries.ticker)
        ),
        orderType: new FormControl(
          'MARKET',
          validateField(OrderRequestSchema.entries.orderType)
        ),
        side: new FormControl(
          this.defaultside() || 'BUY',
          validateField(OrderRequestSchema.entries.side)
        ),
        unitPrice: new FormControl(
          '',
          validateField(OrderRequestSchema.entries.unitPrice)
        ),
        quantity: new FormControl(
          '',
          validateField(OrderRequestSchema.entries.quantity)
        ),
      });
    });
  }

  ngOnInit(): void {
    this.loadPortfolios();
  }

  loadPortfolios() {
    this.portfolioService.getPortfolios().subscribe({
      next: (response) => {
        this.portfolios.set(response);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  handleSubmit() {
    if (this.orderForm.invalid) {
      this.orderForm.markAllAsTouched();
      return;
    }

    const result = v.safeParse(OrderRequestSchema, this.orderForm.value);
    if (result.success) {
      this.formSubmit.emit(result.output);
      this.orderForm.reset();
    } else {
      console.error(result.issues);
      console.log(this.orderForm.value);
    }
  }
}
