import { HttpClient } from '@angular/common/http';
import { computed, Injectable, signal } from '@angular/core';
import { shareReplay, tap } from 'rxjs/operators';
import { AuthResponse, Login } from '../../shared/models/auth.model';
import { add } from 'date-fns';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {
    const auth = localStorage.getItem('auth');
    if (auth) {
      this.authInfo.set(JSON.parse(auth));
    }
  }

  authInfo = signal<AuthResponse | null>(null);
  role = computed(() => this.authInfo()?.user.accountType);
  expiration = computed(() => {
    const expiresAt = JSON.parse(localStorage.getItem('expires_at')!) as number;
    return expiresAt;
  });

  login(data: Login) {
    return this.http
      .post<AuthResponse>('http://localhost:3003/api/v1/auth/login', data)
      .pipe(
        tap({
          next: (auth) => {
            this.setSession(auth);
          },
        }),
        shareReplay()
      );
  }

  private setSession(auth: AuthResponse) {
    const expiresAt = add(new Date(), { seconds: auth.expiresIn / 1000 });
    localStorage.setItem('expires_at', JSON.stringify(expiresAt.getTime()));
    localStorage.setItem('auth', JSON.stringify(auth));

    this.authInfo.set(auth);
  }

  logout() {
    localStorage.removeItem('auth');
  }

  public isLoggedIn() {
    return new Date().getTime() < this.expiration();
  }

  isLoggedOut() {
    return !this.isLoggedIn();
  }
}
