import * as v from 'valibot';

export enum OrderStatus {
  Pending = 'PENDING',
  Completed = 'COMPLETED',
  Cancelled = 'CANCELLED',
  Failed = 'FAILED',
}

export const TradeSchema = v.object({
  id: v.number(),
  symbol: v.string(),
  name: v.string(),
  price: v.number(),
  quantity: v.number(),
  side: v.string(),
  splitted: v.boolean(),
  splitAmounts: v.array(v.number()),
  status: v.enum(OrderStatus),
  date: v.date(),
  completedDate: v.optional(v.date()),
});

export type Trade = v.InferOutput<typeof TradeSchema>;
