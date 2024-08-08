import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  Portfolio,
  PortfolioCreate,
  PortfolioListResponse,
} from '../../shared/models/portfolio.model';

type PortfolioFilters = {
  name?: string;
  description?: string;
  page?: number;
  size?: number;
  sortBy?: 'id' | 'name' | 'description' | 'createdAt' | 'updatedAt';
};

@Injectable({
  providedIn: 'root',
})
export class PortfolioService {
  constructor(private httpClient: HttpClient) {}

  getPortfolios(
    filters: PortfolioFilters = {
      sortBy: 'id',
      size: 10,
      page: 1,
    }
  ) {
    const params = new HttpParams();
    if (filters.name) {
      params.set('name', filters.name);
    }
    if (filters.description) {
      params.set('description', filters.description);
    }
    if (filters.page) {
      params.set('page', filters.page.toString());
    }
    if (filters.size) {
      params.set('size', filters.size.toString());
    }
    if (filters.sortBy) {
      params.set('sortBy', filters.sortBy);
    }

    return this.httpClient.get<PortfolioListResponse>('/portfolios', {
      params,
    });
  }

  getPortfolio(id: number) {
    return this.httpClient.get<Portfolio>(`/portfolios/${id}`);
  }

  addPortfolio(portfolio: PortfolioCreate) {
    return this.httpClient.post<Portfolio>('/portfolios', portfolio);
  }

  updatePortfolio(portfolio: Portfolio) {
    return this.httpClient.put<Portfolio>(
      `/portfolios/${portfolio.id}`,
      portfolio
    );
  }

  deletePortfolio(id: number) {
    return this.httpClient.delete(`/portfolios/${id}`);
  }
}
