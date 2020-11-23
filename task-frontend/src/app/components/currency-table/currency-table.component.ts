import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatTable} from '@angular/material/table';
import {CurrencyRetrieverService} from '../../services/currency-retriever.service';
import {CurrencyRatePercent} from '../../model/currency.rate';
import {map} from 'rxjs/operators';
import {CurrencyRateRetrieverService} from '../../services/currency-rate-retriever.service';

@Component({
  selector: 'app-currency-table',
  templateUrl: './currency-table.component.html',
  styleUrls: ['./currency-table.component.css']
})
export class CurrencyTableComponent implements AfterViewInit {
  @ViewChild(MatTable) table: MatTable<CurrencyRatePercent>;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['code', 'date', 'rate'];

  public pMaxRate: number;
  public pMinRate: number;

  public showPlaceholder: boolean;

  constructor(private currencyRates: CurrencyRateRetrieverService) {
    this.showPlaceholder = true;
  }

  ngAfterViewInit(): void {
    // Synonym.
    const ratesObs = this.currencyRates.currenciesRatesObservable;
    // Disable placeholder.
    ratesObs.subscribe(() => {
      this.showPlaceholder = false;
    });
    // Calc averages and etc.
    ratesObs.subscribe(items => {
      this.pMaxRate = Math.max(...items.map(item => item.rate));
    });
    ratesObs.subscribe(items => {
      this.pMinRate = Math.min(...items.map(item => item.rate));
    });
    this.table.dataSource = ratesObs.pipe(
      map(items => {
        const allRateValues = items.map(item => item.rate);
        const maxRateValue = Math.max(...allRateValues);
        const minRateValue = Math.min(...allRateValues);
        const rateValuesPercents = allRateValues.map(rateValue =>
          Math.floor(100 * (rateValue - minRateValue) / (maxRateValue - minRateValue))
        );
        return items.map((item, index) => {
          return {ratePercent: rateValuesPercents[index], ...item} as CurrencyRatePercent;
        });
      })
    );
  }
}
