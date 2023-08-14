import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SilverBadgeService } from '../service/silver-badge.service';

import { SilverBadgeComponent } from './silver-badge.component';

describe('SilverBadge Management Component', () => {
  let comp: SilverBadgeComponent;
  let fixture: ComponentFixture<SilverBadgeComponent>;
  let service: SilverBadgeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SilverBadgeComponent],
    })
      .overrideTemplate(SilverBadgeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SilverBadgeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(SilverBadgeService);

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
    expect(comp.silverBadges?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
