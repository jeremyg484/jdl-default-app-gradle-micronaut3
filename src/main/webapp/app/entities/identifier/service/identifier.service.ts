import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIdentifier, getIdentifierIdentifier } from '../identifier.model';

export type EntityResponseType = HttpResponse<IIdentifier>;
export type EntityArrayResponseType = HttpResponse<IIdentifier[]>;

@Injectable({ providedIn: 'root' })
export class IdentifierService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/identifiers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(identifier: IIdentifier): Observable<EntityResponseType> {
    return this.http.post<IIdentifier>(this.resourceUrl, identifier, { observe: 'response' });
  }

  update(identifier: IIdentifier): Observable<EntityResponseType> {
    return this.http.put<IIdentifier>(`${this.resourceUrl}/${getIdentifierIdentifier(identifier) as number}`, identifier, {
      observe: 'response',
    });
  }

  partialUpdate(identifier: IIdentifier): Observable<EntityResponseType> {
    return this.http.patch<IIdentifier>(`${this.resourceUrl}/${getIdentifierIdentifier(identifier) as number}`, identifier, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIdentifier>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIdentifier[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIdentifierToCollectionIfMissing(
    identifierCollection: IIdentifier[],
    ...identifiersToCheck: (IIdentifier | null | undefined)[]
  ): IIdentifier[] {
    const identifiers: IIdentifier[] = identifiersToCheck.filter(isPresent);
    if (identifiers.length > 0) {
      const identifierCollectionIdentifiers = identifierCollection.map(identifierItem => getIdentifierIdentifier(identifierItem)!);
      const identifiersToAdd = identifiers.filter(identifierItem => {
        const identifierIdentifier = getIdentifierIdentifier(identifierItem);
        if (identifierIdentifier == null || identifierCollectionIdentifiers.includes(identifierIdentifier)) {
          return false;
        }
        identifierCollectionIdentifiers.push(identifierIdentifier);
        return true;
      });
      return [...identifiersToAdd, ...identifierCollection];
    }
    return identifierCollection;
  }
}
