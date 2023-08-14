import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IJobHistory, JobHistory } from '../job-history.model';
import { JobHistoryService } from '../service/job-history.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IJob } from 'app/entities/job/job.model';
import { JobService } from 'app/entities/job/service/job.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  selector: 'jhi-job-history-update',
  templateUrl: './job-history-update.component.html',
})
export class JobHistoryUpdateComponent implements OnInit {
  isSaving = false;
  languageValues = Object.keys(Language);

  departmentsSharedCollection: IDepartment[] = [];
  jobsSharedCollection: IJob[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    startDate: [],
    endDate: [],
    language: [],
    departments: [],
    jobs: [],
    emps: [],
  });

  constructor(
    protected jobHistoryService: JobHistoryService,
    protected departmentService: DepartmentService,
    protected jobService: JobService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobHistory }) => {
      if (jobHistory.id === undefined) {
        const today = dayjs().startOf('day');
        jobHistory.startDate = today;
        jobHistory.endDate = today;
      }

      this.updateForm(jobHistory);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobHistory = this.createFromForm();
    if (jobHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.jobHistoryService.update(jobHistory));
    } else {
      this.subscribeToSaveResponse(this.jobHistoryService.create(jobHistory));
    }
  }

  trackDepartmentById(_index: number, item: IDepartment): number {
    return item.id!;
  }

  trackJobById(_index: number, item: IJob): number {
    return item.id!;
  }

  trackEmployeeById(_index: number, item: IEmployee): number {
    return item.id!;
  }

  getSelectedDepartment(option: IDepartment, selectedVals?: IDepartment[]): IDepartment {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedJob(option: IJob, selectedVals?: IJob[]): IJob {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedEmployee(option: IEmployee, selectedVals?: IEmployee[]): IEmployee {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobHistory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(jobHistory: IJobHistory): void {
    this.editForm.patchValue({
      id: jobHistory.id,
      startDate: jobHistory.startDate ? jobHistory.startDate.format(DATE_TIME_FORMAT) : null,
      endDate: jobHistory.endDate ? jobHistory.endDate.format(DATE_TIME_FORMAT) : null,
      language: jobHistory.language,
      departments: jobHistory.departments,
      jobs: jobHistory.jobs,
      emps: jobHistory.emps,
    });

    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      ...(jobHistory.departments ?? [])
    );
    this.jobsSharedCollection = this.jobService.addJobToCollectionIfMissing(this.jobsSharedCollection, ...(jobHistory.jobs ?? []));
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      ...(jobHistory.emps ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, ...(this.editForm.get('departments')!.value ?? []))
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.jobService
      .query()
      .pipe(map((res: HttpResponse<IJob[]>) => res.body ?? []))
      .pipe(map((jobs: IJob[]) => this.jobService.addJobToCollectionIfMissing(jobs, ...(this.editForm.get('jobs')!.value ?? []))))
      .subscribe((jobs: IJob[]) => (this.jobsSharedCollection = jobs));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, ...(this.editForm.get('emps')!.value ?? []))
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  protected createFromForm(): IJobHistory {
    return {
      ...new JobHistory(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      endDate: this.editForm.get(['endDate'])!.value ? dayjs(this.editForm.get(['endDate'])!.value, DATE_TIME_FORMAT) : undefined,
      language: this.editForm.get(['language'])!.value,
      departments: this.editForm.get(['departments'])!.value,
      jobs: this.editForm.get(['jobs'])!.value,
      emps: this.editForm.get(['emps'])!.value,
    };
  }
}
