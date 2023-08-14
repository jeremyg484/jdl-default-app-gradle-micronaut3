import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoldenBadge } from '../golden-badge.model';
import { GoldenBadgeService } from '../service/golden-badge.service';

@Component({
  templateUrl: './golden-badge-delete-dialog.component.html',
})
export class GoldenBadgeDeleteDialogComponent {
  goldenBadge?: IGoldenBadge;

  constructor(protected goldenBadgeService: GoldenBadgeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.goldenBadgeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
