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
  name: v.pipe(
    v.string('Invalid name'),
    v.nonEmpty('Name is required'),
    v.minLength(3, 'Name must be at least 3 characters')
  ),
  email: v.pipe(
    v.string('Invalid email'),
    v.nonEmpty('Email is required'),
    v.email('Invalid email')
  ),
  password: v.pipe(
    v.string('Invalid password'),
    v.nonEmpty('Password is required'),
    v.minLength(6, 'Password must be at least 6 characters')
  ),
  accountType: v.optional(v.enum(AccountType)),
});

export type UserCreate = v.InferOutput<typeof UserCreateSchema>;

export const UserUpdateSchema = v.object({
  name: v.string(),
  email: v.pipe(
    v.string('Invalid email'),
    v.nonEmpty('Email is required'),
    v.email('Invalid email')
  ),
  password: v.optional(
    v.union([
      v.literal(''),
      v.pipe(
        v.string('Invalid password'),
        v.minLength(6, 'Password must be at least 6 characters')
      ),
    ])
  ),
  accountType: v.optional(v.enum(AccountType)),
});

export type UserUpdate = v.InferOutput<typeof UserUpdateSchema>;

export const UserListResponseSchema = v.object({
  data: v.array(UserSchema),
  totalPages: v.number(),
  totalElements: v.number(),
  currentPage: v.number(),
});

export type UserListResponse = v.InferOutput<typeof UserListResponseSchema>;
