import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { GoldenBadgeService } from '../service/golden-badge.service';
import { IGoldenBadge, GoldenBadge } from '../golden-badge.model';
import { IIdentifier } from 'app/entities/identifier/identifier.model';
import { IdentifierService } from 'app/entities/identifier/service/identifier.service';

import { GoldenBadgeUpdateComponent } from './golden-badge-update.component';

describe('GoldenBadge Management Update Component', () => {
  let comp: GoldenBadgeUpdateComponent;
  let fixture: ComponentFixture<GoldenBadgeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let goldenBadgeService: GoldenBadgeService;
  let identifierService: IdentifierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [GoldenBadgeUpdateComponent],
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
      .overrideTemplate(GoldenBadgeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GoldenBadgeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    goldenBadgeService = TestBed.inject(GoldenBadgeService);
    identifierService = TestBed.inject(IdentifierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Identifier query and add missing value', () => {
      const goldenBadge: IGoldenBadge = { id: 456 };
      const iden: IIdentifier = { id: 96391 };
      goldenBadge.iden = iden;

      const identifierCollection: IIdentifier[] = [{ id: 11975 }];
      jest.spyOn(identifierService, 'query').mockReturnValue(of(new HttpResponse({ body: identifierCollection })));
      const additionalIdentifiers = [iden];
      const expectedCollection: IIdentifier[] = [...additionalIdentifiers, ...identifierCollection];
      jest.spyOn(identifierService, 'addIdentifierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ goldenBadge });
      comp.ngOnInit();

      expect(identifierService.query).toHaveBeenCalled();
      expect(identifierService.addIdentifierToCollectionIfMissing).toHaveBeenCalledWith(identifierCollection, ...additionalIdentifiers);
      expect(comp.identifiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const goldenBadge: IGoldenBadge = { id: 456 };
      const iden: IIdentifier = { id: 3574 };
      goldenBadge.iden = iden;

      activatedRoute.data = of({ goldenBadge });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(goldenBadge));
      expect(comp.identifiersSharedCollection).toContain(iden);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GoldenBadge>>();
      const goldenBadge = { id: 123 };
      jest.spyOn(goldenBadgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goldenBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: goldenBadge }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(goldenBadgeService.update).toHaveBeenCalledWith(goldenBadge);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GoldenBadge>>();
      const goldenBadge = new GoldenBadge();
      jest.spyOn(goldenBadgeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goldenBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: goldenBadge }));
      saveSubject.complete();

      // THEN
      expect(goldenBadgeService.create).toHaveBeenCalledWith(goldenBadge);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<GoldenBadge>>();
      const goldenBadge = { id: 123 };
      jest.spyOn(goldenBadgeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ goldenBadge });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(goldenBadgeService.update).toHaveBeenCalledWith(goldenBadge);
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
