import * as v from 'valibot';

export enum PortfolioState {
  Active = 'ACTIVE',
  Inactive = 'INACTIVE',
  Default = 'DEFAULT',
}

export const PortfolioSchema = v.object({
  id: v.number(),
  name: v.pipe(
    v.string(),
    v.minLength(3, 'Name must be at least 3 characters')
  ),
  description: v.string(),
  value: v.number(),
  state: v.enum(PortfolioState),
  createdAt: v.date(),
  updatedOn: v.date(),
});

export type Portfolio = v.InferOutput<typeof PortfolioSchema>;

export const PortfolioCreateSchema = v.object({
  name: v.pipe(
    v.string(),
    v.minLength(3, 'Name must be at least 3 characters')
  ),
  description: v.string(),
});

export type PortfolioCreate = v.InferOutput<typeof PortfolioCreateSchema>;

export const PortfolioListResponseSchema = v.object({
  data: v.array(PortfolioSchema),
  totalPages: v.number(),
  totalElements: v.number(),
  currentPage: v.number(),
});

export type PortfolioListResponse = v.InferOutput<typeof PortfolioListResponseSchema>
