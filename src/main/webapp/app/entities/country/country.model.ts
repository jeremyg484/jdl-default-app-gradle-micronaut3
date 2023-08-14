import { IRegion } from 'app/entities/region/region.model';
import { ILocation } from 'app/entities/location/location.model';

export interface ICountry {
  id?: number;
  name?: string | null;
  areas?: IRegion[] | null;
  location?: ILocation | null;
}

export class Country implements ICountry {
  constructor(public id?: number, public name?: string | null, public areas?: IRegion[] | null, public location?: ILocation | null) {}
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}
