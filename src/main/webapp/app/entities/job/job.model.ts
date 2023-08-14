import { ITask } from 'app/entities/task/task.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IJobHistory } from 'app/entities/job-history/job-history.model';
import { JobType } from 'app/entities/enumerations/job-type.model';

export interface IJob {
  id?: number;
  title?: string | null;
  type?: JobType | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  chores?: ITask[] | null;
  emp?: IEmployee | null;
  histories?: IJobHistory[] | null;
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public title?: string | null,
    public type?: JobType | null,
    public minSalary?: number | null,
    public maxSalary?: number | null,
    public chores?: ITask[] | null,
    public emp?: IEmployee | null,
    public histories?: IJobHistory[] | null
  ) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
