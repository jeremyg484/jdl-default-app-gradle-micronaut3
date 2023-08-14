export interface IIdentifier {
  id?: number;
  name?: string;
}

export class Identifier implements IIdentifier {
  constructor(public id?: number, public name?: string) {}
}

export function getIdentifierIdentifier(identifier: IIdentifier): number | undefined {
  return identifier.id;
}
