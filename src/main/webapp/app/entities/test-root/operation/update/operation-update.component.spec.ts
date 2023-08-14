import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OperationService } from '../service/operation.service';
import { IOperation, Operation } from '../operation.model';
import { ITheLabel } from 'app/entities/test-root/the-label/the-label.model';
import { TheLabelService } from 'app/entities/test-root/the-label/service/the-label.service';
import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';
import { BankAccountMySuffixService } from 'app/entities/test-root/bank-account-my-suffix/service/bank-account-my-suffix.service';

import { OperationUpdateComponent } from './operation-update.component';

describe('Operation Management Update Component', () => {
  let comp: OperationUpdateComponent;
  let fixture: ComponentFixture<OperationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let operationService: OperationService;
  let theLabelService: TheLabelService;
  let bankAccountService: BankAccountMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OperationUpdateComponent],
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
      .overrideTemplate(OperationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OperationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    operationService = TestBed.inject(OperationService);
    theLabelService = TestBed.inject(TheLabelService);
    bankAccountService = TestBed.inject(BankAccountMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TheLabel query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const theLabels: ITheLabel[] = [{ id: 68210 }];
      operation.theLabels = theLabels;

      const theLabelCollection: ITheLabel[] = [{ id: 36584 }];
      jest.spyOn(theLabelService, 'query').mockReturnValue(of(new HttpResponse({ body: theLabelCollection })));
      const additionalTheLabels = [...theLabels];
      const expectedCollection: ITheLabel[] = [...additionalTheLabels, ...theLabelCollection];
      jest.spyOn(theLabelService, 'addTheLabelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(theLabelService.query).toHaveBeenCalled();
      expect(theLabelService.addTheLabelToCollectionIfMissing).toHaveBeenCalledWith(theLabelCollection, ...additionalTheLabels);
      expect(comp.theLabelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BankAccountMySuffix query and add missing value', () => {
      const operation: IOperation = { id: 456 };
      const bankAccount: IBankAccountMySuffix = { id: 65249 };
      operation.bankAccount = bankAccount;

      const bankAccountCollection: IBankAccountMySuffix[] = [{ id: 39284 }];
      jest.spyOn(bankAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: bankAccountCollection })));
      const additionalBankAccountMySuffixes = [bankAccount];
      const expectedCollection: IBankAccountMySuffix[] = [...additionalBankAccountMySuffixes, ...bankAccountCollection];
      jest.spyOn(bankAccountService, 'addBankAccountMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(bankAccountService.query).toHaveBeenCalled();
      expect(bankAccountService.addBankAccountMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
        bankAccountCollection,
        ...additionalBankAccountMySuffixes
      );
      expect(comp.bankAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const operation: IOperation = { id: 456 };
      const theLabels: ITheLabel = { id: 53600 };
      operation.theLabels = [theLabels];
      const bankAccount: IBankAccountMySuffix = { id: 53557 };
      operation.bankAccount = bankAccount;

      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(operation));
      expect(comp.theLabelsSharedCollection).toContain(theLabels);
      expect(comp.bankAccountsSharedCollection).toContain(bankAccount);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operation>>();
      const operation = { id: 123 };
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(operationService.update).toHaveBeenCalledWith(operation);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operation>>();
      const operation = new Operation();
      jest.spyOn(operationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: operation }));
      saveSubject.complete();

      // THEN
      expect(operationService.create).toHaveBeenCalledWith(operation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Operation>>();
      const operation = { id: 123 };
      jest.spyOn(operationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ operation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(operationService.update).toHaveBeenCalledWith(operation);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTheLabelById', () => {
      it('Should return tracked TheLabel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTheLabelById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackBankAccountMySuffixById', () => {
      it('Should return tracked BankAccountMySuffix primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBankAccountMySuffixById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedTheLabel', () => {
      it('Should return option if no TheLabel is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedTheLabel(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected TheLabel for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedTheLabel(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this TheLabel is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedTheLabel(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
