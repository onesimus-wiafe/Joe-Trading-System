import { Component, computed, effect, signal } from '@angular/core';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroEye, heroXMark } from '@ng-icons/heroicons/outline';
import { ShortCurrencyPipe } from '../../../core/pipes/short-currency.pipe';
import { needConfirmation } from '../../../core/services/dialog.service';
import { UserFormComponent } from '../../../shared/components/user-form/user-form.component';
import {
  AccountType,
  User,
  UserCreate,
} from '../../../shared/models/user.model';
import { PaginationComponent } from '../../../shared/components/pagination/pagination.component';
import { CurrencyPipe, DatePipe, PercentPipe } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PortfolioService } from '../../../core/services/portfolio.service';
import { UserService } from '../../../core/services/user.service';
import { Stock } from '../../../shared/models/stock.model';
import { PortfolioListResponse } from '../../../shared/models/portfolio.model';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [
    UserFormComponent,
    NgIconComponent,
    ShortCurrencyPipe,
    PaginationComponent,
    CurrencyPipe,
    DatePipe,
    RouterLink,
    PercentPipe,
  ],
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.css',
  providers: [provideIcons({ heroXMark, heroEye })],
})
export class UserDetailComponent {
  prefix = 'users-list';
  portfolios = signal<PortfolioListResponse>({
    data: [],
    totalElements: 0,
    totalPages: 0,
    currentPage: 0,
  });
  dialogOpenSignal = signal<boolean>(false);

  constructor(
    private portfolioService: PortfolioService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute
  ) {
    effect(() => {
      this.loadUser();
    });
  }

  user = signal<User>({
    id: 0,
    name: '',
    email: '',
    pendingDelete: false,
    accountType: AccountType.USER,
    createdAt: new Date(),
    updatedAt: new Date(),
  });

  loadUser() {
    const userId = this.activatedRoute.snapshot.params['userId'];
    this.userService.getUser(userId).subscribe((user) => {
      this.user.set(user);
    });
  }

  stocks: (Stock & { quantity: number })[] = [
    {
      name: 'Amazon',
      price: 161.46,
      symbol: 'AMZN',
      change: -0.1228,
      pic: 'assets/amazon.svg',
      quantity: 10,
    },
    {
      name: 'Apple',
      price: 222.92,
      symbol: 'AAPL',
      change: 0.0209,
      pic: 'assets/apple.svg',
      quantity: 20,
    },
    {
      name: 'Google',
      price: 166.63,
      symbol: 'GOOGL',
      change: -0.0337,
      pic: 'assets/google.svg',
      quantity: 15,
    },
    {
      name: 'IBM',
      price: 186.32,
      symbol: 'IBM',
      change: -0.0176,
      pic: 'assets/ibm.svg',
      quantity: 5,
    },
    {
      name: 'Microsoft',
      price: 405.76,
      symbol: 'MSFT',
      change: -0.0272,
      pic: 'assets/microsoft.svg',
      quantity: 12,
    },
    {
      name: 'Netflix',
      price: 613.28,
      symbol: 'NFLX',
      change: -0.0185,
      pic: 'assets/netflix.svg',
      quantity: 8,
    },
    {
      name: 'Oracle',
      price: 132.59,
      symbol: 'ORCL',
      change: -0.0358,
      pic: 'assets/oracle.svg',
      quantity: 7,
    },
    {
      name: 'Tesla',
      price: 211.26,
      symbol: 'TSLA',
      change: -0.0258,
      pic: 'assets/tesla.svg',
      quantity: 3,
    },
  ];

  showDialog() {
    this.dialogOpenSignal.set(true);
  }

  closeDialog() {
    this.dialogOpenSignal.set(false);
  }

  handleSubmit({
    name,
    email,
    password,
    accountType,
    id,
  }: UserCreate & { id?: number }) {
    // BUG: This handler is being called twice
    if (id) {
      console.log('Editing', { name, email, password, id });
      this.updateUser(id, { name, email, password, accountType });
    }

    this.closeDialog();
  }

  updateUser(id: number, user: UserCreate) {}

  @needConfirmation({
    title: 'Delete User',
    message:
      "Are you sure you want to delete this user? If this user has any stocks, they'll be sold off and their portfolio will be deleted.",
  })
  deleteUser() {
    console.log('Deleting', this.user());
  }
}
