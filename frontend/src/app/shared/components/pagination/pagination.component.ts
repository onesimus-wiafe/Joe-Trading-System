import { Component, computed, effect, input, output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [],
  templateUrl: './pagination.component.html',
  styleUrl: './pagination.component.css',
})
export class PaginationComponent {
  prefix = 'pagination';

  totalItems = input<number>(0);
  itemsPerPage = input<number>(10);
  currentPage = input<number>(1);

  changePage = output<number>();
  changeItemsPerPage = output<number>();

  totalPages= computed(() => {
    return Math.ceil(this.totalItems() / this.itemsPerPage());
  });

  pages = computed(() => {
    const totalPages = this.totalPages;
    const pagesArray: { page: string; enabled: boolean }[] = [];
    const maxPages = 4;

    if (totalPages() <= maxPages) {
      for (let i = 1; i <= totalPages(); i++) {
        pagesArray.push({ page: i.toString(), enabled: true });
      }
    } else {
      const beforeCurrentPage = Math.max(this.currentPage() - 1, 1);
      const afterCurrentPage = Math.min(this.currentPage() + 1, totalPages());

      if (beforeCurrentPage > 2) {
        pagesArray.push({ page: '1', enabled: true });
        pagesArray.push({ page: '...', enabled: false });
      }

      for (
        let i = Math.max(beforeCurrentPage, 1);
        i <= Math.min(afterCurrentPage, totalPages());
        i++
      ) {
        pagesArray.push({ page: i.toString(), enabled: true });
      }

      if (afterCurrentPage < totalPages() - 1) {
        pagesArray.push({ page: '...', enabled: false });
        pagesArray.push({ page: totalPages().toString(), enabled: true });
      }
    }

    return pagesArray;
  });

  get isFirstPage() {
    return this.currentPage() === 1;
  }

  get isLastPage() {
    return this.currentPage() === this.totalPages() || this.totalPages() === 0;
  }

  get isPrevDisabled() {
    return this.isFirstPage;
  }

  get isNextDisabled() {
    return this.isLastPage;
  }

  goToPage(page: string | number) {
    if (page === '...') {
      return;
    }

    this.changePage.emit(+page);
  }

  changePageSize(e: Event) {
    const target = e.target as HTMLSelectElement;
    this.changeItemsPerPage.emit(+target.value);
  }
}
