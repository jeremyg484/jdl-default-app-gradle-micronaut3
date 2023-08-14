import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { BankAccountType } from 'app/entities/enumerations/bank-account-type.model';
import { IBankAccountMySuffix, BankAccountMySuffix } from '../bank-account-my-suffix.model';

import { BankAccountMySuffixService } from './bank-account-my-suffix.service';

describe('BankAccountMySuffix Service', () => {
  let service: BankAccountMySuffixService;
  let httpMock: HttpTestingController;
  let elemDefault: IBankAccountMySuffix;
  let expectedResult: IBankAccountMySuffix | IBankAccountMySuffix[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BankAccountMySuffixService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      bankNumber: 0,
      agencyNumber: 0,
      lastOperationDuration: 0,
      meanOperationDuration: 0,
      balance: 0,
      openingDay: currentDate,
      lastOperationDate: currentDate,
      active: false,
      accountType: BankAccountType.CHECKING,
      attachmentContentType: 'image/png',
      attachment: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          openingDay: currentDate.format(DATE_FORMAT),
          lastOperationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a BankAccountMySuffix', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          openingDay: currentDate.format(DATE_FORMAT),
          lastOperationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          openingDay: currentDate,
          lastOperationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new BankAccountMySuffix()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BankAccountMySuffix', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          bankNumber: 1,
          agencyNumber: 1,
          lastOperationDuration: 1,
          meanOperationDuration: 1,
          balance: 1,
          openingDay: currentDate.format(DATE_FORMAT),
          lastOperationDate: currentDate.format(DATE_TIME_FORMAT),
          active: true,
          accountType: 'BBBBBB',
          attachment: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          openingDay: currentDate,
          lastOperationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BankAccountMySuffix', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          balance: 1,
          lastOperationDate: currentDate.format(DATE_TIME_FORMAT),
          active: true,
          attachment: 'BBBBBB',
          description: 'BBBBBB',
        },
        new BankAccountMySuffix()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          openingDay: currentDate,
          lastOperationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BankAccountMySuffix', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          bankNumber: 1,
          agencyNumber: 1,
          lastOperationDuration: 1,
          meanOperationDuration: 1,
          balance: 1,
          openingDay: currentDate.format(DATE_FORMAT),
          lastOperationDate: currentDate.format(DATE_TIME_FORMAT),
          active: true,
          accountType: 'BBBBBB',
          attachment: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          openingDay: currentDate,
          lastOperationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a BankAccountMySuffix', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBankAccountMySuffixToCollectionIfMissing', () => {
      it('should add a BankAccountMySuffix to an empty array', () => {
        const bankAccount: IBankAccountMySuffix = { id: 123 };
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing([], bankAccount);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankAccount);
      });

      it('should not add a BankAccountMySuffix to an array that contains it', () => {
        const bankAccount: IBankAccountMySuffix = { id: 123 };
        const bankAccountCollection: IBankAccountMySuffix[] = [
          {
            ...bankAccount,
          },
          { id: 456 },
        ];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, bankAccount);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BankAccountMySuffix to an array that doesn't contain it", () => {
        const bankAccount: IBankAccountMySuffix = { id: 123 };
        const bankAccountCollection: IBankAccountMySuffix[] = [{ id: 456 }];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, bankAccount);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankAccount);
      });

      it('should add only unique BankAccountMySuffix to an array', () => {
        const bankAccountArray: IBankAccountMySuffix[] = [{ id: 123 }, { id: 456 }, { id: 82172 }];
        const bankAccountCollection: IBankAccountMySuffix[] = [{ id: 123 }];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, ...bankAccountArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bankAccount: IBankAccountMySuffix = { id: 123 };
        const bankAccount2: IBankAccountMySuffix = { id: 456 };
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing([], bankAccount, bankAccount2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bankAccount);
        expect(expectedResult).toContain(bankAccount2);
      });

      it('should accept null and undefined values', () => {
        const bankAccount: IBankAccountMySuffix = { id: 123 };
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing([], null, bankAccount, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bankAccount);
      });

      it('should return initial array if no BankAccountMySuffix is added', () => {
        const bankAccountCollection: IBankAccountMySuffix[] = [{ id: 123 }];
        expectedResult = service.addBankAccountMySuffixToCollectionIfMissing(bankAccountCollection, undefined, null);
        expect(expectedResult).toEqual(bankAccountCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
