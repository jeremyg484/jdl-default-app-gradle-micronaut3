import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';

export interface IBank {
  id?: number;
  bankNumber?: number | null;
  bankAccounts?: IBankAccountMySuffix[] | null;
}

export class Bank implements IBank {
  constructor(public id?: number, public bankNumber?: number | null, public bankAccounts?: IBankAccountMySuffix[] | null) {}
}

export function getBankIdentifier(bank: IBank): number | undefined {
  return bank.id;
}
