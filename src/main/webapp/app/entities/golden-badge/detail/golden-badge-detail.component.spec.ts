import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GoldenBadgeDetailComponent } from './golden-badge-detail.component';

describe('GoldenBadge Management Detail Component', () => {
  let comp: GoldenBadgeDetailComponent;
  let fixture: ComponentFixture<GoldenBadgeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GoldenBadgeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ goldenBadge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GoldenBadgeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GoldenBadgeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load goldenBadge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.goldenBadge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
