import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IIdentifier, Identifier } from '../identifier.model';
import { IdentifierService } from '../service/identifier.service';

@Component({
  selector: 'jhi-identifier-update',
  templateUrl: './identifier-update.component.html',
})
export class IdentifierUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected identifierService: IdentifierService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ identifier }) => {
      this.updateForm(identifier);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const identifier = this.createFromForm();
    if (identifier.id !== undefined) {
      this.subscribeToSaveResponse(this.identifierService.update(identifier));
    } else {
      this.subscribeToSaveResponse(this.identifierService.create(identifier));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdentifier>>): void {
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

  protected updateForm(identifier: IIdentifier): void {
    this.editForm.patchValue({
      id: identifier.id,
      name: identifier.name,
    });
  }

  protected createFromForm(): IIdentifier {
    return {
      ...new Identifier(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
