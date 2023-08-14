import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITheLabel } from '../the-label.model';
import { TheLabelService } from '../service/the-label.service';

@Component({
  templateUrl: './the-label-delete-dialog.component.html',
})
export class TheLabelDeleteDialogComponent {
  theLabel?: ITheLabel;

  constructor(protected theLabelService: TheLabelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.theLabelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
