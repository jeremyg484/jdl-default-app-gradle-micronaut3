import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CountryService } from '../service/country.service';
import { ICountry, Country } from '../country.model';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';

import { CountryUpdateComponent } from './country-update.component';

describe('Country Management Update Component', () => {
  let comp: CountryUpdateComponent;
  let fixture: ComponentFixture<CountryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countryService: CountryService;
  let locationService: LocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CountryUpdateComponent],
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
      .overrideTemplate(CountryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countryService = TestBed.inject(CountryService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Location query and add missing value', () => {
      const country: ICountry = { id: 456 };
      const location: ILocation = { id: 70520 };
      country.location = location;

      const locationCollection: ILocation[] = [{ id: 89023 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [location];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(locationCollection, ...additionalLocations);
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const country: ICountry = { id: 456 };
      const location: ILocation = { id: 95181 };
      country.location = location;

      activatedRoute.data = of({ country });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(country));
      expect(comp.locationsSharedCollection).toContain(location);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = { id: 123 };
      jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: country }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countryService.update).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = new Country();
      jest.spyOn(countryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: country }));
      saveSubject.complete();

      // THEN
      expect(countryService.create).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Country>>();
      const country = { id: 123 };
      jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ country });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countryService.update).toHaveBeenCalledWith(country);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLocationById', () => {
      it('Should return tracked Location primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLocationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
