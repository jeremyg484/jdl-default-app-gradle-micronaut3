import dayjs from 'dayjs/esm';
import { IDepartment } from 'app/entities/department/department.model';
import { IJob } from 'app/entities/job/job.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IJobHistory {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  language?: Language | null;
  departments?: IDepartment[] | null;
  jobs?: IJob[] | null;
  emps?: IEmployee[] | null;
}

export class JobHistory implements IJobHistory {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public language?: Language | null,
    public departments?: IDepartment[] | null,
    public jobs?: IJob[] | null,
    public emps?: IEmployee[] | null
  ) {}
}

export function getJobHistoryIdentifier(jobHistory: IJobHistory): number | undefined {
  return jobHistory.id;
}
