import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOutOfOrder, getOutOfOrderIdentifier } from '../out-of-order.model';

export type EntityResponseType = HttpResponse<IOutOfOrder>;
export type EntityArrayResponseType = HttpResponse<IOutOfOrder[]>;

@Injectable({ providedIn: 'root' })
export class OutOfOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/out-of-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(outOfOrder: IOutOfOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(outOfOrder);
    return this.http
      .post<IOutOfOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(outOfOrder: IOutOfOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(outOfOrder);
    return this.http
      .put<IOutOfOrder>(`${this.resourceUrl}/${getOutOfOrderIdentifier(outOfOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(outOfOrder: IOutOfOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(outOfOrder);
    return this.http
      .patch<IOutOfOrder>(`${this.resourceUrl}/${getOutOfOrderIdentifier(outOfOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IOutOfOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOutOfOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOutOfOrderToCollectionIfMissing(
    outOfOrderCollection: IOutOfOrder[],
    ...outOfOrdersToCheck: (IOutOfOrder | null | undefined)[]
  ): IOutOfOrder[] {
    const outOfOrders: IOutOfOrder[] = outOfOrdersToCheck.filter(isPresent);
    if (outOfOrders.length > 0) {
      const outOfOrderCollectionIdentifiers = outOfOrderCollection.map(outOfOrderItem => getOutOfOrderIdentifier(outOfOrderItem)!);
      const outOfOrdersToAdd = outOfOrders.filter(outOfOrderItem => {
        const outOfOrderIdentifier = getOutOfOrderIdentifier(outOfOrderItem);
        if (outOfOrderIdentifier == null || outOfOrderCollectionIdentifiers.includes(outOfOrderIdentifier)) {
          return false;
        }
        outOfOrderCollectionIdentifiers.push(outOfOrderIdentifier);
        return true;
      });
      return [...outOfOrdersToAdd, ...outOfOrderCollection];
    }
    return outOfOrderCollection;
  }

  protected convertDateFromClient(outOfOrder: IOutOfOrder): IOutOfOrder {
    return Object.assign({}, outOfOrder, {
      date: outOfOrder.date?.isValid() ? outOfOrder.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((outOfOrder: IOutOfOrder) => {
        outOfOrder.date = outOfOrder.date ? dayjs(outOfOrder.date) : undefined;
      });
    }
    return res;
  }
}
