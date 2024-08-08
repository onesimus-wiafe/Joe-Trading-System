import {HttpClient} from '@angular/common/http';
import {computed, Injectable, signal} from '@angular/core';
import {shareReplay, tap} from 'rxjs/operators';
import {AuthResponse, Login, Register} from '../../shared/models/auth.model';
import {add} from 'date-fns';
import {User} from '../../shared/models/user.model';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {
    const auth = localStorage.getItem('auth');
    if (auth) {
      this.authInfo.set(JSON.parse(auth));

      this.timeoutId.set(
        setTimeout(() => {
          this.logout();
        }, this.expiration())
      );
    }
  }

  authInfo = signal<AuthResponse | null>(null);
  role = computed(() => this.authInfo()?.user.accountType);

  loading = signal<boolean>(false);

  timeoutId = signal<ReturnType<typeof setTimeout> | null>(null);

  private expiration() {
    const expiresAt = JSON.parse(localStorage.getItem('expires_at')!) as number;
    return expiresAt;
  }

  login(data: Login) {
    this.loading.set(true);
    return this.http.post<AuthResponse>('/auth/login', data).pipe(
      tap({
        next: (auth) => {
          this.setSession(auth);
          this.loading.set(false);
          this.timeoutId.set(
            setTimeout(() => {
              this.logout();
            }, auth.expiresIn)
          );
        },
        error: () => {
          this.loading.set(false);
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
    localStorage.removeItem('expires_at');
    this.authInfo.set(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn() {
    return new Date().getTime() < this.expiration();
  }

  isLoggedOut() {
    return !this.isLoggedIn();
  }

  register(data: Register) {
    this.loading.set(true);
    return this.http.post<User>('/auth/register', data).pipe(
      tap({
        next: () => {
          this.loading.set(false);
        },
        error: () => {
          this.loading.set(false);
        },
      }),
      shareReplay()
    );
  }
}
