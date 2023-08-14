import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRegion, Region } from '../region.model';
import { RegionService } from '../service/region.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

@Component({
  selector: 'jhi-region-update',
  templateUrl: './region-update.component.html',
})
export class RegionUpdateComponent implements OnInit {
  isSaving = false;

  countriesSharedCollection: ICountry[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    country: [],
  });

  constructor(
    protected regionService: RegionService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ region }) => {
      this.updateForm(region);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const region = this.createFromForm();
    if (region.id !== undefined) {
      this.subscribeToSaveResponse(this.regionService.update(region));
    } else {
      this.subscribeToSaveResponse(this.regionService.create(region));
    }
  }

  trackCountryById(_index: number, item: ICountry): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(region: IRegion): void {
    this.editForm.patchValue({
      id: region.id,
      name: region.name,
      country: region.country,
    });

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing(this.countriesSharedCollection, region.country);
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('country')!.value))
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }

  protected createFromForm(): IRegion {
    return {
      ...new Region(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
