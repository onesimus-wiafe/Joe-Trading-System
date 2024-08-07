import * as v from 'valibot';
import { AccountType, UserSchema } from './user.model';

export const LoginSchema = v.object({
  email: v.pipe(v.string("Invalid email"), v.nonEmpty("Email is required"), v.email("Invalid email")),
  password: v.pipe(v.string("Invalid password"), v.nonEmpty("Password is required")),
});

export type Login = v.InferOutput<typeof LoginSchema>;

export const RegisterSchema = v.object({
  name: v.string(),
  email: v.pipe(v.string(), v.email()),
  password: v.string(),
});

export type Register = v.InferOutput<typeof RegisterSchema>;

export const CreateUserSchema = v.object({
  name: v.pipe(v.string(), v.minLength(3, 'Name must be at least 3 characters')),
  email: v.pipe(v.string(), v.email('Invalid email address')),
  password: v.pipe(v.string(), v.minLength(8, 'Password must be at least 8 characters')),
  accountType: v.enum(AccountType),
});

export type CreateUser = v.InferOutput<typeof CreateUserSchema>;

export const AuthResponseSchema = v.object({
  token: v.string(),
  expiresIn: v.number(),
  user: UserSchema,
});

export type AuthResponse = v.InferOutput<typeof AuthResponseSchema>;
