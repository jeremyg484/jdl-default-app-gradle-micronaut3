import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBankAccountMySuffix } from '../bank-account-my-suffix.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-bank-account-my-suffix-detail',
  templateUrl: './bank-account-my-suffix-detail.component.html',
})
export class BankAccountMySuffixDetailComponent implements OnInit {
  bankAccount: IBankAccountMySuffix | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankAccount }) => {
      this.bankAccount = bankAccount;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
