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
import {
  Portfolio,
  PortfolioCreate,
  PortfolioState,
} from '../../shared/models/portfolio.model';
import { RouterLink } from '@angular/router';
import { needConfirmation } from '../../core/services/dialog.service';
import { PortfolioService } from '../../core/services/portfolio.service';
import { PortfolioFormComponent } from '../../shared/components/portfolio-form/portfolio-form.component';

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
    PortfolioFormComponent,
  ],
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css',
  providers: [provideIcons({ heroTrash, heroPencil, heroEye, heroXMark })],
})
export class PortfolioComponent {
  portfolios = computed(() => this.portfolioService.getPortfolios());

  constructor(private portfolioService: PortfolioService) {}

  dialogOpenSignal = signal<boolean>(false);

  selectedPortfolio = signal<Portfolio | null>(null);

  showDialog() {
    this.dialogOpenSignal.set(true);
  }

  closeDialog() {
    this.selectedPortfolio.set(null);
    this.dialogOpenSignal.set(false);
  }

  createPortfolio({ portfolioName, description }: PortfolioCreate) {
    const portfolio: Portfolio = {
      id: this.portfolios().length + 1,
      portfolioName: portfolioName,
      state: PortfolioState.Active,
      description: description,
      createdDate: new Date(),
      updatedOn: new Date(),
    };

    this.portfolioService.addPortfolio(portfolio);
    this.closeDialog();
  }

  handleEdit(portfolio: Portfolio) {
    if (portfolio) {
      this.selectedPortfolio.set(portfolio);
      this.showDialog();
    }
  }

  editPortfolio(portfolioId: number, portfolio: PortfolioCreate) {
    const updatedPortfolio = {
      id: portfolioId,
      portfolioName: portfolio.portfolioName,
      description: portfolio.description,
      state: PortfolioState.Active,
      createdDate: new Date(),
      updatedOn: new Date(),
    };

    this.portfolioService.updatePortfolio(updatedPortfolio);
    this.closeDialog();
  }

  handleSubmit({
    portfolioName,
    description,
    id,
  }: PortfolioCreate & { id?: number }) {
    // BUG: This event is being fired twice
    if (id) {
      this.editPortfolio(id, { portfolioName, description });
    } else {
      this.createPortfolio({ portfolioName, description });
    }
  }

  @needConfirmation({
    title: 'Delete Portfolio',
    message: 'Are you sure you want to delete this portfolio?',
  })
  deletePortfolio(id: number) {
    this.portfolioService.deletePortfolio(id);
  }
}
