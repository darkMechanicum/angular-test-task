import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Currency} from '../model/currency';

@Injectable({
  providedIn: 'root'
})
export class CurrencyRetrieverService {

  // Constant page step size.
  private readonly step = 20;
  // Current values.
  private pCurrentMaxLimit: number;
  private pCurrentSearchString: string;
  private pEndLatch: boolean;
  private pCurrentFetched: Currency[];
  // Final observable.
  private pCurrencies: Subject<Currency[]>;

  constructor(
    private httpClient: HttpClient
  ) {
    this.pCurrencies = new Subject();
    this.pCurrentSearchString = null;
  }

  public get currencies(): Observable<Currency[]> {
    return this.pCurrencies;
  }

  public extendPage(a1: () => Promise<void> = null, a2: () => Promise<void> = null): void {
    this.extendPageAsync(a1, a2).then();
  }

  public async extendPageAsync(
    onSendRequestHandler: () => Promise<void> = null,
    onFetchHandler: () => Promise<void> = null
  ): Promise<void> {
    if (this.pEndLatch) {
      return;
    }
    if (onSendRequestHandler) {
      await onSendRequestHandler();
    }
    const fetchedCurrencies = await this.currenciesRequest();
    this.pCurrentMaxLimit += this.step;
    this.pCurrentFetched = [...this.pCurrentFetched, ...fetchedCurrencies];
    this.pCurrencies.next(this.pCurrentFetched);
    if (fetchedCurrencies.length === 0) {
      this.pEndLatch = true;
    }
    if (onFetchHandler) {
      await onFetchHandler();
    }
  }

  private currenciesRequest(): Promise<Currency[]> {
    let params = new HttpParams()
      .append('offset', `${this.pCurrentMaxLimit}`)
      .append('pageSize', `${this.step}`);
    if (this.pCurrentSearchString !== null && this.pCurrentSearchString !== '') {
      params = params.append('substring', this.pCurrentSearchString);
    }
    return this.httpClient
      .get<Currency[]>(`http://127.0.0.1:4200/api/currencies`, {params})
      .toPromise();
  }

  public reset(
    searchString: string,
    onSendRequestHandler: () => Promise<void> = null,
    onFetchHandler: () => Promise<void> = null
  ): void {
    this.pCurrentMaxLimit = 0;
    if (searchString === null) {
      this.pCurrentSearchString = '';
    } else {
      this.pCurrentSearchString = searchString;
    }
    this.pEndLatch = false;
    this.pCurrentFetched = [];
    this.extendPage(onSendRequestHandler, onFetchHandler);
  }

}
