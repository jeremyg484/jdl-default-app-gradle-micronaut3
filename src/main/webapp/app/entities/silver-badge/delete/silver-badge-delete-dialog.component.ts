import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISilverBadge } from '../silver-badge.model';
import { SilverBadgeService } from '../service/silver-badge.service';

@Component({
  templateUrl: './silver-badge-delete-dialog.component.html',
})
export class SilverBadgeDeleteDialogComponent {
  silverBadge?: ISilverBadge;

  constructor(protected silverBadgeService: SilverBadgeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.silverBadgeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
