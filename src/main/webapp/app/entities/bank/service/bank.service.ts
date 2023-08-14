import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBank, getBankIdentifier } from '../bank.model';

export type EntityResponseType = HttpResponse<IBank>;
export type EntityArrayResponseType = HttpResponse<IBank[]>;

@Injectable({ providedIn: 'root' })
export class BankService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/banks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bank: IBank): Observable<EntityResponseType> {
    return this.http.post<IBank>(this.resourceUrl, bank, { observe: 'response' });
  }

  update(bank: IBank): Observable<EntityResponseType> {
    return this.http.put<IBank>(`${this.resourceUrl}/${getBankIdentifier(bank) as number}`, bank, { observe: 'response' });
  }

  partialUpdate(bank: IBank): Observable<EntityResponseType> {
    return this.http.patch<IBank>(`${this.resourceUrl}/${getBankIdentifier(bank) as number}`, bank, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBank>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBank[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBankToCollectionIfMissing(bankCollection: IBank[], ...banksToCheck: (IBank | null | undefined)[]): IBank[] {
    const banks: IBank[] = banksToCheck.filter(isPresent);
    if (banks.length > 0) {
      const bankCollectionIdentifiers = bankCollection.map(bankItem => getBankIdentifier(bankItem)!);
      const banksToAdd = banks.filter(bankItem => {
        const bankIdentifier = getBankIdentifier(bankItem);
        if (bankIdentifier == null || bankCollectionIdentifiers.includes(bankIdentifier)) {
          return false;
        }
        bankCollectionIdentifiers.push(bankIdentifier);
        return true;
      });
      return [...banksToAdd, ...bankCollection];
    }
    return bankCollection;
  }
}
