import * as v from 'valibot';
import { AccountType, UserSchema } from './user.model';

export const LoginSchema = v.object({
  email: v.pipe(v.string(), v.email()),
  password: v.string(),
});

export type Login = v.InferInput<typeof LoginSchema>;

export const RegisterSchema = v.object({
  name: v.string(),
  email: v.pipe(v.string(), v.email()),
  password: v.string(),
});

export type Register = v.InferInput<typeof RegisterSchema>;

export const CreateUserSchema = v.object({
  name: v.pipe(v.string(), v.minLength(3, 'Name must be at least 3 characters')),
  email: v.pipe(v.string(), v.email('Invalid email address')),
  password: v.pipe(v.string(), v.minLength(8, 'Password must be at least 8 characters')),
  accountType: v.enum(AccountType),
});

export type CreateUser = v.InferInput<typeof CreateUserSchema>;

export const AuthResponseSchema = v.object({
  token: v.string(),
  expiresIn: v.number(),
  user: UserSchema,
});

export type AuthResponse = v.InferInput<typeof AuthResponseSchema>;
