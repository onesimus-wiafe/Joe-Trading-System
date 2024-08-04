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

export type User = v.InferInput<typeof UserSchema>;
