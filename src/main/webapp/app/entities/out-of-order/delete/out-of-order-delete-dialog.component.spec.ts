jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { OutOfOrderService } from '../service/out-of-order.service';

import { OutOfOrderDeleteDialogComponent } from './out-of-order-delete-dialog.component';

describe('Component Tests', () => {
  describe('OutOfOrder Management Delete Component', () => {
    let comp: OutOfOrderDeleteDialogComponent;
    let fixture: ComponentFixture<OutOfOrderDeleteDialogComponent>;
    let service: OutOfOrderService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OutOfOrderDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(OutOfOrderDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OutOfOrderDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OutOfOrderService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
