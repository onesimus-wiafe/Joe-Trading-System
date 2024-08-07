import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, effect, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import {
  heroEye,
  heroPencil,
  heroTrash,
  heroXMark,
} from '@ng-icons/heroicons/outline';
import { needConfirmation } from '../../core/services/dialog.service';
import { UserService } from '../../core/services/user.service';
import { OrderFormComponent } from '../../shared/components/order-form/order-form.component';
import { PaginationComponent } from '../../shared/components/pagination/pagination.component';
import { UserFormComponent } from '../../shared/components/user-form/user-form.component';
import {
  AccountType,
  User,
  UserCreate,
  UserListResponse,
  UserUpdate,
} from '../../shared/models/user.model';
import { tap } from 'rxjs/operators';
import { ToastService, ToastVariant } from '../../core/services/toast.service';

@Component({
  selector: 'app-users-list',
  standalone: true,
  imports: [
    NgIconComponent,
    PaginationComponent,
    OrderFormComponent,
    CurrencyPipe,
    DatePipe,
    RouterLink,
    UserFormComponent,
  ],
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.css',
  providers: [provideIcons({ heroXMark, heroPencil, heroEye, heroTrash })],
})
export class UsersListComponent {
  prefix = 'users-list';
  dialogOpenSignal = signal<boolean>(false);
  users = signal<UserListResponse>({
    data: [],
    totalPages: 0,
    totalElements: 0,
    currentPage: 1,
  });
  selectedUser = signal<User | null>(null);

  currentPage = signal(1);
  sizePerPage = signal(10);
  accountType = signal<AccountType | null>(null);

  constructor(
    private userService: UserService,
    private toastService: ToastService
  ) {
    effect(() => {
      this.loadUsers();
    });
  }

  showDialog() {
    this.dialogOpenSignal.set(true);
  }

  closeDialog() {
    this.dialogOpenSignal.set(false);
  }

  @needConfirmation({
    title: 'Delete User',
    message:
      "Are you sure you want to delete this user? If this user has any stocks, they'll be sold off and their portfolio will be deleted.",
  })
  deleteUser(id: number) {
    this.userService.deleteUser(id).subscribe({
      next: () => {
        this.loadUsers();
        this.toastService.initiate({
          message: 'User deleted successfully',
          variant: ToastVariant.Success,
        });
      },
      error: (error) => {
        this.toastService.initiate({
          message: error.error.detail,
          variant: ToastVariant.Error,
        });
      },
    });
  }

  createUser(user: UserCreate) {
    this.userService.createUser(user).subscribe({
      next: () => {
        this.loadUsers();
        this.toastService.initiate({
          message: 'User created successfully',
          variant: ToastVariant.Success,
        });
      },
      error: (error) => {
        this.toastService.initiate({
          message: error.error.detail,
          variant: ToastVariant.Error,
        });
      },
    });
  }

  updateUser(id: number, user: UserUpdate) {
    this.userService.updateUser(id, user).subscribe({
      next: () => {
        this.loadUsers();
        this.toastService.initiate({
          message: 'User updated successfully',
          variant: ToastVariant.Success,
        });
      },
      error: (error) => {
        this.toastService.initiate({
          message: error.error.detail,
          variant: ToastVariant.Error,
        });
      },
    });
  }

  getUserById(id: number) {
    return this.userService.getUser(id);
  }

  handleEdit(user: User) {
    this.selectedUser.set(user);
    this.showDialog();
  }

  handleSubmit({ name, email, password, id }: UserCreate & { id?: number }) {
    // BUG: This handler is being called twice
    if (id) {
      this.updateUser(id, { name, email, password });
    } else {
      this.createUser({ name, email, password });
    }
    this.closeDialog();
  }

  changePage($event: number) {
    this.currentPage.set($event);
  }

  changeItemsPerPage($event: number) {
    this.sizePerPage.set($event);
  }

  loadUsers() {
    this.userService
      .getUsers({
        accountType: this.accountType(),
        page: this.currentPage(),
        size: this.sizePerPage(),
      })
      .subscribe((data) => {
        this.users.set(data);
      });
  }

  setAccountType(accountType: 'USER' | 'ADMIN' | 'ALL') {
    switch (accountType) {
      case 'USER':
        this.accountType.set(AccountType.USER);
        break;
      case 'ADMIN':
        this.accountType.set(AccountType.ADMIN);
        break;
      case 'ALL':
        this.accountType.set(null);
    }
  }
}
