import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IdentifierComponent } from '../list/identifier.component';
import { IdentifierDetailComponent } from '../detail/identifier-detail.component';
import { IdentifierUpdateComponent } from '../update/identifier-update.component';
import { IdentifierRoutingResolveService } from './identifier-routing-resolve.service';

const identifierRoute: Routes = [
  {
    path: '',
    component: IdentifierComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IdentifierDetailComponent,
    resolve: {
      identifier: IdentifierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IdentifierUpdateComponent,
    resolve: {
      identifier: IdentifierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IdentifierUpdateComponent,
    resolve: {
      identifier: IdentifierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(identifierRoute)],
  exports: [RouterModule],
})
export class IdentifierRoutingModule {}
