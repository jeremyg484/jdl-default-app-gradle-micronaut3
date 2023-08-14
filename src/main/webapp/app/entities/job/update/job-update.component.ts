import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IJob, Job } from '../job.model';
import { JobService } from '../service/job.service';
import { ITask } from 'app/entities/task/task.model';
import { TaskService } from 'app/entities/task/service/task.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { JobType } from 'app/entities/enumerations/job-type.model';

@Component({
  selector: 'jhi-job-update',
  templateUrl: './job-update.component.html',
})
export class JobUpdateComponent implements OnInit {
  isSaving = false;
  jobTypeValues = Object.keys(JobType);

  tasksSharedCollection: ITask[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.minLength(5), Validators.maxLength(25)]],
    type: [],
    minSalary: [],
    maxSalary: [],
    chores: [],
    emp: [],
  });

  constructor(
    protected jobService: JobService,
    protected taskService: TaskService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ job }) => {
      this.updateForm(job);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const job = this.createFromForm();
    if (job.id !== undefined) {
      this.subscribeToSaveResponse(this.jobService.update(job));
    } else {
      this.subscribeToSaveResponse(this.jobService.create(job));
    }
  }

  trackTaskById(_index: number, item: ITask): number {
    return item.id!;
  }

  trackEmployeeById(_index: number, item: IEmployee): number {
    return item.id!;
  }

  getSelectedTask(option: ITask, selectedVals?: ITask[]): ITask {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJob>>): void {
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

  protected updateForm(job: IJob): void {
    this.editForm.patchValue({
      id: job.id,
      title: job.title,
      type: job.type,
      minSalary: job.minSalary,
      maxSalary: job.maxSalary,
      chores: job.chores,
      emp: job.emp,
    });

    this.tasksSharedCollection = this.taskService.addTaskToCollectionIfMissing(this.tasksSharedCollection, ...(job.chores ?? []));
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(this.employeesSharedCollection, job.emp);
  }

  protected loadRelationshipsOptions(): void {
    this.taskService
      .query()
      .pipe(map((res: HttpResponse<ITask[]>) => res.body ?? []))
      .pipe(map((tasks: ITask[]) => this.taskService.addTaskToCollectionIfMissing(tasks, ...(this.editForm.get('chores')!.value ?? []))))
      .subscribe((tasks: ITask[]) => (this.tasksSharedCollection = tasks));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) => this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('emp')!.value))
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  protected createFromForm(): IJob {
    return {
      ...new Job(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      type: this.editForm.get(['type'])!.value,
      minSalary: this.editForm.get(['minSalary'])!.value,
      maxSalary: this.editForm.get(['maxSalary'])!.value,
      chores: this.editForm.get(['chores'])!.value,
      emp: this.editForm.get(['emp'])!.value,
    };
  }
}
