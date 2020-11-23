import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {CurrencyRetrieverService} from '../../services/currency-retriever.service';
import {Currency} from '../../model/currency';
import {MatOptionSelectionChange} from '@angular/material/core';
import {fromEvent, interval, Observable, Subject} from 'rxjs';
import {CurrencyRateRetrieverService} from '../../services/currency-rate-retriever.service';
import {debounce, distinctUntilChanged, filter, map, share, takeUntil} from 'rxjs/operators';
import {MatAutocomplete} from '@angular/material/autocomplete';
import {MatInput} from '@angular/material/input';

interface ScrollEvent extends UIEvent {
  target: Element;
}

@Component({
  selector: 'app-currency-list',
  templateUrl: './currency-list.component.html',
  styleUrls: ['./currency-list.component.css']
})
export class CurrencyListComponent implements OnInit, OnDestroy {
  @ViewChild(MatAutocomplete)
  public childAutoComplete: MatAutocomplete;
  public myCurrencies: Observable<Currency[]>;
  public showSpinner: boolean;
  private lastScrollTop: number;
  private focusAfterSelected: boolean;
  private onStopMaxScroll$: Subject<void> = new Subject();
  private inputChange$: Subject<string> = new Subject();
  private timeoutPromise = (value: number) => new Promise((resolve) => setTimeout(resolve, value));

  constructor(
    private currencies: CurrencyRetrieverService,
    private currencyRates: CurrencyRateRetrieverService,
  ) {
    this.showSpinner = false;
  }

  private extendPage(): void {
    this.currencies.extendPage(
      async () => {
        this.showSpinner = true;
      },
      async () => {
        this.showSpinner = false;
      },
    );
  }

  onChangeSearchString(value: string): void {
    this.inputChange$.next(value);
  }

  onChangeSelection(event: MatOptionSelectionChange, input: HTMLInputElement): void {
    this.currencyRates.currentCurrency = (event.source.value as Currency).code;
    input.blur();
    this.focusAfterSelected = true;
    this.onChangeSearchString(null);
  }

  onFocus(input: HTMLInputElement): void {
    if (this.focusAfterSelected) {
      input.blur();
      this.focusAfterSelected = false;
    } else {
      input.select();
    }
  }

  ngOnInit(): void {
    this.myCurrencies = this.currencies.currencies;
    this.currencies.reset(null);
    // Small hack to debounce input changes.
    this.inputChange$.pipe(
      debounce(() => interval(300))
    ).subscribe((value) => this.currencies.reset(value));
  }

  public subscribeMaxScroll(): void {
    this.maxScroll()
      .then((subs) => subs.subscribe(() => this.extendPage()));
  }

  public unsubscribeMaxScroll(): void {
    this.onStopMaxScroll$.next();
  }

  ngOnDestroy(): void {
    this.onStopMaxScroll$.next();
  }

  displayCurrency(value: Currency): string {
    return value && value.name ? value.name : 'Нет наименования';
  }

  private async maxScroll(): Promise<Observable<any>> {
    // Small hack, since panel is not available at instance after opened Autocomplete event.
    await this.timeoutPromise(10);
    return fromEvent<ScrollEvent>(this.childAutoComplete.panel.nativeElement, 'scroll')
      .pipe(
        takeUntil(this.onStopMaxScroll$),
        map((event: ScrollEvent) => event.target),
        share(),
        /* Distinct IE scroll events. */
        distinctUntilChanged((target) => target.scrollTop === this.lastScrollTop),
        filter(({clientHeight, scrollTop, scrollHeight}: Element) => {
          this.lastScrollTop = scrollTop;
          /* Floor Chrome scroll position. */
          return Math.floor(clientHeight + scrollTop) === scrollHeight;
        }),
      );
  }

}
