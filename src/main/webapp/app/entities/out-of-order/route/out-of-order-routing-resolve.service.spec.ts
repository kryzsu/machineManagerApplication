jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOutOfOrder, OutOfOrder } from '../out-of-order.model';
import { OutOfOrderService } from '../service/out-of-order.service';

import { OutOfOrderRoutingResolveService } from './out-of-order-routing-resolve.service';

describe('Service Tests', () => {
  describe('OutOfOrder routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OutOfOrderRoutingResolveService;
    let service: OutOfOrderService;
    let resultOutOfOrder: IOutOfOrder | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OutOfOrderRoutingResolveService);
      service = TestBed.inject(OutOfOrderService);
      resultOutOfOrder = undefined;
    });

    describe('resolve', () => {
      it('should return IOutOfOrder returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOutOfOrder = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOutOfOrder).toEqual({ id: 123 });
      });

      it('should return new IOutOfOrder if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOutOfOrder = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOutOfOrder).toEqual(new OutOfOrder());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as OutOfOrder })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOutOfOrder = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOutOfOrder).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
