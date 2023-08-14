import { IIdentifier } from 'app/entities/identifier/identifier.model';

export interface ISilverBadge {
  id?: number;
  name?: string | null;
  iden?: IIdentifier;
}

export class SilverBadge implements ISilverBadge {
  constructor(public id?: number, public name?: string | null, public iden?: IIdentifier) {}
}

export function getSilverBadgeIdentifier(silverBadge: ISilverBadge): number | undefined {
  return silverBadge.id;
}
