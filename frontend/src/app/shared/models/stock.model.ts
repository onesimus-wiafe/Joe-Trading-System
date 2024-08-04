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
