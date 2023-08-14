import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BankDetailComponent } from './bank-detail.component';

describe('Bank Management Detail Component', () => {
  let comp: BankDetailComponent;
  let fixture: ComponentFixture<BankDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BankDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ bank: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BankDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BankDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load bank on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.bank).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
