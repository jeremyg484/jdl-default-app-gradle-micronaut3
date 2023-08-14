import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepartment } from '../department.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-department-detail',
  templateUrl: './department-detail.component.html',
})
export class DepartmentDetailComponent implements OnInit {
  department: IDepartment | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ department }) => {
      this.department = department;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
