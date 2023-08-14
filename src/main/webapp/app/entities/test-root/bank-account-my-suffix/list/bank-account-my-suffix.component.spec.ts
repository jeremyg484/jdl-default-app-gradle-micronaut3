import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BankAccountMySuffixService } from '../service/bank-account-my-suffix.service';

import { BankAccountMySuffixComponent } from './bank-account-my-suffix.component';

describe('BankAccountMySuffix Management Component', () => {
  let comp: BankAccountMySuffixComponent;
  let fixture: ComponentFixture<BankAccountMySuffixComponent>;
  let service: BankAccountMySuffixService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BankAccountMySuffixComponent],
    })
      .overrideTemplate(BankAccountMySuffixComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BankAccountMySuffixComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BankAccountMySuffixService);

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
    expect(comp.bankAccounts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
