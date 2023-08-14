import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BankService } from '../service/bank.service';
import { IBank, Bank } from '../bank.model';
import { IBankAccountMySuffix } from 'app/entities/test-root/bank-account-my-suffix/bank-account-my-suffix.model';
import { BankAccountMySuffixService } from 'app/entities/test-root/bank-account-my-suffix/service/bank-account-my-suffix.service';

import { BankUpdateComponent } from './bank-update.component';

describe('Bank Management Update Component', () => {
  let comp: BankUpdateComponent;
  let fixture: ComponentFixture<BankUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bankService: BankService;
  let bankAccountService: BankAccountMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BankUpdateComponent],
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
      .overrideTemplate(BankUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bankService = TestBed.inject(BankService);
    bankAccountService = TestBed.inject(BankAccountMySuffixService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call BankAccountMySuffix query and add missing value', () => {
      const bank: IBank = { id: 456 };
      const bankAccounts: IBankAccountMySuffix[] = [{ id: 81157 }];
      bank.bankAccounts = bankAccounts;

      const bankAccountCollection: IBankAccountMySuffix[] = [{ id: 82458 }];
      jest.spyOn(bankAccountService, 'query').mockReturnValue(of(new HttpResponse({ body: bankAccountCollection })));
      const additionalBankAccountMySuffixes = [...bankAccounts];
      const expectedCollection: IBankAccountMySuffix[] = [...additionalBankAccountMySuffixes, ...bankAccountCollection];
      jest.spyOn(bankAccountService, 'addBankAccountMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      expect(bankAccountService.query).toHaveBeenCalled();
      expect(bankAccountService.addBankAccountMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
        bankAccountCollection,
        ...additionalBankAccountMySuffixes
      );
      expect(comp.bankAccountsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bank: IBank = { id: 456 };
      const bankAccounts: IBankAccountMySuffix = { id: 2289 };
      bank.bankAccounts = [bankAccounts];

      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bank));
      expect(comp.bankAccountsSharedCollection).toContain(bankAccounts);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bank>>();
      const bank = { id: 123 };
      jest.spyOn(bankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bank }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bankService.update).toHaveBeenCalledWith(bank);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bank>>();
      const bank = new Bank();
      jest.spyOn(bankService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bank }));
      saveSubject.complete();

      // THEN
      expect(bankService.create).toHaveBeenCalledWith(bank);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bank>>();
      const bank = { id: 123 };
      jest.spyOn(bankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bankService.update).toHaveBeenCalledWith(bank);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackBankAccountMySuffixById', () => {
      it('Should return tracked BankAccountMySuffix primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBankAccountMySuffixById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedBankAccountMySuffix', () => {
      it('Should return option if no BankAccountMySuffix is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedBankAccountMySuffix(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected BankAccountMySuffix for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedBankAccountMySuffix(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this BankAccountMySuffix is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedBankAccountMySuffix(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
