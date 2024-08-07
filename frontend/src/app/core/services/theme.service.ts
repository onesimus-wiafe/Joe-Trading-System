import { Injectable, computed, effect, output, signal } from '@angular/core';

export const storageKey = 'theme';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  #html = document.querySelector('html');

  themeSignal = signal<'light' | 'dark'>('light');

  theme = computed(() => this.themeSignal());

  constructor() {
    this.initializeThemeFromPreferences();

    effect(() => {
      this.updateRenderedTheme();
    });
  }

  toggleTheme(): void {
    this.themeSignal.update((prev) =>
      this.isDarkThemeActive() ? 'light' : 'dark'
    );
  }

  private initializeThemeFromPreferences(): void {
    let systemIsDarkTheme = window.matchMedia(
      '(prefers-color-scheme: dark)'
    ).matches;
    const theme = localStorage.getItem(storageKey) as 'light' | 'dark';
    !!theme
      ? this.themeSignal.set(theme)
      : this.themeSignal.set(systemIsDarkTheme ? 'dark' : 'light');

    this.#html?.setAttribute('data-theme', theme);
    this.#html?.setAttribute('class', theme);
  }

  isDarkThemeActive(): boolean {
    return this.themeSignal() === 'dark' ? true : false;
  }

  private updateRenderedTheme(): void {
    this.#html?.setAttribute('data-theme', this.themeSignal());
    this.#html?.setAttribute('class', this.themeSignal());

    localStorage.setItem(storageKey, this.themeSignal());
  }
}

export const initializeTheme = (themeService: ThemeService) => () => {};
