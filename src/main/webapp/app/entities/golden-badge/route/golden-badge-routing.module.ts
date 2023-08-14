import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GoldenBadgeComponent } from '../list/golden-badge.component';
import { GoldenBadgeDetailComponent } from '../detail/golden-badge-detail.component';
import { GoldenBadgeUpdateComponent } from '../update/golden-badge-update.component';
import { GoldenBadgeRoutingResolveService } from './golden-badge-routing-resolve.service';

const goldenBadgeRoute: Routes = [
  {
    path: '',
    component: GoldenBadgeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GoldenBadgeDetailComponent,
    resolve: {
      goldenBadge: GoldenBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GoldenBadgeUpdateComponent,
    resolve: {
      goldenBadge: GoldenBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GoldenBadgeUpdateComponent,
    resolve: {
      goldenBadge: GoldenBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(goldenBadgeRoute)],
  exports: [RouterModule],
})
export class GoldenBadgeRoutingModule {}
