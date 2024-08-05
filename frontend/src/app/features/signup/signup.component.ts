import { Component, computed, effect } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
  AbstractControl,
} from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { RegisterSchema } from '../../shared/models/auth.model';
import * as v from 'valibot';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  constructor(private authService: AuthService, private router: Router) {}

  loading = computed(() => this.authService.loading());

  registerForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      this.passwordMatchValidator('password'),
    ]),
    confirm_password: new FormControl('', [
      Validators.required,
      this.passwordMatchValidator('confirm_password'),
    ]),
  });

  passwordMatchValidator(field: 'password' | 'confirm_password') {
    return (control: AbstractControl) => {
      const password = control.parent?.get('password')?.value;
      const confirmPassword = control.parent?.get('confirm_password')?.value;

      const errors = control.parent?.get('confirm_password')?.errors;

      if (password !== confirmPassword) {
        if (field === 'password') {
          control.parent
            ?.get('confirm_password')
            ?.setErrors({ ...errors, mismatch: true });
          return null;
        }
        return { mismatch: true };
      }

      if (errors) {
        delete errors?.['mismatch'];
        control.parent
          ?.get('confirm_password')
          ?.setErrors(Object.keys(errors).length ? errors : null);
      }

      return null;
    };
  }

  register() {
    const result = this.registerForm.valid;
    if (result) {
      const data = v.safeParse(RegisterSchema, this.registerForm.value);
      if (data.success) {
        this.authService.register(data.output).subscribe({
          next: (response) => {
            this.router.navigate(['/login']);
          },
          error: (error: HttpErrorResponse) => {
            this.registerForm.setErrors({ server: error.error.message });
          },
        });
      } else {
        console.error(data.issues);
      }
    }
  }
}
