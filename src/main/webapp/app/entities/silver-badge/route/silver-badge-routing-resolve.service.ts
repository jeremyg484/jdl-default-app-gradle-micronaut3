import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISilverBadge, SilverBadge } from '../silver-badge.model';
import { SilverBadgeService } from '../service/silver-badge.service';

@Injectable({ providedIn: 'root' })
export class SilverBadgeRoutingResolveService implements Resolve<ISilverBadge> {
  constructor(protected service: SilverBadgeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISilverBadge> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((silverBadge: HttpResponse<SilverBadge>) => {
          if (silverBadge.body) {
            return of(silverBadge.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SilverBadge());
  }
}
