import { Component, computed, ViewContainerRef } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import * as v from 'valibot';
import { AuthService } from '../../core/services/auth.service';
import { LoginSchema } from '../../shared/models/auth.model';
import { needConfirmation } from '../../core/services/dialog.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });

  constructor(private authService: AuthService, private router: Router, private viewContainerRef: ViewContainerRef) {}

  loading = computed(() => this.authService.loading());

  login() {
    const result = v.safeParse(LoginSchema, this.loginForm.value);
    if (result.success) {
      this.authService.login(result.output).subscribe({
        next: (response) => {
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {},
      });
    } else {
      console.error(result.issues);
    }
  }

  getErrorMessage(control: FormControl) {
    if (control.hasError('required')) {
      return 'You must enter a value';
    }
    if (control.hasError('email')) {
      return 'Not a valid email';
    }

    return '';
  }
}
