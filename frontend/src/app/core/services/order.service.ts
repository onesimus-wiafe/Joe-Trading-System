import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {OrderRequest, OrderResponse} from '../../shared/models/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  constructor(private httpClient: HttpClient) { }

  createOrder(data: OrderRequest) {
    return this.httpClient.post<OrderResponse>("/orders", data);
  }

  cancelOrder
  () {}
}
