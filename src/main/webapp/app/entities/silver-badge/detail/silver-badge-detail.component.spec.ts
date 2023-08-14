import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SilverBadgeDetailComponent } from './silver-badge-detail.component';

describe('SilverBadge Management Detail Component', () => {
  let comp: SilverBadgeDetailComponent;
  let fixture: ComponentFixture<SilverBadgeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SilverBadgeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ silverBadge: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SilverBadgeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SilverBadgeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load silverBadge on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.silverBadge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
