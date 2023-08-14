import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIdentifier } from '../identifier.model';
import { IdentifierService } from '../service/identifier.service';

@Component({
  templateUrl: './identifier-delete-dialog.component.html',
})
export class IdentifierDeleteDialogComponent {
  identifier?: IIdentifier;

  constructor(protected identifierService: IdentifierService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.identifierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
