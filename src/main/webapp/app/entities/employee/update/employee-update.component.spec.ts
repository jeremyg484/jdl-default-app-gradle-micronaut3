import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeService } from '../service/employee.service';
import { IEmployee, Employee } from '../employee.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISilverBadge } from 'app/entities/silver-badge/silver-badge.model';
import { SilverBadgeService } from 'app/entities/silver-badge/service/silver-badge.service';
import { IGoldenBadge } from 'app/entities/golden-badge/golden-badge.model';
import { GoldenBadgeService } from 'app/entities/golden-badge/service/golden-badge.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeService: EmployeeService;
  let userService: UserService;
  let silverBadgeService: SilverBadgeService;
  let goldenBadgeService: GoldenBadgeService;
  let departmentService: DepartmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeService = TestBed.inject(EmployeeService);
    userService = TestBed.inject(UserService);
    silverBadgeService = TestBed.inject(SilverBadgeService);
    goldenBadgeService = TestBed.inject(GoldenBadgeService);
    departmentService = TestBed.inject(DepartmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const user: IUser = { id: 84478 };
      employee.user = user;

      const userCollection: IUser[] = [{ id: 1447 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Employee query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const manager: IEmployee = { id: 4374 };
      employee.manager = manager;

      const employeeCollection: IEmployee[] = [{ id: 10177 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [manager];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(employeeCollection, ...additionalEmployees);
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SilverBadge query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const sibag: ISilverBadge = { id: 23057 };
      employee.sibag = sibag;

      const silverBadgeCollection: ISilverBadge[] = [{ id: 24344 }];
      jest.spyOn(silverBadgeService, 'query').mockReturnValue(of(new HttpResponse({ body: silverBadgeCollection })));
      const additionalSilverBadges = [sibag];
      const expectedCollection: ISilverBadge[] = [...additionalSilverBadges, ...silverBadgeCollection];
      jest.spyOn(silverBadgeService, 'addSilverBadgeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(silverBadgeService.query).toHaveBeenCalled();
      expect(silverBadgeService.addSilverBadgeToCollectionIfMissing).toHaveBeenCalledWith(silverBadgeCollection, ...additionalSilverBadges);
      expect(comp.silverBadgesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call GoldenBadge query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const gobag: IGoldenBadge = { id: 51460 };
      employee.gobag = gobag;

      const goldenBadgeCollection: IGoldenBadge[] = [{ id: 4079 }];
      jest.spyOn(goldenBadgeService, 'query').mockReturnValue(of(new HttpResponse({ body: goldenBadgeCollection })));
      const additionalGoldenBadges = [gobag];
      const expectedCollection: IGoldenBadge[] = [...additionalGoldenBadges, ...goldenBadgeCollection];
      jest.spyOn(goldenBadgeService, 'addGoldenBadgeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(goldenBadgeService.query).toHaveBeenCalled();
      expect(goldenBadgeService.addGoldenBadgeToCollectionIfMissing).toHaveBeenCalledWith(goldenBadgeCollection, ...additionalGoldenBadges);
      expect(comp.goldenBadgesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const department: IDepartment = { id: 35363 };
      employee.department = department;

      const departmentCollection: IDepartment[] = [{ id: 78278 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(departmentCollection, ...additionalDepartments);
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const user: IUser = { id: 66698 };
      employee.user = user;
      const manager: IEmployee = { id: 79320 };
      employee.manager = manager;
      const sibag: ISilverBadge = { id: 65347 };
      employee.sibag = sibag;
      const gobag: IGoldenBadge = { id: 28948 };
      employee.gobag = gobag;
      const department: IDepartment = { id: 60127 };
      employee.department = department;

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(employee));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.employeesSharedCollection).toContain(manager);
      expect(comp.silverBadgesSharedCollection).toContain(sibag);
      expect(comp.goldenBadgesSharedCollection).toContain(gobag);
      expect(comp.departmentsSharedCollection).toContain(department);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = new Employee();
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeService.create).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Employee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalledWith(employee);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEmployeeById', () => {
      it('Should return tracked Employee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEmployeeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSilverBadgeById', () => {
      it('Should return tracked SilverBadge primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSilverBadgeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackGoldenBadgeById', () => {
      it('Should return tracked GoldenBadge primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGoldenBadgeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDepartmentById', () => {
      it('Should return tracked Department primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDepartmentById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
