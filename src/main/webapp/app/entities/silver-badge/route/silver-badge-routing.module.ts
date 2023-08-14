import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SilverBadgeComponent } from '../list/silver-badge.component';
import { SilverBadgeDetailComponent } from '../detail/silver-badge-detail.component';
import { SilverBadgeUpdateComponent } from '../update/silver-badge-update.component';
import { SilverBadgeRoutingResolveService } from './silver-badge-routing-resolve.service';

const silverBadgeRoute: Routes = [
  {
    path: '',
    component: SilverBadgeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SilverBadgeDetailComponent,
    resolve: {
      silverBadge: SilverBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SilverBadgeUpdateComponent,
    resolve: {
      silverBadge: SilverBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SilverBadgeUpdateComponent,
    resolve: {
      silverBadge: SilverBadgeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(silverBadgeRoute)],
  exports: [RouterModule],
})
export class SilverBadgeRoutingModule {}
