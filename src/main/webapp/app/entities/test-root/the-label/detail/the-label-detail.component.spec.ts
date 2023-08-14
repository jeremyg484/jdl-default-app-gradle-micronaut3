import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TheLabelDetailComponent } from './the-label-detail.component';

describe('TheLabel Management Detail Component', () => {
  let comp: TheLabelDetailComponent;
  let fixture: ComponentFixture<TheLabelDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TheLabelDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ theLabel: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TheLabelDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TheLabelDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load theLabel on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.theLabel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
