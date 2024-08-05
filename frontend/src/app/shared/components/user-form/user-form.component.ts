import { Component, computed, effect, input, output } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import * as v from 'valibot';
import { User, UserCreate, UserCreateSchema } from '../../models/user.model';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css',
})
export class UserFormComponent {
  userForm = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl(''),
  });

  defaultUser = input<User | null>(null);
  isEdit = computed<boolean>(() => !!this.defaultUser());

  submit = output<UserCreate & { id?: number }>();

  constructor() {
    effect(() => {
      this.userForm = new FormGroup({
        name: new FormControl(
          this.defaultUser()?.name || '',
          Validators.required
        ),
        email: new FormControl(this.defaultUser()?.email || '', [
          Validators.required,
          Validators.email,
        ]),
        password: new FormControl('', this.validatePassword),
      });
    });
  }

  validatePassword = (control: AbstractControl) => {
    if (this.isEdit()) {
      if (control.value) {
        return Validators.minLength(6)(control);
      }
    } else {
      const validators = [Validators.required, Validators.minLength(6)];
      for (const validator of validators) {
        const result = validator(control);
        if (result) {
          return result;
        }
      }
    }

    return null;
  };

  handleSubmit() {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    const result = v.safeParse(UserCreateSchema, this.userForm.value);
    if (result.success) {
      console.log('emitted', {
        ...result.output,
        id: this.defaultUser()?.id,
      });
    } else {
      console.error(result.issues);
    }
  }
}
