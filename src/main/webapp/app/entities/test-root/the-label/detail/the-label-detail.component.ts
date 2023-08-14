import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITheLabel } from '../the-label.model';

@Component({
  selector: 'jhi-the-label-detail',
  templateUrl: './the-label-detail.component.html',
})
export class TheLabelDetailComponent implements OnInit {
  theLabel: ITheLabel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ theLabel }) => {
      this.theLabel = theLabel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
