import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBankAccountMySuffix, BankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';

@Injectable({ providedIn: 'root' })
export class BankAccountMySuffixRoutingResolveService implements Resolve<IBankAccountMySuffix> {
  constructor(protected service: BankAccountMySuffixService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBankAccountMySuffix> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bankAccount: HttpResponse<BankAccountMySuffix>) => {
          if (bankAccount.body) {
            return of(bankAccount.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BankAccountMySuffix());
  }
}
