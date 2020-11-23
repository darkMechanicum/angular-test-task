export interface CurrencyRate {
  rate: number;
  date: Date;
  code: string;
}

export interface CurrencyRatePercent extends CurrencyRate {
  ratePercent: number;
}

export interface CurrencyRatePage {
  rates: CurrencyRate[];
}
