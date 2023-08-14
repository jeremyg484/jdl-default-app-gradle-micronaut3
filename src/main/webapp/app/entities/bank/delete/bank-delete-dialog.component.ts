import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBank } from '../bank.model';
import { BankService } from '../service/bank.service';

@Component({
  templateUrl: './bank-delete-dialog.component.html',
})
export class BankDeleteDialogComponent {
  bank?: IBank;

  constructor(protected bankService: BankService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
