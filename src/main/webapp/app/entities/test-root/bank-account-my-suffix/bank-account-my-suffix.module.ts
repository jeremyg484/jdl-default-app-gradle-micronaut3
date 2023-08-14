import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BankAccountMySuffixComponent } from './list/bank-account-my-suffix.component';
import { BankAccountMySuffixDetailComponent } from './detail/bank-account-my-suffix-detail.component';
import { BankAccountMySuffixUpdateComponent } from './update/bank-account-my-suffix-update.component';
import { BankAccountMySuffixDeleteDialogComponent } from './delete/bank-account-my-suffix-delete-dialog.component';
import { BankAccountMySuffixRoutingModule } from './route/bank-account-my-suffix-routing.module';

@NgModule({
  imports: [SharedModule, BankAccountMySuffixRoutingModule],
  declarations: [
    BankAccountMySuffixComponent,
    BankAccountMySuffixDetailComponent,
    BankAccountMySuffixUpdateComponent,
    BankAccountMySuffixDeleteDialogComponent,
  ],
  entryComponents: [BankAccountMySuffixDeleteDialogComponent],
})
export class BankAccountMySuffixModule {}
