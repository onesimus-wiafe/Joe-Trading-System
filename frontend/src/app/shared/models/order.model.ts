import * as v from 'valibot';

export enum OrderStatus {
  OPEN = 'OPEN',
  PARTIALLY_FILLED = 'PARTIALLY_FILLED',
  CANCELLED = 'CANCELLED',
  CLOSED = 'CLOSED',
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
