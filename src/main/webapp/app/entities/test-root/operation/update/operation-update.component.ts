import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOperation, Operation } from '../operation.model';
import { OperationService } from '../service/operation.service';
import { ITheLabel } from 'app/entities/test-root/the-label/the-label.model';
import { TheLabelService } from 'app/entities/test-root/the-label/service/the-label.service';
import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';
import { BankAccountMySuffixService } from 'app/entities/test-root/bank-account-my-suffix/service/bank-account-my-suffix.service';

@Component({
  selector: 'jhi-operation-update',
  templateUrl: './operation-update.component.html',
})
export class OperationUpdateComponent implements OnInit {
  isSaving = false;

  theLabelsSharedCollection: ITheLabel[] = [];
  bankAccountsSharedCollection: IBankAccountMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    description: [],
    amount: [null, [Validators.required]],
    theLabels: [],
    bankAccount: [],
  });

  constructor(
    protected operationService: OperationService,
    protected theLabelService: TheLabelService,
    protected bankAccountService: BankAccountMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ operation }) => {
      if (operation.id === undefined) {
        const today = dayjs().startOf('day');
        operation.date = today;
      }

      this.updateForm(operation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const operation = this.createFromForm();
    if (operation.id !== undefined) {
      this.subscribeToSaveResponse(this.operationService.update(operation));
    } else {
      this.subscribeToSaveResponse(this.operationService.create(operation));
    }
  }

  trackTheLabelById(_index: number, item: ITheLabel): number {
    return item.id!;
  }

  trackBankAccountMySuffixById(_index: number, item: IBankAccountMySuffix): number {
    return item.id!;
  }

  getSelectedTheLabel(option: ITheLabel, selectedVals?: ITheLabel[]): ITheLabel {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOperation>>): void {
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

  protected updateForm(operation: IOperation): void {
    this.editForm.patchValue({
      id: operation.id,
      date: operation.date ? operation.date.format(DATE_TIME_FORMAT) : null,
      description: operation.description,
      amount: operation.amount,
      theLabels: operation.theLabels,
      bankAccount: operation.bankAccount,
    });

    this.theLabelsSharedCollection = this.theLabelService.addTheLabelToCollectionIfMissing(
      this.theLabelsSharedCollection,
      ...(operation.theLabels ?? [])
    );
    this.bankAccountsSharedCollection = this.bankAccountService.addBankAccountMySuffixToCollectionIfMissing(
      this.bankAccountsSharedCollection,
      operation.bankAccount
    );
  }

  protected loadRelationshipsOptions(): void {
    this.theLabelService
      .query()
      .pipe(map((res: HttpResponse<ITheLabel[]>) => res.body ?? []))
      .pipe(
        map((theLabels: ITheLabel[]) =>
          this.theLabelService.addTheLabelToCollectionIfMissing(theLabels, ...(this.editForm.get('theLabels')!.value ?? []))
        )
      )
      .subscribe((theLabels: ITheLabel[]) => (this.theLabelsSharedCollection = theLabels));

    this.bankAccountService
      .query()
      .pipe(map((res: HttpResponse<IBankAccountMySuffix[]>) => res.body ?? []))
      .pipe(
        map((bankAccounts: IBankAccountMySuffix[]) =>
          this.bankAccountService.addBankAccountMySuffixToCollectionIfMissing(bankAccounts, this.editForm.get('bankAccount')!.value)
        )
      )
      .subscribe((bankAccounts: IBankAccountMySuffix[]) => (this.bankAccountsSharedCollection = bankAccounts));
  }

  protected createFromForm(): IOperation {
    return {
      ...new Operation(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      description: this.editForm.get(['description'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      theLabels: this.editForm.get(['theLabels'])!.value,
      bankAccount: this.editForm.get(['bankAccount'])!.value,
    };
  }
}
