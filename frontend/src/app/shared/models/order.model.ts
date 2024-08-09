import * as v from 'valibot';
import { StockSymbol } from './stock.model';

export enum OrderStatus {
  OPEN = 'OPEN',
  PARTIALLY_FILLED = 'PARTIALLY_FILLED',
  CANCELLED = 'CANCELLED',
  CLOSED = 'CLOSED',
}

export enum OrderType {
  MARKET = 'MARKET',
  LIMIT = 'LIMIT',
}

export enum OrderSide {
  BUY = 'BUY',
  SELL = 'SELL',
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

export const OrderRequestSchema = v.object({
  portfolioId: v.pipe(
    v.union([v.number(), v.string()]),
    v.transform(Number.parseInt)
  ),
  ticker: v.enum(StockSymbol),
  quantity: v.pipe(
    v.union([v.number(), v.string()]),
    v.transform(Number.parseInt)
  ),
  orderType: v.enum(OrderType),
  side: v.enum(OrderSide),
  unitPrice: v.pipe(v.string(), v.nonEmpty(), v.transform(parseFloat)),
});

export type OrderRequest = v.InferOutput<typeof OrderRequestSchema>;

export const OrderResponseSchema = v.object({
  ticker: v.string(),
  quantity: v.pipe(v.number(), v.integer()),
  unitPrice: v.number(),
  side: v.string(),
  strategy: v.string(),
  orderType: v.string(),
  message: v.string(),
});

export type OrderResponse = v.InferOutput<typeof OrderResponseSchema>;
