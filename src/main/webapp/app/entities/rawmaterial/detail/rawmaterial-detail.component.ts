import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRawmaterial } from '../rawmaterial.model';

@Component({
  selector: 'jhi-rawmaterial-detail',
  templateUrl: './rawmaterial-detail.component.html',
})
export class RawmaterialDetailComponent implements OnInit {
  rawmaterial: IRawmaterial | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rawmaterial }) => {
      this.rawmaterial = rawmaterial;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
