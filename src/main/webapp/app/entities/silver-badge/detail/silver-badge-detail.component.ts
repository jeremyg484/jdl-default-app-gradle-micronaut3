import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISilverBadge } from '../silver-badge.model';

@Component({
  selector: 'jhi-silver-badge-detail',
  templateUrl: './silver-badge-detail.component.html',
})
export class SilverBadgeDetailComponent implements OnInit {
  silverBadge: ISilverBadge | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ silverBadge }) => {
      this.silverBadge = silverBadge;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
