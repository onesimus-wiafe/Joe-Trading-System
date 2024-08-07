import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, effect, OnInit, signal } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import * as v from 'valibot';
import { AuthService } from '../../core/services/auth.service';
import { validateField } from '../../core/validators/validate';
import { RegisterSchema } from '../../shared/models/auth.model';
import { UserCreateSchema } from '../../shared/models/user.model';
import { ToastComponent } from '../../shared/components/toast/toast.component';
import { ToastService, ToastVariant } from '../../core/services/toast.service';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, ToastComponent],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
    private toastService: ToastService
  ) {}

  loading = computed(() => this.authService.loading());
  formError = computed(() => this.registerForm.errors);

  registerForm = new FormGroup({
    name: new FormControl('', validateField(UserCreateSchema.entries.name)),
    email: new FormControl('', validateField(UserCreateSchema.entries.email)),
    password: new FormControl(
      '',
      validateField(UserCreateSchema.entries.password)
    ),
    confirm_password: new FormControl('', [
      validateField(
        v.pipe(
          v.string('Password is invalid'),
          v.nonEmpty('Confirm password is required')
        )
      ),
      this.passwordMatchValidator,
    ]),
  });

  passwordMatchValidator(control: AbstractControl) {
    const password = control.parent?.get('password')?.value;
    const confirmPassword = control.value;

    if (password !== confirmPassword) {
      return { mismatch: 'Passwords do not match' };
    }

    return null;
  }

  register() {
    const result = this.registerForm.valid;
    if (result) {
      const data = v.safeParse(RegisterSchema, this.registerForm.value);
      if (data.success) {
        this.authService.register(data.output).subscribe({
          next: (response) => {
            this.toastService.initiate({
              message: "You've successfully registered!",
              variant: ToastVariant.Success,
            });
            setTimeout(() => {
              this.router.navigate(['/login']);
            }, 2000);
          },
          error: (error: HttpErrorResponse) => {
            this.toastService.initiate({
              message: error.error.detail,
              variant: ToastVariant.Error,
            });
          },
        });
      } else {
        console.error(data.issues);
      }
    }
  }
}
