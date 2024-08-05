import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroEye, heroPencil, heroTrash, heroXMark } from '@ng-icons/heroicons/outline';
import { UserService } from '../../core/services/user.service';
import { OrderFormComponent } from '../../shared/components/order-form/order-form.component';
import { PaginationComponent } from '../../shared/components/pagination/pagination.component';
import { User, UserCreate, UserUpdate } from '../../shared/models/user.model';
import { needConfirmation } from '../../core/services/dialog.service';
import { UserFormComponent } from "../../shared/components/user-form/user-form.component";

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
  constructor(private userService: UserService) {}

  dialogOpenSignal = signal<boolean>(false);
  prefix = 'users-list';

  showDialog() {
    this.dialogOpenSignal.set(true);
  }

  closeDialog() {
    this.dialogOpenSignal.set(false);
  }

  users = computed(() => this.userService.getUsers());
  selectedUser = signal<User | null>(null);

  @needConfirmation({
    title: 'Delete User',
    message:
      "Are you sure you want to delete this user? If this user has any stocks, they'll be sold off and their portfolio will be deleted.",
  })
  deleteUser(id: number) {
    this.userService.deleteUser(id);
  }

  createUser(user: UserCreate) {
    this.userService.createUser(user);
  }

  updateUser(id: number, user: UserUpdate) {
    this.userService.updateUser(id, user);
  }

  getUserById(id: number) {
    return this.userService.getUserById(id);
  }

  handleEdit(user: User) {
    this.selectedUser.set(user);
    this.showDialog();
  }

  handleSubmit({
    name,
    email,
    password,
    id,
  }: UserCreate & { id?: number }) {
    // BUG: This handler is being called twice
    if (id) {
      console.log('Editing', { name, email, password, id });
      this.updateUser(id, { name, email, password });
    } else {
      console.log('Creating', { name, email, password });
      this.createUser({ name, email, password });
    }
    this.closeDialog();
  }
}
