import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
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
import { needConfirmation } from '../../core/services/dialog.service';
import { PortfolioService } from '../../core/services/portfolio.service';

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
  portfolios = computed(() => this.portfolioService.getPortfolios());

  portfolioForm = new FormGroup({
    portfolioName: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
    ]),
    description: new FormControl(''),
  });

  constructor(private portfolioService: PortfolioService) {}

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

    const portfolio: Portfolio = {
      id: this.portfolios().length + 1,
      portfolioName: this.portfolioForm.get('portfolioName')?.value!,
      state: PortfolioState.Active,
      description: this.portfolioForm.get('description')?.value!,
      createdDate: new Date(),
      updatedOn: new Date(),
    };

    this.portfolioService.addPortfolio(portfolio);
    this.portfolioForm.reset();
    this.closeDialog();
  }

  @needConfirmation({
    title: 'Delete Portfolio',
    message: 'Are you sure you want to delete this portfolio?',
  })
  deletePortfolio(id: number) {
    this.portfolioService.deletePortfolio(id);
  }
}
