import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BankComponent } from '../list/bank.component';
import { BankDetailComponent } from '../detail/bank-detail.component';
import { BankUpdateComponent } from '../update/bank-update.component';
import { BankRoutingResolveService } from './bank-routing-resolve.service';

const bankRoute: Routes = [
  {
    path: '',
    component: BankComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BankDetailComponent,
    resolve: {
      bank: BankRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BankUpdateComponent,
    resolve: {
      bank: BankRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BankUpdateComponent,
    resolve: {
      bank: BankRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bankRoute)],
  exports: [RouterModule],
})
export class BankRoutingModule {}
