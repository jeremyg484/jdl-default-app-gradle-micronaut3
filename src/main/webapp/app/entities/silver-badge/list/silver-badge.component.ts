import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISilverBadge } from '../silver-badge.model';
import { SilverBadgeService } from '../service/silver-badge.service';
import { SilverBadgeDeleteDialogComponent } from '../delete/silver-badge-delete-dialog.component';

@Component({
  selector: 'jhi-silver-badge',
  templateUrl: './silver-badge.component.html',
})
export class SilverBadgeComponent implements OnInit {
  silverBadges?: ISilverBadge[];
  isLoading = false;

  constructor(protected silverBadgeService: SilverBadgeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.silverBadgeService.query().subscribe({
      next: (res: HttpResponse<ISilverBadge[]>) => {
        this.isLoading = false;
        this.silverBadges = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISilverBadge): number {
    return item.id!;
  }

  delete(silverBadge: ISilverBadge): void {
    const modalRef = this.modalService.open(SilverBadgeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.silverBadge = silverBadge;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
