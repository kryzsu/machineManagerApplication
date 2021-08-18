import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IView, getViewIdentifier } from '../view.model';

export type EntityResponseType = HttpResponse<IView>;
export type EntityArrayResponseType = HttpResponse<IView[]>;

@Injectable({ providedIn: 'root' })
export class ViewService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/views');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(view: IView): Observable<EntityResponseType> {
    return this.http.post<IView>(this.resourceUrl, view, { observe: 'response' });
  }

  update(view: IView): Observable<EntityResponseType> {
    return this.http.put<IView>(`${this.resourceUrl}/${getViewIdentifier(view) as number}`, view, { observe: 'response' });
  }

  partialUpdate(view: IView): Observable<EntityResponseType> {
    return this.http.patch<IView>(`${this.resourceUrl}/${getViewIdentifier(view) as number}`, view, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IView>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IView[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addViewToCollectionIfMissing(viewCollection: IView[], ...viewsToCheck: (IView | null | undefined)[]): IView[] {
    const views: IView[] = viewsToCheck.filter(isPresent);
    if (views.length > 0) {
      const viewCollectionIdentifiers = viewCollection.map(viewItem => getViewIdentifier(viewItem)!);
      const viewsToAdd = views.filter(viewItem => {
        const viewIdentifier = getViewIdentifier(viewItem);
        if (viewIdentifier == null || viewCollectionIdentifiers.includes(viewIdentifier)) {
          return false;
        }
        viewCollectionIdentifiers.push(viewIdentifier);
        return true;
      });
      return [...viewsToAdd, ...viewCollection];
    }
    return viewCollection;
  }
}
