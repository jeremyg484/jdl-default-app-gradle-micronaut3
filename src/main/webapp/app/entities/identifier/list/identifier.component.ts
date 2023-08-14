import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IIdentifier } from '../identifier.model';
import { IdentifierService } from '../service/identifier.service';
import { IdentifierDeleteDialogComponent } from '../delete/identifier-delete-dialog.component';

@Component({
  selector: 'jhi-identifier',
  templateUrl: './identifier.component.html',
})
export class IdentifierComponent implements OnInit {
  identifiers?: IIdentifier[];
  isLoading = false;

  constructor(protected identifierService: IdentifierService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.identifierService.query().subscribe({
      next: (res: HttpResponse<IIdentifier[]>) => {
        this.isLoading = false;
        this.identifiers = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IIdentifier): number {
    return item.id!;
  }

  delete(identifier: IIdentifier): void {
    const modalRef = this.modalService.open(IdentifierDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.identifier = identifier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
