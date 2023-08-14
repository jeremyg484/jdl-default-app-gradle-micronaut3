import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISilverBadge, getSilverBadgeIdentifier } from '../silver-badge.model';

export type EntityResponseType = HttpResponse<ISilverBadge>;
export type EntityArrayResponseType = HttpResponse<ISilverBadge[]>;

@Injectable({ providedIn: 'root' })
export class SilverBadgeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/silver-badges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(silverBadge: ISilverBadge): Observable<EntityResponseType> {
    return this.http.post<ISilverBadge>(this.resourceUrl, silverBadge, { observe: 'response' });
  }

  update(silverBadge: ISilverBadge): Observable<EntityResponseType> {
    return this.http.put<ISilverBadge>(`${this.resourceUrl}/${getSilverBadgeIdentifier(silverBadge) as number}`, silverBadge, {
      observe: 'response',
    });
  }

  partialUpdate(silverBadge: ISilverBadge): Observable<EntityResponseType> {
    return this.http.patch<ISilverBadge>(`${this.resourceUrl}/${getSilverBadgeIdentifier(silverBadge) as number}`, silverBadge, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISilverBadge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISilverBadge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSilverBadgeToCollectionIfMissing(
    silverBadgeCollection: ISilverBadge[],
    ...silverBadgesToCheck: (ISilverBadge | null | undefined)[]
  ): ISilverBadge[] {
    const silverBadges: ISilverBadge[] = silverBadgesToCheck.filter(isPresent);
    if (silverBadges.length > 0) {
      const silverBadgeCollectionIdentifiers = silverBadgeCollection.map(silverBadgeItem => getSilverBadgeIdentifier(silverBadgeItem)!);
      const silverBadgesToAdd = silverBadges.filter(silverBadgeItem => {
        const silverBadgeIdentifier = getSilverBadgeIdentifier(silverBadgeItem);
        if (silverBadgeIdentifier == null || silverBadgeCollectionIdentifiers.includes(silverBadgeIdentifier)) {
          return false;
        }
        silverBadgeCollectionIdentifiers.push(silverBadgeIdentifier);
        return true;
      });
      return [...silverBadgesToAdd, ...silverBadgeCollection];
    }
    return silverBadgeCollection;
  }
}
