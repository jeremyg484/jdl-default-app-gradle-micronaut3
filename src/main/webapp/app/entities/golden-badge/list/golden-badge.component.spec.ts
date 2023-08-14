import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GoldenBadgeService } from '../service/golden-badge.service';

import { GoldenBadgeComponent } from './golden-badge.component';

describe('GoldenBadge Management Component', () => {
  let comp: GoldenBadgeComponent;
  let fixture: ComponentFixture<GoldenBadgeComponent>;
  let service: GoldenBadgeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GoldenBadgeComponent],
    })
      .overrideTemplate(GoldenBadgeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GoldenBadgeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GoldenBadgeService);

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
    expect(comp.goldenBadges?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
