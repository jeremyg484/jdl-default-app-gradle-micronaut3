import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIdentifier, Identifier } from '../identifier.model';
import { IdentifierService } from '../service/identifier.service';

@Injectable({ providedIn: 'root' })
export class IdentifierRoutingResolveService implements Resolve<IIdentifier> {
  constructor(protected service: IdentifierService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIdentifier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((identifier: HttpResponse<Identifier>) => {
          if (identifier.body) {
            return of(identifier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Identifier());
  }
}
