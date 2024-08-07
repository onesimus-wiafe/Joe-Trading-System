import { Component, effect, input, OnInit, signal } from '@angular/core';
import { NgIcon, provideIcons } from '@ng-icons/core';
import { heroXMark } from '@ng-icons/heroicons/outline';
import {
  ToastService,
  ToastVariant,
} from '../../../core/services/toast.service';
import {
  animate,
  state,
  style,
  transition,
  trigger,
} from '@angular/animations';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [NgIcon],
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.css',
  providers: [provideIcons({ heroXMark })],
  animations: [
    trigger('openClose', [
      state('open', style({ transform: 'scale(1)', opacity: 1 })),
      state('closed', style({ transform: 'scale(0)', opacity: 1 })),
      transition('open <=> closed', [animate('0.2s ease-in-out')]),
    ]),
  ],
})
export class ToastComponent implements OnInit {
  visible = signal<boolean>(false);

  variant = signal<ToastVariant>(ToastVariant.Success);
  message = signal<string>('');

  constructor(public toastService: ToastService) {}

  ngOnInit(): void {
    this.toastService.open.subscribe((data) => {
      if (data) {
        this.message.set(data.message);
        this.variant.set(data.variant);
        this.visible.set(true);
        setTimeout(() => {
          this.visible.set(false);
        }, 5000);
      } else {
        this.visible.set(false);
      }
    });
  }

  getAlertClass(): string {
    return this.variant() === ToastVariant.Success
      ? 'alert alert-success'
      : 'alert alert-error';
  }
}
