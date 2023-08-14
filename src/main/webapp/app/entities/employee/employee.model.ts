import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IJob } from 'app/entities/job/job.model';
import { ISilverBadge } from 'app/entities/silver-badge/silver-badge.model';
import { IGoldenBadge } from 'app/entities/golden-badge/golden-badge.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IJobHistory } from 'app/entities/job-history/job-history.model';

export interface IEmployee {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
  salary?: number | null;
  commissionPct?: number | null;
  user?: IUser | null;
  jobs?: IJob[] | null;
  manager?: IEmployee | null;
  sibag?: ISilverBadge;
  gobag?: IGoldenBadge;
  department?: IDepartment | null;
  histories?: IJobHistory[] | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public hireDate?: dayjs.Dayjs | null,
    public salary?: number | null,
    public commissionPct?: number | null,
    public user?: IUser | null,
    public jobs?: IJob[] | null,
    public manager?: IEmployee | null,
    public sibag?: ISilverBadge,
    public gobag?: IGoldenBadge,
    public department?: IDepartment | null,
    public histories?: IJobHistory[] | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
