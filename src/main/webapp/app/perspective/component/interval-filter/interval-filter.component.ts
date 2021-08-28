import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { defaultInterval, FilterInterval } from './filter-interval';
import { DATE_FORMAT } from '../../../config/input.constants';

@Component({
  selector: 'jhi-interval-filter',
  templateUrl: './interval-filter.component.html',
  styleUrls: ['./interval-filter.component.scss'],
})
export class IntervalFilterComponent implements OnInit {
  @Input()
  isLoading = false;

  @Output() refresh = new EventEmitter<FilterInterval>();

  filterForm = this.fb.group({
    startDate: [Validators.required],
    endDate: [Validators.required],
  });

  constructor(protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.filterForm.patchValue(defaultInterval);
  }

  loadPage(): void {
    this.refresh.emit({
      startDate: this.filterForm.value.startDate,
      endDate: this.filterForm.value.endDate,
    });
  }
}
