import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IdentifierService } from '../service/identifier.service';
import { IIdentifier, Identifier } from '../identifier.model';

import { IdentifierUpdateComponent } from './identifier-update.component';

describe('Identifier Management Update Component', () => {
  let comp: IdentifierUpdateComponent;
  let fixture: ComponentFixture<IdentifierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let identifierService: IdentifierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IdentifierUpdateComponent],
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
      .overrideTemplate(IdentifierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdentifierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    identifierService = TestBed.inject(IdentifierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const identifier: IIdentifier = { id: 456 };

      activatedRoute.data = of({ identifier });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(identifier));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Identifier>>();
      const identifier = { id: 123 };
      jest.spyOn(identifierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ identifier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: identifier }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(identifierService.update).toHaveBeenCalledWith(identifier);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Identifier>>();
      const identifier = new Identifier();
      jest.spyOn(identifierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ identifier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: identifier }));
      saveSubject.complete();

      // THEN
      expect(identifierService.create).toHaveBeenCalledWith(identifier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Identifier>>();
      const identifier = { id: 123 };
      jest.spyOn(identifierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ identifier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(identifierService.update).toHaveBeenCalledWith(identifier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
