import { ICountry } from 'app/entities/country/country.model';

export interface IRegion {
  id?: number;
  name?: string | null;
  country?: ICountry | null;
}

export class Region implements IRegion {
  constructor(public id?: number, public name?: string | null, public country?: ICountry | null) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
