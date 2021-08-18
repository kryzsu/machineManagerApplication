import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MachineManagerApplicationTestModule } from '../../../test.module';
import { OutOfOrderDetailComponent } from 'app/entities/out-of-order/out-of-order-detail.component';
import { OutOfOrder } from 'app/shared/model/out-of-order.model';

describe('Component Tests', () => {
  describe('OutOfOrder Management Detail Component', () => {
    let comp: OutOfOrderDetailComponent;
    let fixture: ComponentFixture<OutOfOrderDetailComponent>;
    const route = ({ data: of({ outOfOrder: new OutOfOrder(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MachineManagerApplicationTestModule],
        declarations: [OutOfOrderDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
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
        expect(comp.outOfOrder).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
