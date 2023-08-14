import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';
import { BankAccountMySuffixDeleteDialogComponent } from '../delete/bank-account-my-suffix-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-bank-account-my-suffix',
  templateUrl: './bank-account-my-suffix.component.html',
})
export class BankAccountMySuffixComponent implements OnInit {
  bankAccounts?: IBankAccountMySuffix[];
  isLoading = false;

  constructor(protected bankAccountService: BankAccountMySuffixService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bankAccountService.query().subscribe({
      next: (res: HttpResponse<IBankAccountMySuffix[]>) => {
        this.isLoading = false;
        this.bankAccounts = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBankAccountMySuffix): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(bankAccount: IBankAccountMySuffix): void {
    const modalRef = this.modalService.open(BankAccountMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bankAccount = bankAccount;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
