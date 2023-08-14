import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';

@Component({
  templateUrl: './bank-account-my-suffix-delete-dialog.component.html',
})
export class BankAccountMySuffixDeleteDialogComponent {
  bankAccount?: IBankAccountMySuffix;

  constructor(protected bankAccountService: BankAccountMySuffixService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankAccountService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
