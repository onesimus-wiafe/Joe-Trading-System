import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

export enum ToastVariant {
  Success = 'success',
  Error = 'error',
}

export interface ToastData {
  message: string;
  variant: ToastVariant;
}

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  data: ToastData | null = null;
  public open = new BehaviorSubject<ToastData | null>(null);

  initiate(data: ToastData) {
    this.data = data;
    this.open.next(data);
  }

  hide() {
    this.open.next(null);
  }
}
