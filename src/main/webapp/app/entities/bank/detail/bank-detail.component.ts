import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBank } from '../bank.model';

@Component({
  selector: 'jhi-bank-detail',
  templateUrl: './bank-detail.component.html',
})
export class BankDetailComponent implements OnInit {
  bank: IBank | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bank }) => {
      this.bank = bank;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
