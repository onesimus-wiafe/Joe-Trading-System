import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-order-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './order-form.component.html',
  styleUrl: './order-form.component.css',
})
export class OrderFormComponent {
  handleSubmit() {
    throw new Error('Method not implemented.');
  }
  orderForm = new FormGroup({
    portfolio: new FormControl('', Validators.required),
    stock: new FormControl('MSFT', Validators.required),
    tradeType: new FormControl('MARKET', Validators.required),
    side: new FormControl('BUY', Validators.required),
    price: new FormControl('', Validators.required),
    quantity: new FormControl('', Validators.required),
  });
}
