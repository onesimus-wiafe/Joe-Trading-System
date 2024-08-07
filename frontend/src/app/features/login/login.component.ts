import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, effect } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import * as v from 'valibot';
import { AuthService } from '../../core/services/auth.service';
import { validateField } from '../../core/validators/validate';
import { LoginSchema } from '../../shared/models/auth.model';
import { ToastService, ToastVariant } from '../../core/services/toast.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  loginForm = new FormGroup({
    email: new FormControl('', validateField(LoginSchema.entries.email)),
    password: new FormControl('', validateField(LoginSchema.entries.password)),
  });
  loading = computed(() => this.authService.loading());

  constructor(
    private authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  login() {
    const result = v.safeParse(LoginSchema, this.loginForm.value);
    if (result.success) {
      this.authService.login(result.output).subscribe({
        next: (response) => {
          this.toastService.initiate({
            message: 'Logged in successfully',
            variant: ToastVariant.Success,
          });
          this.router.navigate(['/dashboard']);
        },
        error: (error: HttpErrorResponse) => {
          this.toastService.initiate({
            message: error.error.detail,
            variant: ToastVariant.Error,
          });
        },
      });
    } else {
      for (const error of result.issues) {
        for (const path of error.path ?? []) {
          this.loginForm.controls.email.setErrors({ email: true });
        }
      }
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
