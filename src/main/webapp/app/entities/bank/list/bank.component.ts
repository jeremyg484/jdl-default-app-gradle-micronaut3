import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBank } from '../bank.model';
import { BankService } from '../service/bank.service';
import { BankDeleteDialogComponent } from '../delete/bank-delete-dialog.component';

@Component({
  selector: 'jhi-bank',
  templateUrl: './bank.component.html',
})
export class BankComponent implements OnInit {
  banks?: IBank[];
  isLoading = false;

  constructor(protected bankService: BankService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bankService.query().subscribe({
      next: (res: HttpResponse<IBank[]>) => {
        this.isLoading = false;
        this.banks = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBank): number {
    return item.id!;
  }

  delete(bank: IBank): void {
    const modalRef = this.modalService.open(BankDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bank = bank;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
