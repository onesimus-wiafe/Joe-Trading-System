import { CurrencyPipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'shortCurrency',
  standalone: true,
})
export class ShortCurrencyPipe extends CurrencyPipe implements PipeTransform {
  override transform(
    value: any,
    currencyCode: string = 'USD',
    display: string | boolean = 'symbol',
    digitsInfo?: string,
    locale?: string
  ): any {
    if (value == null) return null;

    let shortenedValue = value;
    let suffix = '';

    if (value >= 1_000_000_000) {
      shortenedValue = value / 1_000_000_000;
      suffix = 'B';
    } else if (value >= 1_000_000) {
      shortenedValue = value / 1_000_000;
      suffix = 'M';
    } else if (value >= 1_000) {
      shortenedValue = value / 1_000;
      suffix = 'K';
    }

    const formattedValue = super.transform(
      shortenedValue,
      currencyCode,
      display,
      digitsInfo,
      locale
    );
    return `${formattedValue} ${suffix}`.trim();
  }
}
