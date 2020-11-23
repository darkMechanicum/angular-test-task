import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-currency-date',
  templateUrl: './currency-date.component.html',
  styleUrls: ['./currency-date.component.css']
})
export class CurrencyDateComponent implements OnInit {

  today: Date;
  sixMonthsAgo: Date;

  constructor() {
    this.today = new Date();
    this.sixMonthsAgo = new Date();
    this.sixMonthsAgo.setMonth(this.today.getMonth() - 6);
  }

  openDatePicker(dp): void {
    dp.open();
  }

  closeDatePicker(eventData: any, dp?: any): void {
    dp.elementRef.nativeElement.hide();
    // get month and year from eventData and close datepicker, thus not allowing user to select date
    dp.close();
  }

  ngOnInit(): void {
  }

}
