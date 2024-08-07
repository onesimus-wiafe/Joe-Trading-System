import { Component, computed, effect, input, output } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { OrderRequest, OrderRequestSchema } from '../../models/stock.model';
import * as v from 'valibot';
import { PortfolioService } from '../../../core/services/portfolio.service';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css',
})
export class OrderFormComponent {
  constructor(private portfolioService: PortfolioService) {
    effect(() => {
      this.orderForm = new FormGroup({
        portfolio: new FormControl(
          this.defaultportfolio()||undefined,
          Validators.required
        ),
        stock: new FormControl(
          this.defaultstock() || 'MSFT',
          Validators.required
        ),
        tradeType: new FormControl('MARKET', Validators.required),
        side: new FormControl(this.defaultside() || 'BUY', Validators.required),
        price: new FormControl('', [
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

  portfolios = computed(() => this.portfolioService.getPortfolios());

  handleSubmit() {
    if (this.orderForm.invalid) {
      this.orderForm.markAllAsTouched();
      return;
    }

    const result = v.safeParse(OrderRequestSchema, this.orderForm.value);
    if (result.success) {
      this.submit.emit(result.output);
      this.orderForm.reset();
    } else {
      console.error(result.issues);
    }
  }

  defaultportfolio = input<number | undefined>(1);
  defaultstock = input<string | undefined>('MSFT');
  defaultside = input<'BUY' | 'SELL' | undefined | null>('BUY');

  submit = output<OrderRequest>();

  orderForm: FormGroup = new FormGroup({
    portfolio: new FormControl('', Validators.required),
    stock: new FormControl('', Validators.required),
    tradeType: new FormControl('MARKET', Validators.required),
    side: new FormControl('', Validators.required),
    price: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[0-9]+(\.[0-9]{1,2})?$/),
    ]),
    quantity: new FormControl('', [
      Validators.required,
      Validators.pattern(/^[0-9]+$/),
    ]),
  });
}
