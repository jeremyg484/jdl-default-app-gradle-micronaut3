import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBankAccountMySuffix, BankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { BankAccountType } from 'app/entities/enumerations/bank-account-type.model';

@Component({
  selector: 'jhi-bank-account-my-suffix-update',
  templateUrl: './bank-account-my-suffix-update.component.html',
})
export class BankAccountMySuffixUpdateComponent implements OnInit {
  isSaving = false;
  bankAccountTypeValues = Object.keys(BankAccountType);

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    bankNumber: [],
    agencyNumber: [],
    lastOperationDuration: [],
    meanOperationDuration: [],
    balance: [null, [Validators.required]],
    openingDay: [],
    lastOperationDate: [],
    active: [],
    accountType: [],
    attachment: [],
    attachmentContentType: [],
    description: [],
    user: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected bankAccountService: BankAccountMySuffixService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankAccount }) => {
      if (bankAccount.id === undefined) {
        const today = dayjs().startOf('day');
        bankAccount.lastOperationDate = today;
      }

      this.updateForm(bankAccount);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('jhipsterSampleApplicationApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankAccount = this.createFromForm();
    if (bankAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.bankAccountService.update(bankAccount));
    } else {
      this.subscribeToSaveResponse(this.bankAccountService.create(bankAccount));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankAccountMySuffix>>): void {
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

  protected updateForm(bankAccount: IBankAccountMySuffix): void {
    this.editForm.patchValue({
      id: bankAccount.id,
      name: bankAccount.name,
      bankNumber: bankAccount.bankNumber,
      agencyNumber: bankAccount.agencyNumber,
      lastOperationDuration: bankAccount.lastOperationDuration,
      meanOperationDuration: bankAccount.meanOperationDuration,
      balance: bankAccount.balance,
      openingDay: bankAccount.openingDay,
      lastOperationDate: bankAccount.lastOperationDate ? bankAccount.lastOperationDate.format(DATE_TIME_FORMAT) : null,
      active: bankAccount.active,
      accountType: bankAccount.accountType,
      attachment: bankAccount.attachment,
      attachmentContentType: bankAccount.attachmentContentType,
      description: bankAccount.description,
      user: bankAccount.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, bankAccount.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IBankAccountMySuffix {
    return {
      ...new BankAccountMySuffix(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      bankNumber: this.editForm.get(['bankNumber'])!.value,
      agencyNumber: this.editForm.get(['agencyNumber'])!.value,
      lastOperationDuration: this.editForm.get(['lastOperationDuration'])!.value,
      meanOperationDuration: this.editForm.get(['meanOperationDuration'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      openingDay: this.editForm.get(['openingDay'])!.value,
      lastOperationDate: this.editForm.get(['lastOperationDate'])!.value
        ? dayjs(this.editForm.get(['lastOperationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      active: this.editForm.get(['active'])!.value,
      accountType: this.editForm.get(['accountType'])!.value,
      attachmentContentType: this.editForm.get(['attachmentContentType'])!.value,
      attachment: this.editForm.get(['attachment'])!.value,
      description: this.editForm.get(['description'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
