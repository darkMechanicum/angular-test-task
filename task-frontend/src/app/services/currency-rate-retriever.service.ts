import {Injectable} from '@angular/core';
import {Observable, Observer, Subject} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {DataSource} from '@angular/cdk/collections';
import {CurrencyRate, CurrencyRatePage} from '../model/currency.rate';
import {Currency} from '../model/currency';

@Injectable({
  providedIn: 'root'
})
export class CurrencyRateRetrieverService {

  private pCurrentMonth: number;
  private pCurrentYear: number;
  private pCurrentCurrency: string;
  private readonly pCurrencyRatesSubject: Subject<CurrencyRate[]>;

  constructor(
    private httpClient: HttpClient
  ) {
    this.pCurrentMonth = (new Date()).getMonth() + 1;
    this.pCurrentYear = (new Date()).getFullYear();
    this.pCurrentCurrency = 'R01010';
    this.pCurrencyRatesSubject = new Subject<CurrencyRate[]>();
  }

  private refreshCurrenciesRateObservable(): void {
    this.httpClient.get<CurrencyRate[]>(
      `http://127.0.0.1:4200/api/rates/${this.pCurrentCurrency}`,
      {
        params: new HttpParams(),
        headers: new HttpHeaders()
           .set('month', this.pCurrentMonth.toString())
           .set('year', this.pCurrentYear.toString())
      }
    ).subscribe(
      (page: CurrencyRate[]) => this.pCurrencyRatesSubject.next(page)
    );
  }

  public get currenciesRatesObservable(): Observable<CurrencyRate[]> {
    return this.pCurrencyRatesSubject;
  }

  public get currentCurrency(): string {
    return this.pCurrentCurrency;
  }

  public set currentCurrency(currency: string) {
    this.pCurrentCurrency = currency;
    this.refreshCurrenciesRateObservable();
  }

  public get currentYear(): number {
    return this.pCurrentYear;
  }

  public set currentYear(year: number) {
    this.pCurrentYear = year;
    this.refreshCurrenciesRateObservable();
  }

  public get currentMonth(): number {
    return this.pCurrentMonth;
  }

  public set currentMonth(month: number) {
    this.pCurrentMonth = month;
    this.refreshCurrenciesRateObservable();
  }

}
