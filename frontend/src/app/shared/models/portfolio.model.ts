import * as v from 'valibot';

export enum PortfolioState {
  Active = 'ACTIVE',
  Inactive = 'INACTIVE',
  Default = 'DEFAULT',
}

export const PortfolioSchema = v.object({
  id: v.number(),
  portfolioName: v.pipe(v.string(), v.minLength(3, 'Name must be at least 3 characters')),
  description: v.string(),
  state: v.enum(PortfolioState),
  createdDate: v.date(),
  updatedOn: v.date(),
});

export type Portfolio = v.InferOutput<typeof PortfolioSchema>;

export const PortfolioCreateSchema = v.object({
  portfolioName: v.pipe(v.string(), v.minLength(3, 'Name must be at least 3 characters')),
  description: v.string(),
});

export type PortfolioCreate = v.InferOutput<typeof PortfolioCreateSchema>;
