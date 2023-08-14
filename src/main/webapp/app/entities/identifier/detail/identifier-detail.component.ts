import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIdentifier } from '../identifier.model';

@Component({
  selector: 'jhi-identifier-detail',
  templateUrl: './identifier-detail.component.html',
})
export class IdentifierDetailComponent implements OnInit {
  identifier: IIdentifier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ identifier }) => {
      this.identifier = identifier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
