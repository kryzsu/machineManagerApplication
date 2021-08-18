import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOutOfOrder } from '../out-of-order.model';

@Component({
  selector: 'jhi-out-of-order-detail',
  templateUrl: './out-of-order-detail.component.html',
})
export class OutOfOrderDetailComponent implements OnInit {
  outOfOrder: IOutOfOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ outOfOrder }) => {
      this.outOfOrder = outOfOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
