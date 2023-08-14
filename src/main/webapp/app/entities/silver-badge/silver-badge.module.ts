import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SilverBadgeComponent } from './list/silver-badge.component';
import { SilverBadgeDetailComponent } from './detail/silver-badge-detail.component';
import { SilverBadgeUpdateComponent } from './update/silver-badge-update.component';
import { SilverBadgeDeleteDialogComponent } from './delete/silver-badge-delete-dialog.component';
import { SilverBadgeRoutingModule } from './route/silver-badge-routing.module';

@NgModule({
  imports: [SharedModule, SilverBadgeRoutingModule],
  declarations: [SilverBadgeComponent, SilverBadgeDetailComponent, SilverBadgeUpdateComponent, SilverBadgeDeleteDialogComponent],
  entryComponents: [SilverBadgeDeleteDialogComponent],
})
export class SilverBadgeModule {}
