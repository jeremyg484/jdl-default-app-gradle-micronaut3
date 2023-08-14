import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIdentifier, Identifier } from '../identifier.model';

import { IdentifierService } from './identifier.service';

describe('Identifier Service', () => {
  let service: IdentifierService;
  let httpMock: HttpTestingController;
  let elemDefault: IIdentifier;
  let expectedResult: IIdentifier | IIdentifier[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IdentifierService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a Identifier', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Identifier()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Identifier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Identifier', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new Identifier()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Identifier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a Identifier', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addIdentifierToCollectionIfMissing', () => {
      it('should add a Identifier to an empty array', () => {
        const identifier: IIdentifier = { id: 123 };
        expectedResult = service.addIdentifierToCollectionIfMissing([], identifier);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(identifier);
      });

      it('should not add a Identifier to an array that contains it', () => {
        const identifier: IIdentifier = { id: 123 };
        const identifierCollection: IIdentifier[] = [
          {
            ...identifier,
          },
          { id: 456 },
        ];
        expectedResult = service.addIdentifierToCollectionIfMissing(identifierCollection, identifier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Identifier to an array that doesn't contain it", () => {
        const identifier: IIdentifier = { id: 123 };
        const identifierCollection: IIdentifier[] = [{ id: 456 }];
        expectedResult = service.addIdentifierToCollectionIfMissing(identifierCollection, identifier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(identifier);
      });

      it('should add only unique Identifier to an array', () => {
        const identifierArray: IIdentifier[] = [{ id: 123 }, { id: 456 }, { id: 72770 }];
        const identifierCollection: IIdentifier[] = [{ id: 123 }];
        expectedResult = service.addIdentifierToCollectionIfMissing(identifierCollection, ...identifierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const identifier: IIdentifier = { id: 123 };
        const identifier2: IIdentifier = { id: 456 };
        expectedResult = service.addIdentifierToCollectionIfMissing([], identifier, identifier2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(identifier);
        expect(expectedResult).toContain(identifier2);
      });

      it('should accept null and undefined values', () => {
        const identifier: IIdentifier = { id: 123 };
        expectedResult = service.addIdentifierToCollectionIfMissing([], null, identifier, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(identifier);
      });

      it('should return initial array if no Identifier is added', () => {
        const identifierCollection: IIdentifier[] = [{ id: 123 }];
        expectedResult = service.addIdentifierToCollectionIfMissing(identifierCollection, undefined, null);
        expect(expectedResult).toEqual(identifierCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
