import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGoldenBadge, GoldenBadge } from '../golden-badge.model';

import { GoldenBadgeService } from './golden-badge.service';

describe('GoldenBadge Service', () => {
  let service: GoldenBadgeService;
  let httpMock: HttpTestingController;
  let elemDefault: IGoldenBadge;
  let expectedResult: IGoldenBadge | IGoldenBadge[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GoldenBadgeService);
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

    it('should create a GoldenBadge', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new GoldenBadge()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GoldenBadge', () => {
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

    it('should partial update a GoldenBadge', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new GoldenBadge()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GoldenBadge', () => {
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

    it('should delete a GoldenBadge', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addGoldenBadgeToCollectionIfMissing', () => {
      it('should add a GoldenBadge to an empty array', () => {
        const goldenBadge: IGoldenBadge = { id: 123 };
        expectedResult = service.addGoldenBadgeToCollectionIfMissing([], goldenBadge);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(goldenBadge);
      });

      it('should not add a GoldenBadge to an array that contains it', () => {
        const goldenBadge: IGoldenBadge = { id: 123 };
        const goldenBadgeCollection: IGoldenBadge[] = [
          {
            ...goldenBadge,
          },
          { id: 456 },
        ];
        expectedResult = service.addGoldenBadgeToCollectionIfMissing(goldenBadgeCollection, goldenBadge);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GoldenBadge to an array that doesn't contain it", () => {
        const goldenBadge: IGoldenBadge = { id: 123 };
        const goldenBadgeCollection: IGoldenBadge[] = [{ id: 456 }];
        expectedResult = service.addGoldenBadgeToCollectionIfMissing(goldenBadgeCollection, goldenBadge);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(goldenBadge);
      });

      it('should add only unique GoldenBadge to an array', () => {
        const goldenBadgeArray: IGoldenBadge[] = [{ id: 123 }, { id: 456 }, { id: 68787 }];
        const goldenBadgeCollection: IGoldenBadge[] = [{ id: 123 }];
        expectedResult = service.addGoldenBadgeToCollectionIfMissing(goldenBadgeCollection, ...goldenBadgeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const goldenBadge: IGoldenBadge = { id: 123 };
        const goldenBadge2: IGoldenBadge = { id: 456 };
        expectedResult = service.addGoldenBadgeToCollectionIfMissing([], goldenBadge, goldenBadge2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(goldenBadge);
        expect(expectedResult).toContain(goldenBadge2);
      });

      it('should accept null and undefined values', () => {
        const goldenBadge: IGoldenBadge = { id: 123 };
        expectedResult = service.addGoldenBadgeToCollectionIfMissing([], null, goldenBadge, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(goldenBadge);
      });

      it('should return initial array if no GoldenBadge is added', () => {
        const goldenBadgeCollection: IGoldenBadge[] = [{ id: 123 }];
        expectedResult = service.addGoldenBadgeToCollectionIfMissing(goldenBadgeCollection, undefined, null);
        expect(expectedResult).toEqual(goldenBadgeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
