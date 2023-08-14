import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SilverBadgeService } from '../service/silver-badge.service';
import { ISilverBadge, SilverBadge } from '../silver-badge.model';
import { IIdentifier } from 'app/entities/identifier/identifier.model';
import { IdentifierService } from 'app/entities/identifier/service/identifier.service';

import { SilverBadgeUpdateComponent } from './silver-badge-update.component';

describe('SilverBadge Management Update Component', () => {
  let comp: SilverBadgeUpdateComponent;
  let fixture: ComponentFixture<SilverBadgeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let silverBadgeService: SilverBadgeService;
  let identifierService: IdentifierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SilverBadgeUpdateComponent],
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
      .overrideTemplate(SilverBadgeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SilverBadgeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    silverBadgeService = TestBed.inject(SilverBadgeService);
    identifierService = TestBed.inject(IdentifierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Identifier query and add missing value', () => {
      const silverBadge: ISilverBadge = { id: 456 };
      const iden: IIdentifier = { id: 74729 };
      silverBadge.iden = iden;

      const identifierCollection: IIdentifier[] = [{ id: 1170 }];
      jest.spyOn(identifierService, 'query').mockReturnValue(of(new HttpResponse({ body: identifierCollection })));
      const additionalIdentifiers = [iden];
      const expectedCollection: IIdentifier[] = [...additionalIdentifiers, ...identifierCollection];
      jest.spyOn(identifierService, 'addIdentifierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ silverBadge });
      comp.ngOnInit();

      expect(identifierService.query).toHaveBeenCalled();
      expect(identifierService.addIdentifierToCollectionIfMissing).toHaveBeenCalledWith(identifierCollection, ...additionalIdentifiers);
      expect(comp.identifiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const silverBadge: ISilverBadge = { id: 456 };
      const iden: IIdentifier = { id: 32489 };
      silverBadge.iden = iden;

      activatedRoute.data = of({ silverBadge });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(silverBadge));
      expect(comp.identifiersSharedCollection).toContain(iden);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SilverBadge>>();
      const silverBadge = { id: 123 };
      jest.spyOn(silverBadgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ silverBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: silverBadge }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(silverBadgeService.update).toHaveBeenCalledWith(silverBadge);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SilverBadge>>();
      const silverBadge = new SilverBadge();
      jest.spyOn(silverBadgeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ silverBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: silverBadge }));
      saveSubject.complete();

      // THEN
      expect(silverBadgeService.create).toHaveBeenCalledWith(silverBadge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SilverBadge>>();
      const silverBadge = { id: 123 };
      jest.spyOn(silverBadgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ silverBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(silverBadgeService.update).toHaveBeenCalledWith(silverBadge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackIdentifierById', () => {
      it('Should return tracked Identifier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIdentifierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
