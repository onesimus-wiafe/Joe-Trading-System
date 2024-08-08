import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, effect, OnInit, signal } from '@angular/core';
import {
  ReactiveFormsModule
} from '@angular/forms';
import { RouterLink } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroEye,
  heroPencil,
  heroTrash,
  heroXMark,
} from '@ng-icons/heroicons/outline';
import { needConfirmation } from '../../core/services/dialog.service';
import { PortfolioService } from '../../core/services/portfolio.service';
import { ToastService, ToastVariant } from '../../core/services/toast.service';
import { PaginationComponent } from '../../shared/components/pagination/pagination.component';
import { PortfolioFormComponent } from '../../shared/components/portfolio-form/portfolio-form.component';
import {
  Portfolio,
  PortfolioCreate,
  PortfolioListResponse
} from '../../shared/models/portfolio.model';

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
export class PortfolioComponent implements OnInit {
  portfolios = signal<PortfolioListResponse>({
    data: [],
    totalElements: 0,
    totalPages: 0,
    currentPage: 0,
  });

  currentPage = signal(1);
  sizePerPage = signal(10);

  constructor(
    private portfolioService: PortfolioService,
    private toastService: ToastService
  ) {
    effect(() => {
      this.loadPortfolios();
    });
  }

  ngOnInit(): void {
    this.portfolioService.getPortfolios().subscribe({
      next: (response) => {
        this.portfolios.set(response);
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  loadPortfolios() {
    this.portfolioService
      .getPortfolios({
        page: this.currentPage(),
        size: this.sizePerPage(),
      })
      .subscribe({
        next: (response) => {
          this.portfolios.set(response);
        },
        error: (error) => {
          this.toastService.initiate({
            message: 'Failed to load portfolios',
            variant: ToastVariant.Error,
          });
        },
      });
  }

  dialogOpenSignal = signal<boolean>(false);

  selectedPortfolio = signal<Portfolio | null>(null);

  showDialog() {
    this.dialogOpenSignal.set(true);
  }

  closeDialog() {
    this.selectedPortfolio.set(null);
    this.dialogOpenSignal.set(false);
  }

  createPortfolio(data: PortfolioCreate) {
    this.portfolioService.addPortfolio(data).subscribe({});
    this.closeDialog();
  }

  handleEdit(portfolio: Portfolio) {
    this.selectedPortfolio.set(portfolio);
    this.showDialog();
  }

  editPortfolio(portfolioId: number, portfolio: PortfolioCreate) {
    // const updatedPortfolio = {
    //   id: portfolioId,
    //   name: portfolio.name,
    //   description: portfolio.description,
    //   state: PortfolioState.Active,
    //   createdDate: new Date(),
    //   updatedOn: new Date(),
    // };
    // this.portfolioService.updatePortfolio(updatedPortfolio);
    // this.closeDialog();
  }

  handleSubmit({ name, description, id }: PortfolioCreate & { id?: number }) {
    // BUG: This event is being fired twice
    if (id) {
      console.log('Editing', { name, description, id });
      this.editPortfolio(id, { name, description });
    } else {
      console.log('Creating', { name, description });
      this.createPortfolio({ name, description });
    }
  }

  @needConfirmation({
    title: 'Delete Portfolio',
    message: 'Are you sure you want to delete this portfolio?',
  })
  deletePortfolio(id: number) {
    this.portfolioService.deletePortfolio(id);
  }

  changeItemsPerPage(size: number) {
    this.sizePerPage.set(size);
  }

  changePage(page: number) {
    this.currentPage.set(page);
  }
}
