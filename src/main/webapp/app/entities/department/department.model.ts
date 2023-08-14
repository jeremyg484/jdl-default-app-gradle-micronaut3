import { ILocation } from 'app/entities/location/location.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IJobHistory } from 'app/entities/job-history/job-history.model';

export interface IDepartment {
  id?: number;
  name?: string;
  description?: string | null;
  advertisementContentType?: string | null;
  advertisement?: string | null;
  logoContentType?: string | null;
  logo?: string | null;
  location?: ILocation | null;
  employees?: IEmployee[] | null;
  histories?: IJobHistory[] | null;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public advertisementContentType?: string | null,
    public advertisement?: string | null,
    public logoContentType?: string | null,
    public logo?: string | null,
    public location?: ILocation | null,
    public employees?: IEmployee[] | null,
    public histories?: IJobHistory[] | null
  ) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}
