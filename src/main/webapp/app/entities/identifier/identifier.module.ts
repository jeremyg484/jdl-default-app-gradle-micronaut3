import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IdentifierComponent } from './list/identifier.component';
import { IdentifierDetailComponent } from './detail/identifier-detail.component';
import { IdentifierUpdateComponent } from './update/identifier-update.component';
import { IdentifierDeleteDialogComponent } from './delete/identifier-delete-dialog.component';
import { IdentifierRoutingModule } from './route/identifier-routing.module';

@NgModule({
  imports: [SharedModule, IdentifierRoutingModule],
  declarations: [IdentifierComponent, IdentifierDetailComponent, IdentifierUpdateComponent, IdentifierDeleteDialogComponent],
  entryComponents: [IdentifierDeleteDialogComponent],
})
export class IdentifierModule {}
