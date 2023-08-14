import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBank, Bank } from '../bank.model';
import { BankService } from '../service/bank.service';

@Injectable({ providedIn: 'root' })
export class BankRoutingResolveService implements Resolve<IBank> {
  constructor(protected service: BankService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBank> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bank: HttpResponse<Bank>) => {
          if (bank.body) {
            return of(bank.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bank());
  }
}
