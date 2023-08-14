import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGoldenBadge, GoldenBadge } from '../golden-badge.model';
import { GoldenBadgeService } from '../service/golden-badge.service';
import { IIdentifier } from 'app/entities/identifier/identifier.model';
import { IdentifierService } from 'app/entities/identifier/service/identifier.service';

@Component({
  selector: 'jhi-golden-badge-update',
  templateUrl: './golden-badge-update.component.html',
})
export class GoldenBadgeUpdateComponent implements OnInit {
  isSaving = false;

  identifiersSharedCollection: IIdentifier[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    iden: [null, Validators.required],
  });

  constructor(
    protected goldenBadgeService: GoldenBadgeService,
    protected identifierService: IdentifierService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goldenBadge }) => {
      this.updateForm(goldenBadge);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const goldenBadge = this.createFromForm();
    if (goldenBadge.id !== undefined) {
      this.subscribeToSaveResponse(this.goldenBadgeService.update(goldenBadge));
    } else {
      this.subscribeToSaveResponse(this.goldenBadgeService.create(goldenBadge));
    }
  }

  trackIdentifierById(_index: number, item: IIdentifier): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoldenBadge>>): void {
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

  protected updateForm(goldenBadge: IGoldenBadge): void {
    this.editForm.patchValue({
      id: goldenBadge.id,
      name: goldenBadge.name,
      iden: goldenBadge.iden,
    });

    this.identifiersSharedCollection = this.identifierService.addIdentifierToCollectionIfMissing(
      this.identifiersSharedCollection,
      goldenBadge.iden
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

  protected createFromForm(): IGoldenBadge {
    return {
      ...new GoldenBadge(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      iden: this.editForm.get(['iden'])!.value,
    };
  }
}
