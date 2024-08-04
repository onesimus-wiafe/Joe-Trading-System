import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroEye,
  heroPencil,
  heroTrash,
  heroXMark,
} from '@ng-icons/heroicons/outline';
import { PaginationComponent } from '../../shared/components/pagination/pagination.component';
import { Portfolio, PortfolioState } from '../../shared/models/portfolio.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-portfolio',
  standalone: true,
  imports: [
    CurrencyPipe,
    NgIconComponent,
    PaginationComponent,
    ReactiveFormsModule,
    DatePipe,
    RouterLink,
  ],
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css',
  providers: [provideIcons({ heroTrash, heroPencil, heroEye, heroXMark })],
})
export class PortfolioComponent {
  portfolios: Portfolio[] = [
    {
      id: 1,
      portfolioName: 'Portfolio 1',
      state: PortfolioState.Active,
      description: 'This is the first portfolio',
      createdDate: new Date(),
      updatedOn: new Date(),
    },
    {
      id: 2,
      portfolioName: 'Portfolio 2',
      state: PortfolioState.Inactive,
      description: 'This is the second portfolio',
      createdDate: new Date(),
      updatedOn: new Date(),
    },
  ];

  portfolioForm = new FormGroup({
    portfolioName: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
    ]),
    description: new FormControl(''),
  });

  dialogOpenSignal = signal<boolean>(false);

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

    // this.portfolioForm.reset();
    // this.closeDialog();
  }
}
