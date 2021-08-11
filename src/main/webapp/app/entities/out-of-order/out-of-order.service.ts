import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IOutOfOrder } from 'app/shared/model/out-of-order.model';

type EntityResponseType = HttpResponse<IOutOfOrder>;
type EntityArrayResponseType = HttpResponse<IOutOfOrder[]>;

@Injectable({ providedIn: 'root' })
export class OutOfOrderService {
  public resourceUrl = SERVER_API_URL + 'api/out-of-orders';

  constructor(protected http: HttpClient) {}

  create(outOfOrder: IOutOfOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(outOfOrder);
    return this.http
      .post<IOutOfOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(outOfOrder: IOutOfOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(outOfOrder);
    return this.http
      .put<IOutOfOrder>(this.resourceUrl, copy, { observe: 'response' })
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

  protected convertDateFromClient(outOfOrder: IOutOfOrder): IOutOfOrder {
    const copy: IOutOfOrder = Object.assign({}, outOfOrder, {
      date: outOfOrder.date && outOfOrder.date.isValid() ? outOfOrder.date.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((outOfOrder: IOutOfOrder) => {
        outOfOrder.date = outOfOrder.date ? moment(outOfOrder.date) : undefined;
      });
    }
    return res;
  }
}
