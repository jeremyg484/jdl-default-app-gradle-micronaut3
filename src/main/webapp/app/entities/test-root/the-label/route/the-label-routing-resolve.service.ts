import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITheLabel, TheLabel } from '../the-label.model';
import { TheLabelService } from '../service/the-label.service';

@Injectable({ providedIn: 'root' })
export class TheLabelRoutingResolveService implements Resolve<ITheLabel> {
  constructor(protected service: TheLabelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITheLabel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((theLabel: HttpResponse<TheLabel>) => {
          if (theLabel.body) {
            return of(theLabel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TheLabel());
  }
}
