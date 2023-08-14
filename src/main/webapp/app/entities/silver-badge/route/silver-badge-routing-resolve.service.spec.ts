import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ISilverBadge, SilverBadge } from '../silver-badge.model';
import { SilverBadgeService } from '../service/silver-badge.service';

import { SilverBadgeRoutingResolveService } from './silver-badge-routing-resolve.service';

describe('SilverBadge routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SilverBadgeRoutingResolveService;
  let service: SilverBadgeService;
  let resultSilverBadge: ISilverBadge | undefined;

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
    routingResolveService = TestBed.inject(SilverBadgeRoutingResolveService);
    service = TestBed.inject(SilverBadgeService);
    resultSilverBadge = undefined;
  });

  describe('resolve', () => {
    it('should return ISilverBadge returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSilverBadge = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSilverBadge).toEqual({ id: 123 });
    });

    it('should return new ISilverBadge if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSilverBadge = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSilverBadge).toEqual(new SilverBadge());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SilverBadge })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSilverBadge = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSilverBadge).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
