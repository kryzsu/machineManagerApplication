import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorked } from '../worked.model';

@Component({
  selector: 'jhi-worked-detail',
  templateUrl: './worked-detail.component.html',
})
export class WorkedDetailComponent implements OnInit {
  worked: IWorked | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ worked }) => {
      this.worked = worked;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
