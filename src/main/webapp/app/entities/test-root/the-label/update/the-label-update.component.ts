import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITheLabel, TheLabel } from '../the-label.model';
import { TheLabelService } from '../service/the-label.service';

@Component({
  selector: 'jhi-the-label-update',
  templateUrl: './the-label-update.component.html',
})
export class TheLabelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    labelName: [null, [Validators.required, Validators.minLength(3)]],
  });

  constructor(protected theLabelService: TheLabelService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ theLabel }) => {
      this.updateForm(theLabel);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const theLabel = this.createFromForm();
    if (theLabel.id !== undefined) {
      this.subscribeToSaveResponse(this.theLabelService.update(theLabel));
    } else {
      this.subscribeToSaveResponse(this.theLabelService.create(theLabel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITheLabel>>): void {
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

  protected updateForm(theLabel: ITheLabel): void {
    this.editForm.patchValue({
      id: theLabel.id,
      labelName: theLabel.labelName,
    });
  }

  protected createFromForm(): ITheLabel {
    return {
      ...new TheLabel(),
      id: this.editForm.get(['id'])!.value,
      labelName: this.editForm.get(['labelName'])!.value,
    };
  }
}
