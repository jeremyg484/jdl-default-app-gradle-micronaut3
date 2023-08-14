import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBankAccountMySuffix, getBankAccountMySuffixIdentifier } from '../bank-account-my-suffix.model';

export type EntityResponseType = HttpResponse<IBankAccountMySuffix>;
export type EntityArrayResponseType = HttpResponse<IBankAccountMySuffix[]>;

@Injectable({ providedIn: 'root' })
export class BankAccountMySuffixService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bank-accounts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bankAccount: IBankAccountMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankAccount);
    return this.http
      .post<IBankAccountMySuffix>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bankAccount: IBankAccountMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankAccount);
    return this.http
      .put<IBankAccountMySuffix>(`${this.resourceUrl}/${getBankAccountMySuffixIdentifier(bankAccount) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(bankAccount: IBankAccountMySuffix): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bankAccount);
    return this.http
      .patch<IBankAccountMySuffix>(`${this.resourceUrl}/${getBankAccountMySuffixIdentifier(bankAccount) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBankAccountMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBankAccountMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBankAccountMySuffixToCollectionIfMissing(
    bankAccountCollection: IBankAccountMySuffix[],
    ...bankAccountsToCheck: (IBankAccountMySuffix | null | undefined)[]
  ): IBankAccountMySuffix[] {
    const bankAccounts: IBankAccountMySuffix[] = bankAccountsToCheck.filter(isPresent);
    if (bankAccounts.length > 0) {
      const bankAccountCollectionIdentifiers = bankAccountCollection.map(
        bankAccountItem => getBankAccountMySuffixIdentifier(bankAccountItem)!
      );
      const bankAccountsToAdd = bankAccounts.filter(bankAccountItem => {
        const bankAccountIdentifier = getBankAccountMySuffixIdentifier(bankAccountItem);
        if (bankAccountIdentifier == null || bankAccountCollectionIdentifiers.includes(bankAccountIdentifier)) {
          return false;
        }
        bankAccountCollectionIdentifiers.push(bankAccountIdentifier);
        return true;
      });
      return [...bankAccountsToAdd, ...bankAccountCollection];
    }
    return bankAccountCollection;
  }

  protected convertDateFromClient(bankAccount: IBankAccountMySuffix): IBankAccountMySuffix {
    return Object.assign({}, bankAccount, {
      openingDay: bankAccount.openingDay?.isValid() ? bankAccount.openingDay.format(DATE_FORMAT) : undefined,
      lastOperationDate: bankAccount.lastOperationDate?.isValid() ? bankAccount.lastOperationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.openingDay = res.body.openingDay ? dayjs(res.body.openingDay) : undefined;
      res.body.lastOperationDate = res.body.lastOperationDate ? dayjs(res.body.lastOperationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bankAccount: IBankAccountMySuffix) => {
        bankAccount.openingDay = bankAccount.openingDay ? dayjs(bankAccount.openingDay) : undefined;
        bankAccount.lastOperationDate = bankAccount.lastOperationDate ? dayjs(bankAccount.lastOperationDate) : undefined;
      });
    }
    return res;
  }
}
