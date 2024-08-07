import { AbstractControl } from '@angular/forms';
import { UserCreateSchema } from '../../shared/models/user.model';
import * as v from 'valibot';

UserCreateSchema.entries.email;

export const validateField = (schema: v.BaseSchema<any, any, v.BaseIssue<unknown>>) => {
  return (control: AbstractControl) => {
    const result = v.safeParse(schema, control.value);
    if (result.success) {
      return null;
    } else {
      for (const error of result.issues) {
        return { message: error.message };
      }

      return null;
    }
  };
};

