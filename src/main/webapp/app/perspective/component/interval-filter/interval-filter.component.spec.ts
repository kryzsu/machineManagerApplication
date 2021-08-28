import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IntervalFilterComponent } from './interval-filter.component';

describe('IntervalFilterComponent', () => {
  let component: IntervalFilterComponent;
  let fixture: ComponentFixture<IntervalFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IntervalFilterComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IntervalFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
