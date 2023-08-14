import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISilverBadge, SilverBadge } from '../silver-badge.model';

import { SilverBadgeService } from './silver-badge.service';

describe('SilverBadge Service', () => {
  let service: SilverBadgeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISilverBadge;
  let expectedResult: ISilverBadge | ISilverBadge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SilverBadgeService);
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

    it('should create a SilverBadge', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SilverBadge()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SilverBadge', () => {
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

    it('should partial update a SilverBadge', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new SilverBadge()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SilverBadge', () => {
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

    it('should delete a SilverBadge', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSilverBadgeToCollectionIfMissing', () => {
      it('should add a SilverBadge to an empty array', () => {
        const silverBadge: ISilverBadge = { id: 123 };
        expectedResult = service.addSilverBadgeToCollectionIfMissing([], silverBadge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(silverBadge);
      });

      it('should not add a SilverBadge to an array that contains it', () => {
        const silverBadge: ISilverBadge = { id: 123 };
        const silverBadgeCollection: ISilverBadge[] = [
          {
            ...silverBadge,
          },
          { id: 456 },
        ];
        expectedResult = service.addSilverBadgeToCollectionIfMissing(silverBadgeCollection, silverBadge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SilverBadge to an array that doesn't contain it", () => {
        const silverBadge: ISilverBadge = { id: 123 };
        const silverBadgeCollection: ISilverBadge[] = [{ id: 456 }];
        expectedResult = service.addSilverBadgeToCollectionIfMissing(silverBadgeCollection, silverBadge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(silverBadge);
      });

      it('should add only unique SilverBadge to an array', () => {
        const silverBadgeArray: ISilverBadge[] = [{ id: 123 }, { id: 456 }, { id: 68344 }];
        const silverBadgeCollection: ISilverBadge[] = [{ id: 123 }];
        expectedResult = service.addSilverBadgeToCollectionIfMissing(silverBadgeCollection, ...silverBadgeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const silverBadge: ISilverBadge = { id: 123 };
        const silverBadge2: ISilverBadge = { id: 456 };
        expectedResult = service.addSilverBadgeToCollectionIfMissing([], silverBadge, silverBadge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(silverBadge);
        expect(expectedResult).toContain(silverBadge2);
      });

      it('should accept null and undefined values', () => {
        const silverBadge: ISilverBadge = { id: 123 };
        expectedResult = service.addSilverBadgeToCollectionIfMissing([], null, silverBadge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(silverBadge);
      });

      it('should return initial array if no SilverBadge is added', () => {
        const silverBadgeCollection: ISilverBadge[] = [{ id: 123 }];
        expectedResult = service.addSilverBadgeToCollectionIfMissing(silverBadgeCollection, undefined, null);
        expect(expectedResult).toEqual(silverBadgeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
