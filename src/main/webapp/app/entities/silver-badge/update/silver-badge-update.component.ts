import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISilverBadge, SilverBadge } from '../silver-badge.model';
import { SilverBadgeService } from '../service/silver-badge.service';
import { IIdentifier } from 'app/entities/identifier/identifier.model';
import { IdentifierService } from 'app/entities/identifier/service/identifier.service';

@Component({
  selector: 'jhi-silver-badge-update',
  templateUrl: './silver-badge-update.component.html',
})
export class SilverBadgeUpdateComponent implements OnInit {
  isSaving = false;

  identifiersSharedCollection: IIdentifier[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    iden: [null, Validators.required],
  });

  constructor(
    protected silverBadgeService: SilverBadgeService,
    protected identifierService: IdentifierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ silverBadge }) => {
      this.updateForm(silverBadge);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const silverBadge = this.createFromForm();
    if (silverBadge.id !== undefined) {
      this.subscribeToSaveResponse(this.silverBadgeService.update(silverBadge));
    } else {
      this.subscribeToSaveResponse(this.silverBadgeService.create(silverBadge));
    }
  }

  trackIdentifierById(_index: number, item: IIdentifier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISilverBadge>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(silverBadge: ISilverBadge): void {
    this.editForm.patchValue({
      id: silverBadge.id,
      name: silverBadge.name,
      iden: silverBadge.iden,
    });

    this.identifiersSharedCollection = this.identifierService.addIdentifierToCollectionIfMissing(
      this.identifiersSharedCollection,
      silverBadge.iden
    );
  }

  protected loadRelationshipsOptions(): void {
    this.identifierService
      .query()
      .pipe(map((res: HttpResponse<IIdentifier[]>) => res.body ?? []))
      .pipe(
        map((identifiers: IIdentifier[]) =>
          this.identifierService.addIdentifierToCollectionIfMissing(identifiers, this.editForm.get('iden')!.value)
        )
      )
      .subscribe((identifiers: IIdentifier[]) => (this.identifiersSharedCollection = identifiers));
  }

  protected createFromForm(): ISilverBadge {
    return {
      ...new SilverBadge(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      iden: this.editForm.get(['iden'])!.value,
    };
  }
}
