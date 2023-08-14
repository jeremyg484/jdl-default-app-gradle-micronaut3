import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TheLabelService } from '../service/the-label.service';
import { ITheLabel, TheLabel } from '../the-label.model';

import { TheLabelUpdateComponent } from './the-label-update.component';

describe('TheLabel Management Update Component', () => {
  let comp: TheLabelUpdateComponent;
  let fixture: ComponentFixture<TheLabelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let theLabelService: TheLabelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TheLabelUpdateComponent],
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
      .overrideTemplate(TheLabelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TheLabelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    theLabelService = TestBed.inject(TheLabelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const theLabel: ITheLabel = { id: 456 };

      activatedRoute.data = of({ theLabel });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(theLabel));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TheLabel>>();
      const theLabel = { id: 123 };
      jest.spyOn(theLabelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ theLabel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: theLabel }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(theLabelService.update).toHaveBeenCalledWith(theLabel);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TheLabel>>();
      const theLabel = new TheLabel();
      jest.spyOn(theLabelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ theLabel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: theLabel }));
      saveSubject.complete();

      // THEN
      expect(theLabelService.create).toHaveBeenCalledWith(theLabel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TheLabel>>();
      const theLabel = { id: 123 };
      jest.spyOn(theLabelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ theLabel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(theLabelService.update).toHaveBeenCalledWith(theLabel);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
