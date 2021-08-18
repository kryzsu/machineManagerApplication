import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IView } from '../view.model';

@Component({
  selector: 'jhi-view-detail',
  templateUrl: './view-detail.component.html',
})
export class ViewDetailComponent implements OnInit {
  view: IView | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ view }) => {
      this.view = view;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
