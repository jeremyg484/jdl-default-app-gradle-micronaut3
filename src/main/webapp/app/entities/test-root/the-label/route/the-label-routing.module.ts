import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TheLabelComponent } from '../list/the-label.component';
import { TheLabelDetailComponent } from '../detail/the-label-detail.component';
import { TheLabelUpdateComponent } from '../update/the-label-update.component';
import { TheLabelRoutingResolveService } from './the-label-routing-resolve.service';

const theLabelRoute: Routes = [
  {
    path: '',
    component: TheLabelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TheLabelDetailComponent,
    resolve: {
      theLabel: TheLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TheLabelUpdateComponent,
    resolve: {
      theLabel: TheLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TheLabelUpdateComponent,
    resolve: {
      theLabel: TheLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(theLabelRoute)],
  exports: [RouterModule],
})
export class TheLabelRoutingModule {}
