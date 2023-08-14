import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoldenBadge } from '../golden-badge.model';
import { GoldenBadgeService } from '../service/golden-badge.service';
import { GoldenBadgeDeleteDialogComponent } from '../delete/golden-badge-delete-dialog.component';

@Component({
  selector: 'jhi-golden-badge',
  templateUrl: './golden-badge.component.html',
})
export class GoldenBadgeComponent implements OnInit {
  goldenBadges?: IGoldenBadge[];
  isLoading = false;

  constructor(protected goldenBadgeService: GoldenBadgeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.goldenBadgeService.query().subscribe({
      next: (res: HttpResponse<IGoldenBadge[]>) => {
        this.isLoading = false;
        this.goldenBadges = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IGoldenBadge): number {
    return item.id!;
  }

  delete(goldenBadge: IGoldenBadge): void {
    const modalRef = this.modalService.open(GoldenBadgeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.goldenBadge = goldenBadge;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
