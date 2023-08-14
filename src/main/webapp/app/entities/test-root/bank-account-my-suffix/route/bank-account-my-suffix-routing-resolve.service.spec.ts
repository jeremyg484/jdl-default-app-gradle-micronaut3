import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IBankAccountMySuffix, BankAccountMySuffix } from '../bank-account-my-suffix.model';
import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';

import { BankAccountMySuffixRoutingResolveService } from './bank-account-my-suffix-routing-resolve.service';

describe('BankAccountMySuffix routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BankAccountMySuffixRoutingResolveService;
  let service: BankAccountMySuffixService;
  let resultBankAccountMySuffix: IBankAccountMySuffix | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(BankAccountMySuffixRoutingResolveService);
    service = TestBed.inject(BankAccountMySuffixService);
    resultBankAccountMySuffix = undefined;
  });

  describe('resolve', () => {
    it('should return IBankAccountMySuffix returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankAccountMySuffix = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankAccountMySuffix).toEqual({ id: 123 });
    });

    it('should return new IBankAccountMySuffix if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankAccountMySuffix = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBankAccountMySuffix).toEqual(new BankAccountMySuffix());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BankAccountMySuffix })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBankAccountMySuffix = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBankAccountMySuffix).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
