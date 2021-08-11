jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IWorked, Worked } from '../worked.model';
import { WorkedService } from '../service/worked.service';

import { WorkedRoutingResolveService } from './worked-routing-resolve.service';

describe('Service Tests', () => {
  describe('Worked routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: WorkedRoutingResolveService;
    let service: WorkedService;
    let resultWorked: IWorked | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(WorkedRoutingResolveService);
      service = TestBed.inject(WorkedService);
      resultWorked = undefined;
    });

    describe('resolve', () => {
      it('should return IWorked returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorked = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorked).toEqual({ id: 123 });
      });

      it('should return new IWorked if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorked = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultWorked).toEqual(new Worked());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Worked })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultWorked = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultWorked).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
