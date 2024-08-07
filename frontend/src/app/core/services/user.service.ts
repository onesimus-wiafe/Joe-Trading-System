import { Injectable, signal } from '@angular/core';
import {
  AccountType,
  User,
  UserCreate,
  UserListResponse,
  UserUpdate,
} from '../../shared/models/user.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { shareReplay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  getUsers(
    {
      size,
      page,
      accountType,
    }: { size: number; page: number; accountType?: AccountType | null } = {
      size: 10,
      page: 1,
    }
  ) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('accountType', accountType?.toString() || '');

    return this.httpClient
      .get<UserListResponse>('/users', { params });
  }

  deleteUser(id: number) {
    return this.httpClient.delete(`/users/${id}`);
  }

  createUser(user: UserCreate) {
    return this.httpClient.post<User>('/users', user);
  }

  updateUser(id: number, user: UserUpdate) {
    return this.httpClient.put<User>(`/users/${id}`, user);
  }

  getUser(id: number) {
    return this.httpClient.get<User>(`/users/${id}`).pipe(shareReplay());
  }
}
