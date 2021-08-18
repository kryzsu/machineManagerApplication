import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OutOfOrderDetailComponent } from './out-of-order-detail.component';

describe('Component Tests', () => {
  describe('OutOfOrder Management Detail Component', () => {
    let comp: OutOfOrderDetailComponent;
    let fixture: ComponentFixture<OutOfOrderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OutOfOrderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ outOfOrder: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OutOfOrderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OutOfOrderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load outOfOrder on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.outOfOrder).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
