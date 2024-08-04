import { Component, computed } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  FormGroup,
  FormControl,
  Validators,
  ReactiveFormsModule,
  AbstractControl,
} from '@angular/forms';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.css',
})
export class SignupComponent {
  loginForm = new FormGroup({
    full_name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    username: new FormControl('', [Validators.required]),
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

  handleSubmit() {
    console.log('handleSubmit() called');
    console.log(this.loginForm.value);
  }
}
