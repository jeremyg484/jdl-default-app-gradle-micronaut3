import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGoldenBadge, GoldenBadge } from '../golden-badge.model';
import { GoldenBadgeService } from '../service/golden-badge.service';

@Injectable({ providedIn: 'root' })
export class GoldenBadgeRoutingResolveService implements Resolve<IGoldenBadge> {
  constructor(protected service: GoldenBadgeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGoldenBadge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((goldenBadge: HttpResponse<GoldenBadge>) => {
          if (goldenBadge.body) {
            return of(goldenBadge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GoldenBadge());
  }
}
