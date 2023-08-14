import { IOperation } from 'app/entities/test-root/operation/operation.model';

export interface ITheLabel {
  id?: number;
  labelName?: string;
  operations?: IOperation[] | null;
}

export class TheLabel implements ITheLabel {
  constructor(public id?: number, public labelName?: string, public operations?: IOperation[] | null) {}
}

export function getTheLabelIdentifier(theLabel: ITheLabel): number | undefined {
  return theLabel.id;
}
