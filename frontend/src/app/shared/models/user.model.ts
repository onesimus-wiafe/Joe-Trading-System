import * as v from 'valibot';

export enum AccountType {
  ADMIN = 'ADMIN',
  USER = 'USER',
}

export const UserSchema = v.object({
  id: v.number(),
  name: v.string(),
  email: v.pipe(v.string(), v.email()),
  accountType: v.enum(AccountType),
  pendingDelete: v.boolean(),
  createdAt: v.date(),
  updatedAt: v.date(),
});

export type User = v.InferOutput<typeof UserSchema>;

export const UserCreateSchema = v.object({
  name: v.string(),
  email: v.pipe(v.string(), v.email()),
  password: v.pipe(v.string(), v.minLength(6)),
});

export type UserCreate = v.InferOutput<typeof UserCreateSchema>;

export const UserUpdateSchema = v.object({
  name: v.string(),
  email: v.pipe(v.string(), v.email()),
  password: v.optional(v.pipe(v.string(), v.minLength(6))),
});

export type UserUpdate = v.InferOutput<typeof UserUpdateSchema>;
