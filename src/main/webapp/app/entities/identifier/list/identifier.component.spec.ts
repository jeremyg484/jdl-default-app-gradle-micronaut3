import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { IdentifierService } from '../service/identifier.service';

import { IdentifierComponent } from './identifier.component';

describe('Identifier Management Component', () => {
  let comp: IdentifierComponent;
  let fixture: ComponentFixture<IdentifierComponent>;
  let service: IdentifierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [IdentifierComponent],
    })
      .overrideTemplate(IdentifierComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IdentifierComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(IdentifierService);

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
    expect(comp.identifiers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
