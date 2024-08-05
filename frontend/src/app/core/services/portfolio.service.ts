import { Injectable, signal } from '@angular/core';
import { Portfolio, PortfolioState } from '../../shared/models/portfolio.model';

@Injectable({
  providedIn: 'root',
})
export class PortfolioService {
  constructor() {}

  private portfolios = signal<Portfolio[]>([
    {
      id: 1,
      portfolioName: 'Portfolio 1',
      state: PortfolioState.Active,
      description: 'This is the first portfolio',
      createdDate: new Date(),
      updatedOn: new Date(),
    },
    {
      id: 2,
      portfolioName: 'Portfolio 2',
      state: PortfolioState.Inactive,
      description: 'This is the second portfolio',
      createdDate: new Date(),
      updatedOn: new Date(),
    },
  ]);

  getPortfolios() {
    return this.portfolios();
  }

  getPortfolio(id: number) {
    return this.portfolios().find((p) => p.id === id);
  }

  addPortfolio(portfolio: Portfolio) {
    this.portfolios.set([...this.portfolios(), portfolio]);
  }

  updatePortfolio(portfolio: Portfolio) {
    this.portfolios.set(
      this.portfolios().map((p) => (p.id === portfolio.id ? portfolio : p))
    );
  }

  deletePortfolio(id: number) {
    this.portfolios.set(this.portfolios().filter((p) => p.id !== id));
  }

  // getPortfolioValue(id: number) {
  //   const portfolio = this.getPortfolio(id);
  //   if (!portfolio) {
  //     return 0;
  //   }
  //   return portfolio.stocks.reduce(
  //     (total, stock) => total + stock.price * stock.quantity,
  //     0
  //   );
  // }
}
