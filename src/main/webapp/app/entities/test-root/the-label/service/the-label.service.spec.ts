import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITheLabel, TheLabel } from '../the-label.model';

import { TheLabelService } from './the-label.service';

describe('TheLabel Service', () => {
  let service: TheLabelService;
  let httpMock: HttpTestingController;
  let elemDefault: ITheLabel;
  let expectedResult: ITheLabel | ITheLabel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TheLabelService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      labelName: 'AAAAAAA',
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

    it('should create a TheLabel', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TheLabel()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TheLabel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          labelName: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TheLabel', () => {
      const patchObject = Object.assign(
        {
          labelName: 'BBBBBB',
        },
        new TheLabel()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TheLabel', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          labelName: 'BBBBBB',
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

    it('should delete a TheLabel', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTheLabelToCollectionIfMissing', () => {
      it('should add a TheLabel to an empty array', () => {
        const theLabel: ITheLabel = { id: 123 };
        expectedResult = service.addTheLabelToCollectionIfMissing([], theLabel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(theLabel);
      });

      it('should not add a TheLabel to an array that contains it', () => {
        const theLabel: ITheLabel = { id: 123 };
        const theLabelCollection: ITheLabel[] = [
          {
            ...theLabel,
          },
          { id: 456 },
        ];
        expectedResult = service.addTheLabelToCollectionIfMissing(theLabelCollection, theLabel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TheLabel to an array that doesn't contain it", () => {
        const theLabel: ITheLabel = { id: 123 };
        const theLabelCollection: ITheLabel[] = [{ id: 456 }];
        expectedResult = service.addTheLabelToCollectionIfMissing(theLabelCollection, theLabel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(theLabel);
      });

      it('should add only unique TheLabel to an array', () => {
        const theLabelArray: ITheLabel[] = [{ id: 123 }, { id: 456 }, { id: 56528 }];
        const theLabelCollection: ITheLabel[] = [{ id: 123 }];
        expectedResult = service.addTheLabelToCollectionIfMissing(theLabelCollection, ...theLabelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const theLabel: ITheLabel = { id: 123 };
        const theLabel2: ITheLabel = { id: 456 };
        expectedResult = service.addTheLabelToCollectionIfMissing([], theLabel, theLabel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(theLabel);
        expect(expectedResult).toContain(theLabel2);
      });

      it('should accept null and undefined values', () => {
        const theLabel: ITheLabel = { id: 123 };
        expectedResult = service.addTheLabelToCollectionIfMissing([], null, theLabel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(theLabel);
      });

      it('should return initial array if no TheLabel is added', () => {
        const theLabelCollection: ITheLabel[] = [{ id: 123 }];
        expectedResult = service.addTheLabelToCollectionIfMissing(theLabelCollection, undefined, null);
        expect(expectedResult).toEqual(theLabelCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
