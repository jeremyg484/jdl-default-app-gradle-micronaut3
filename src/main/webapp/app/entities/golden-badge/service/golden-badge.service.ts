import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGoldenBadge, getGoldenBadgeIdentifier } from '../golden-badge.model';

export type EntityResponseType = HttpResponse<IGoldenBadge>;
export type EntityArrayResponseType = HttpResponse<IGoldenBadge[]>;

@Injectable({ providedIn: 'root' })
export class GoldenBadgeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/golden-badges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(goldenBadge: IGoldenBadge): Observable<EntityResponseType> {
    return this.http.post<IGoldenBadge>(this.resourceUrl, goldenBadge, { observe: 'response' });
  }

  update(goldenBadge: IGoldenBadge): Observable<EntityResponseType> {
    return this.http.put<IGoldenBadge>(`${this.resourceUrl}/${getGoldenBadgeIdentifier(goldenBadge) as number}`, goldenBadge, {
      observe: 'response',
    });
  }

  partialUpdate(goldenBadge: IGoldenBadge): Observable<EntityResponseType> {
    return this.http.patch<IGoldenBadge>(`${this.resourceUrl}/${getGoldenBadgeIdentifier(goldenBadge) as number}`, goldenBadge, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGoldenBadge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGoldenBadge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGoldenBadgeToCollectionIfMissing(
    goldenBadgeCollection: IGoldenBadge[],
    ...goldenBadgesToCheck: (IGoldenBadge | null | undefined)[]
  ): IGoldenBadge[] {
    const goldenBadges: IGoldenBadge[] = goldenBadgesToCheck.filter(isPresent);
    if (goldenBadges.length > 0) {
      const goldenBadgeCollectionIdentifiers = goldenBadgeCollection.map(goldenBadgeItem => getGoldenBadgeIdentifier(goldenBadgeItem)!);
      const goldenBadgesToAdd = goldenBadges.filter(goldenBadgeItem => {
        const goldenBadgeIdentifier = getGoldenBadgeIdentifier(goldenBadgeItem);
        if (goldenBadgeIdentifier == null || goldenBadgeCollectionIdentifiers.includes(goldenBadgeIdentifier)) {
          return false;
        }
        goldenBadgeCollectionIdentifiers.push(goldenBadgeIdentifier);
        return true;
      });
      return [...goldenBadgesToAdd, ...goldenBadgeCollection];
    }
    return goldenBadgeCollection;
  }
}
