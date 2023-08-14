import dayjs from 'dayjs/esm';
import { ITheLabel } from 'app/entities/test-root/the-label/the-label.model';
import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';

export interface IOperation {
  id?: number;
  date?: dayjs.Dayjs;
  description?: string | null;
  amount?: number;
  theLabels?: ITheLabel[] | null;
  bankAccount?: IBankAccountMySuffix | null;
}

export class Operation implements IOperation {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs,
    public description?: string | null,
    public amount?: number,
    public theLabels?: ITheLabel[] | null,
    public bankAccount?: IBankAccountMySuffix | null
  ) {}
}

export function getOperationIdentifier(operation: IOperation): number | undefined {
  return operation.id;
}
