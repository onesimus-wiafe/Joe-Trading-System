import { Component, computed, effect, input, output } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import * as v from 'valibot';
import {
  AccountType,
  User,
  UserCreate,
  UserCreateSchema,
  UserUpdateSchema,
} from '../../models/user.model';
import { validateField } from '../../../core/validators/validate';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css',
})
export class UserFormComponent {
  userForm = new FormGroup({
    name: new FormControl(''),
    email: new FormControl(''),
    password: new FormControl(''),
    accountType: new FormControl(AccountType.USER.toString()),
  });

  defaultUser = input<User | null>(null);
  isEdit = computed<boolean>(() => !!this.defaultUser());

  open = input<boolean>(false);

  formSubmit = output<UserCreate & { id?: number }>();

  constructor() {
    effect(() => {
      this.userForm = new FormGroup({
        name: new FormControl(
          this.defaultUser()?.name || '',
          this.isEdit()
            ? validateField(UserCreateSchema.entries.name)
            : validateField(UserUpdateSchema.entries.name)
        ),
        email: new FormControl(
          this.defaultUser()?.email || '',
          validateField(UserCreateSchema.entries.email)
        ),
        accountType: new FormControl(
          this.defaultUser()?.accountType.toString() ||
            AccountType.USER.toString(),
          this.isEdit()
            ? validateField(UserCreateSchema.entries.accountType)
            : validateField(UserUpdateSchema.entries.accountType)
        ),
        password: new FormControl('', this.validatePassword),
      });
    });

    effect(() => {
      if (!this.open()) {
        this.userForm.reset();
      }
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

    let schema: v.ObjectSchema<any, any>;
    if (this.isEdit()) {
      schema = UserUpdateSchema;
    } else {
      schema = UserCreateSchema;
    }

    const result = v.safeParse(schema, this.userForm.value);
    if (result.success) {
      // this.formSubmit.emit({ ...result.output, id: this.defaultUser()?.id });
    } else {
      console.error(result.issues);
    }
  }
}
