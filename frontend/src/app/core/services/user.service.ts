import { Injectable, signal } from '@angular/core';
import { AccountType, User, UserCreate, UserUpdate } from '../../shared/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor() {}

  private users = signal<User[]>([
    {
      id: 1,
      name: 'Alice',
      email: 'aliceinwonderland@yahoo.com',
      accountType: AccountType.ADMIN,
      pendingDelete: false,
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id: 2,
      name: 'Bob',
      email: 'bob@yahoo.com',
      accountType: AccountType.USER,
      pendingDelete: false,
      createdAt: new Date(),
      updatedAt: new Date(),
    },
  ]);

  getUsers() {
    return this.users();
  }

  deleteUser(id: number) {
    this.users.set(this.users().filter((user) => user.id !== id));
  }

  createUser(user: UserCreate) {
    this.users.set([
      ...this.users(),
      {
        id: this.users().length + 1,
        ...user,
        accountType: AccountType.USER,
        pendingDelete: false,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    ]);
  }

  updateUser(id: number, user: UserUpdate) {
    this.users.set(
      this.users().map((u) => {
        if (u.id === id) {
          return {
            ...u,
            ...user,
            updatedAt: new Date(),
          };
        }
        return u;
      })
    );
  }

  getUserById(id: number) {
    return this.users().find((user) => user.id === id);
  }
}
