import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BankAccountMySuffixComponent } from '../list/bank-account-my-suffix.component';
import { BankAccountMySuffixDetailComponent } from '../detail/bank-account-my-suffix-detail.component';
import { BankAccountMySuffixUpdateComponent } from '../update/bank-account-my-suffix-update.component';
import { BankAccountMySuffixRoutingResolveService } from './bank-account-my-suffix-routing-resolve.service';

const bankAccountRoute: Routes = [
  {
    path: '',
    component: BankAccountMySuffixComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankAccountMySuffixDetailComponent,
    resolve: {
      bankAccount: BankAccountMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BankAccountMySuffixUpdateComponent,
    resolve: {
      bankAccount: BankAccountMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BankAccountMySuffixUpdateComponent,
    resolve: {
      bankAccount: BankAccountMySuffixRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bankAccountRoute)],
  exports: [RouterModule],
})
export class BankAccountMySuffixRoutingModule {}
