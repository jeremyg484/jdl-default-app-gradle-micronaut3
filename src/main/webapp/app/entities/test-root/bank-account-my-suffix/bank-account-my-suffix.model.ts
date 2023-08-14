import dayjs from 'dayjs/esm';
import { IOperation } from 'app/entities/test-root/operation/operation.model';
import { IUser } from 'app/entities/user/user.model';
import { IBank } from 'app/entities/bank/bank.model';
import { BankAccountType } from 'app/entities/enumerations/bank-account-type.model';

export interface IBankAccountMySuffix {
  id?: number;
  name?: string;
  bankNumber?: number | null;
  agencyNumber?: number | null;
  lastOperationDuration?: number | null;
  meanOperationDuration?: number | null;
  balance?: number;
  openingDay?: dayjs.Dayjs | null;
  lastOperationDate?: dayjs.Dayjs | null;
  active?: boolean | null;
  accountType?: BankAccountType | null;
  attachmentContentType?: string | null;
  attachment?: string | null;
  description?: string | null;
  operations?: IOperation[] | null;
  user?: IUser | null;
  banks?: IBank[] | null;
}

export class BankAccountMySuffix implements IBankAccountMySuffix {
  constructor(
    public id?: number,
    public name?: string,
    public bankNumber?: number | null,
    public agencyNumber?: number | null,
    public lastOperationDuration?: number | null,
    public meanOperationDuration?: number | null,
    public balance?: number,
    public openingDay?: dayjs.Dayjs | null,
    public lastOperationDate?: dayjs.Dayjs | null,
    public active?: boolean | null,
    public accountType?: BankAccountType | null,
    public attachmentContentType?: string | null,
    public attachment?: string | null,
    public description?: string | null,
    public operations?: IOperation[] | null,
    public user?: IUser | null,
    public banks?: IBank[] | null
  ) {
    this.active = this.active ?? false;
  }
}

export function getBankAccountMySuffixIdentifier(bankAccount: IBankAccountMySuffix): number | undefined {
  return bankAccount.id;
}
