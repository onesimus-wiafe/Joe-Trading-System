import * as v from 'valibot';

export const ErrorSchema = v.object({
  title: v.string(),
  status: v.number(),
  detail: v.string(),
  description: v.string(),
  instance: v.string(),
});
