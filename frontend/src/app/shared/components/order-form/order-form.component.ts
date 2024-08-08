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
import { OrderRequest, OrderRequestSchema, OrderType } from '../../models/order.model';

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
    portfolioId: new FormControl('', Validators.required),
    ticker: new FormControl('', Validators.required),
    orderType: new FormControl('MARKET', Validators.required),
    side: new FormControl('', Validators.required),
    unitPrice: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[0-9]+(\.[0-9]{1,2})?$/),
    ]),
    quantity: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[0-9]+$/),
    ]),
  });

  constructor(private portfolioService: PortfolioService) {
    effect(() => {
      this.orderForm = new FormGroup({
        portfolioId: new FormControl(
          this.defaultportfolio() || undefined,
          Validators.required
        ),
        ticker: new FormControl(
          this.defaultstock() || 'MSFT',
          Validators.required
        ),
        orderType: new FormControl('MARKET', Validators.required),
        side: new FormControl(this.defaultside() || 'BUY', Validators.required),
        unitPrice: new FormControl('', [
          Validators.required,
          Validators.pattern(/^[0-9]+(\.[0-9]{1,2})?$/),
        ]),
        quantity: new FormControl('', [
          Validators.required,
          Validators.pattern(/^[0-9]+$/),
        ]),
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
    }
  }
}
