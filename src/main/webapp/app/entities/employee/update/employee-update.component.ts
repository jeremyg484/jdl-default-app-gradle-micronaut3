import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmployee, Employee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISilverBadge } from 'app/entities/silver-badge/silver-badge.model';
import { SilverBadgeService } from 'app/entities/silver-badge/service/silver-badge.service';
import { IGoldenBadge } from 'app/entities/golden-badge/golden-badge.model';
import { GoldenBadgeService } from 'app/entities/golden-badge/service/golden-badge.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  employeesSharedCollection: IEmployee[] = [];
  silverBadgesSharedCollection: ISilverBadge[] = [];
  goldenBadgesSharedCollection: IGoldenBadge[] = [];
  departmentsSharedCollection: IDepartment[] = [];

  editForm = this.fb.group({
    id: [],
    firstName: [],
    lastName: [],
    email: [],
    phoneNumber: [],
    hireDate: [],
    salary: [],
    commissionPct: [],
    user: [],
    manager: [],
    sibag: [null, Validators.required],
    gobag: [null, Validators.required],
    department: [],
  });

  constructor(
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected silverBadgeService: SilverBadgeService,
    protected goldenBadgeService: GoldenBadgeService,
    protected departmentService: DepartmentService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      if (employee.id === undefined) {
        const today = dayjs().startOf('day');
        employee.hireDate = today;
      }

      this.updateForm(employee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.createFromForm();
    if (employee.id !== undefined) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  trackEmployeeById(_index: number, item: IEmployee): number {
    return item.id!;
  }

  trackSilverBadgeById(_index: number, item: ISilverBadge): number {
    return item.id!;
  }

  trackGoldenBadgeById(_index: number, item: IGoldenBadge): number {
    return item.id!;
  }

  trackDepartmentById(_index: number, item: IDepartment): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.editForm.patchValue({
      id: employee.id,
      firstName: employee.firstName,
      lastName: employee.lastName,
      email: employee.email,
      phoneNumber: employee.phoneNumber,
      hireDate: employee.hireDate ? employee.hireDate.format(DATE_TIME_FORMAT) : null,
      salary: employee.salary,
      commissionPct: employee.commissionPct,
      user: employee.user,
      manager: employee.manager,
      sibag: employee.sibag,
      gobag: employee.gobag,
      department: employee.department,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, employee.user);
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing(
      this.employeesSharedCollection,
      employee.manager
    );
    this.silverBadgesSharedCollection = this.silverBadgeService.addSilverBadgeToCollectionIfMissing(
      this.silverBadgesSharedCollection,
      employee.sibag
    );
    this.goldenBadgesSharedCollection = this.goldenBadgeService.addGoldenBadgeToCollectionIfMissing(
      this.goldenBadgesSharedCollection,
      employee.gobag
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing(
      this.departmentsSharedCollection,
      employee.department
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing(employees, this.editForm.get('manager')!.value)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.silverBadgeService
      .query()
      .pipe(map((res: HttpResponse<ISilverBadge[]>) => res.body ?? []))
      .pipe(
        map((silverBadges: ISilverBadge[]) =>
          this.silverBadgeService.addSilverBadgeToCollectionIfMissing(silverBadges, this.editForm.get('sibag')!.value)
        )
      )
      .subscribe((silverBadges: ISilverBadge[]) => (this.silverBadgesSharedCollection = silverBadges));

    this.goldenBadgeService
      .query()
      .pipe(map((res: HttpResponse<IGoldenBadge[]>) => res.body ?? []))
      .pipe(
        map((goldenBadges: IGoldenBadge[]) =>
          this.goldenBadgeService.addGoldenBadgeToCollectionIfMissing(goldenBadges, this.editForm.get('gobag')!.value)
        )
      )
      .subscribe((goldenBadges: IGoldenBadge[]) => (this.goldenBadgesSharedCollection = goldenBadges));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing(departments, this.editForm.get('department')!.value)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));
  }

  protected createFromForm(): IEmployee {
    return {
      ...new Employee(),
      id: this.editForm.get(['id'])!.value,
      firstName: this.editForm.get(['firstName'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      hireDate: this.editForm.get(['hireDate'])!.value ? dayjs(this.editForm.get(['hireDate'])!.value, DATE_TIME_FORMAT) : undefined,
      salary: this.editForm.get(['salary'])!.value,
      commissionPct: this.editForm.get(['commissionPct'])!.value,
      user: this.editForm.get(['user'])!.value,
      manager: this.editForm.get(['manager'])!.value,
      sibag: this.editForm.get(['sibag'])!.value,
      gobag: this.editForm.get(['gobag'])!.value,
      department: this.editForm.get(['department'])!.value,
    };
  }
}
