import { Component, input, output, signal } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroXMark } from '@ng-icons/heroicons/outline';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [NgIconComponent],
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css',
  providers: [provideIcons({ heroXMark })],
})
export class ConfirmDialogComponent {
  constructor() {}

  title = signal<string>('Confirmation');
  message = signal<string>('Are you sure you want to perform this action?');

  closed = output<boolean>();

  close: (result: boolean) => void = () => {
    this.closed.emit(false);
  };
}
