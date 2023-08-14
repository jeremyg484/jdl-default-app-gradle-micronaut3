import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IdentifierDetailComponent } from './identifier-detail.component';

describe('Identifier Management Detail Component', () => {
  let comp: IdentifierDetailComponent;
  let fixture: ComponentFixture<IdentifierDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IdentifierDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ identifier: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IdentifierDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IdentifierDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load identifier on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.identifier).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
