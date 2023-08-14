import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBank, Bank } from '../bank.model';

import { BankService } from './bank.service';

describe('Bank Service', () => {
  let service: BankService;
  let httpMock: HttpTestingController;
  let elemDefault: IBank;
  let expectedResult: IBank | IBank[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BankService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bankNumber: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Bank', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Bank()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bank', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bankNumber: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bank', () => {
      const patchObject = Object.assign({}, new Bank());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bank', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bankNumber: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Bank', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBankToCollectionIfMissing', () => {
      it('should add a Bank to an empty array', () => {
        const bank: IBank = { id: 123 };
        expectedResult = service.addBankToCollectionIfMissing([], bank);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bank);
      });

      it('should not add a Bank to an array that contains it', () => {
        const bank: IBank = { id: 123 };
        const bankCollection: IBank[] = [
          {
            ...bank,
          },
          { id: 456 },
        ];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, bank);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bank to an array that doesn't contain it", () => {
        const bank: IBank = { id: 123 };
        const bankCollection: IBank[] = [{ id: 456 }];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, bank);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bank);
      });

      it('should add only unique Bank to an array', () => {
        const bankArray: IBank[] = [{ id: 123 }, { id: 456 }, { id: 20967 }];
        const bankCollection: IBank[] = [{ id: 123 }];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, ...bankArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bank: IBank = { id: 123 };
        const bank2: IBank = { id: 456 };
        expectedResult = service.addBankToCollectionIfMissing([], bank, bank2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bank);
        expect(expectedResult).toContain(bank2);
      });

      it('should accept null and undefined values', () => {
        const bank: IBank = { id: 123 };
        expectedResult = service.addBankToCollectionIfMissing([], null, bank, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bank);
      });

      it('should return initial array if no Bank is added', () => {
        const bankCollection: IBank[] = [{ id: 123 }];
        expectedResult = service.addBankToCollectionIfMissing(bankCollection, undefined, null);
        expect(expectedResult).toEqual(bankCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
