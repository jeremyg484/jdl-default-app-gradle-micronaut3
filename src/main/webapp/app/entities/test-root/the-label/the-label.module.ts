import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TheLabelComponent } from './list/the-label.component';
import { TheLabelDetailComponent } from './detail/the-label-detail.component';
import { TheLabelUpdateComponent } from './update/the-label-update.component';
import { TheLabelDeleteDialogComponent } from './delete/the-label-delete-dialog.component';
import { TheLabelRoutingModule } from './route/the-label-routing.module';

@NgModule({
  imports: [SharedModule, TheLabelRoutingModule],
  declarations: [TheLabelComponent, TheLabelDetailComponent, TheLabelUpdateComponent, TheLabelDeleteDialogComponent],
  entryComponents: [TheLabelDeleteDialogComponent],
})
export class TheLabelModule {}
