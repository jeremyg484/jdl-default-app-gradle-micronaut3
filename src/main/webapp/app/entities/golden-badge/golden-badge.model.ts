import { IIdentifier } from 'app/entities/identifier/identifier.model';

export interface IGoldenBadge {
  id?: number;
  name?: string | null;
  iden?: IIdentifier;
}

export class GoldenBadge implements IGoldenBadge {
  constructor(public id?: number, public name?: string | null, public iden?: IIdentifier) {}
}

export function getGoldenBadgeIdentifier(goldenBadge: IGoldenBadge): number | undefined {
  return goldenBadge.id;
}
