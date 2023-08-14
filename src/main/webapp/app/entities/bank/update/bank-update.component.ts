import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBank, Bank } from '../bank.model';
import { BankService } from '../service/bank.service';
import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';
import { BankAccountMySuffixService } from 'app/entities/test-root/bank-account-my-suffix/service/bank-account-my-suffix.service';

@Component({
  selector: 'jhi-bank-update',
  templateUrl: './bank-update.component.html',
})
export class BankUpdateComponent implements OnInit {
  isSaving = false;

  bankAccountsSharedCollection: IBankAccountMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    bankNumber: [],
    bankAccounts: [],
  });

  constructor(
    protected bankService: BankService,
    protected bankAccountService: BankAccountMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bank }) => {
      this.updateForm(bank);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bank = this.createFromForm();
    if (bank.id !== undefined) {
      this.subscribeToSaveResponse(this.bankService.update(bank));
    } else {
      this.subscribeToSaveResponse(this.bankService.create(bank));
    }
  }

  trackBankAccountMySuffixById(_index: number, item: IBankAccountMySuffix): number {
    return item.id!;
  }

  getSelectedBankAccountMySuffix(option: IBankAccountMySuffix, selectedVals?: IBankAccountMySuffix[]): IBankAccountMySuffix {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBank>>): void {
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

  protected updateForm(bank: IBank): void {
    this.editForm.patchValue({
      id: bank.id,
      bankNumber: bank.bankNumber,
      bankAccounts: bank.bankAccounts,
    });

    this.bankAccountsSharedCollection = this.bankAccountService.addBankAccountMySuffixToCollectionIfMissing(
      this.bankAccountsSharedCollection,
      ...(bank.bankAccounts ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bankAccountService
      .query()
      .pipe(map((res: HttpResponse<IBankAccountMySuffix[]>) => res.body ?? []))
      .pipe(
        map((bankAccounts: IBankAccountMySuffix[]) =>
          this.bankAccountService.addBankAccountMySuffixToCollectionIfMissing(
            bankAccounts,
            ...(this.editForm.get('bankAccounts')!.value ?? [])
          )
        )
      )
      .subscribe((bankAccounts: IBankAccountMySuffix[]) => (this.bankAccountsSharedCollection = bankAccounts));
  }

  protected createFromForm(): IBank {
    return {
      ...new Bank(),
      id: this.editForm.get(['id'])!.value,
      bankNumber: this.editForm.get(['bankNumber'])!.value,
      bankAccounts: this.editForm.get(['bankAccounts'])!.value,
    };
  }
}
