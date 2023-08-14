import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BankService } from '../service/bank.service';

import { BankComponent } from './bank.component';

describe('Bank Management Component', () => {
  let comp: BankComponent;
  let fixture: ComponentFixture<BankComponent>;
  let service: BankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BankComponent],
    })
      .overrideTemplate(BankComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BankService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.banks?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
