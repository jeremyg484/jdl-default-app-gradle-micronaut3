import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGoldenBadge } from '../golden-badge.model';

@Component({
  selector: 'jhi-golden-badge-detail',
  templateUrl: './golden-badge-detail.component.html',
})
export class GoldenBadgeDetailComponent implements OnInit {
  goldenBadge: IGoldenBadge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ goldenBadge }) => {
      this.goldenBadge = goldenBadge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
