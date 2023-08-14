import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GoldenBadgeComponent } from './list/golden-badge.component';
import { GoldenBadgeDetailComponent } from './detail/golden-badge-detail.component';
import { GoldenBadgeUpdateComponent } from './update/golden-badge-update.component';
import { GoldenBadgeDeleteDialogComponent } from './delete/golden-badge-delete-dialog.component';
import { GoldenBadgeRoutingModule } from './route/golden-badge-routing.module';

@NgModule({
  imports: [SharedModule, GoldenBadgeRoutingModule],
  declarations: [GoldenBadgeComponent, GoldenBadgeDetailComponent, GoldenBadgeUpdateComponent, GoldenBadgeDeleteDialogComponent],
  entryComponents: [GoldenBadgeDeleteDialogComponent],
})
export class GoldenBadgeModule {}
