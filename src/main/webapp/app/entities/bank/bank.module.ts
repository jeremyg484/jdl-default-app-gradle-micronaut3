import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BankComponent } from './list/bank.component';
import { BankDetailComponent } from './detail/bank-detail.component';
import { BankUpdateComponent } from './update/bank-update.component';
import { BankDeleteDialogComponent } from './delete/bank-delete-dialog.component';
import { BankRoutingModule } from './route/bank-routing.module';

@NgModule({
  imports: [SharedModule, BankRoutingModule],
  declarations: [BankComponent, BankDetailComponent, BankUpdateComponent, BankDeleteDialogComponent],
  entryComponents: [BankDeleteDialogComponent],
})
export class BankModule {}
