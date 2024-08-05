import * as v from "valibot";

export enum Symbol {
  MSFT = 'MSFT',
  NFLX = 'NFLX',
  GOOGL = 'GOOGL',
  AAPL = 'AAPL',
  TSLA = 'TSLA',
  IBM = 'IBM',
  ORCL = 'ORCL',
  AMZN = 'AMZN',
}

export const Stock = v.object({
  name: v.string(),
  symbol: v.enum(Symbol),
  price: v.number(),
  change: v.number(),
  pic: v.string(),
});

export type Stock = {
  name: string;
  symbol: string;
  price: number;
  change: number;
  pic: string;
};

export const OrderRequestSchema = v.object({
  portfolio: v.string(),
  stock: v.string(),
  tradeType: v.string(),
  side: v.string(),
  price: v.pipe(v.string(), v.transform(parseFloat)),
  quantity: v.pipe(v.union([v.number(), v.string()]), v.transform(Number.parseInt)),
});

export type OrderRequest = v.InferOutput<typeof OrderRequestSchema>;
