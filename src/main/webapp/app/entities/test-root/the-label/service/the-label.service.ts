import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITheLabel, getTheLabelIdentifier } from '../the-label.model';

export type EntityResponseType = HttpResponse<ITheLabel>;
export type EntityArrayResponseType = HttpResponse<ITheLabel[]>;

@Injectable({ providedIn: 'root' })
export class TheLabelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/the-labels');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(theLabel: ITheLabel): Observable<EntityResponseType> {
    return this.http.post<ITheLabel>(this.resourceUrl, theLabel, { observe: 'response' });
  }

  update(theLabel: ITheLabel): Observable<EntityResponseType> {
    return this.http.put<ITheLabel>(`${this.resourceUrl}/${getTheLabelIdentifier(theLabel) as number}`, theLabel, { observe: 'response' });
  }

  partialUpdate(theLabel: ITheLabel): Observable<EntityResponseType> {
    return this.http.patch<ITheLabel>(`${this.resourceUrl}/${getTheLabelIdentifier(theLabel) as number}`, theLabel, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITheLabel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITheLabel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTheLabelToCollectionIfMissing(theLabelCollection: ITheLabel[], ...theLabelsToCheck: (ITheLabel | null | undefined)[]): ITheLabel[] {
    const theLabels: ITheLabel[] = theLabelsToCheck.filter(isPresent);
    if (theLabels.length > 0) {
      const theLabelCollectionIdentifiers = theLabelCollection.map(theLabelItem => getTheLabelIdentifier(theLabelItem)!);
      const theLabelsToAdd = theLabels.filter(theLabelItem => {
        const theLabelIdentifier = getTheLabelIdentifier(theLabelItem);
        if (theLabelIdentifier == null || theLabelCollectionIdentifiers.includes(theLabelIdentifier)) {
          return false;
        }
        theLabelCollectionIdentifiers.push(theLabelIdentifier);
        return true;
      });
      return [...theLabelsToAdd, ...theLabelCollection];
    }
    return theLabelCollection;
  }
}
