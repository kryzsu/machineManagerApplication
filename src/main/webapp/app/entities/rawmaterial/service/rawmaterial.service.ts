import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRawmaterial, getRawmaterialIdentifier } from '../rawmaterial.model';

export type EntityResponseType = HttpResponse<IRawmaterial>;
export type EntityArrayResponseType = HttpResponse<IRawmaterial[]>;

@Injectable({ providedIn: 'root' })
export class RawmaterialService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rawmaterials');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rawmaterial: IRawmaterial): Observable<EntityResponseType> {
    return this.http.post<IRawmaterial>(this.resourceUrl, rawmaterial, { observe: 'response' });
  }

  update(rawmaterial: IRawmaterial): Observable<EntityResponseType> {
    return this.http.put<IRawmaterial>(`${this.resourceUrl}/${getRawmaterialIdentifier(rawmaterial) as number}`, rawmaterial, {
      observe: 'response',
    });
  }

  partialUpdate(rawmaterial: IRawmaterial): Observable<EntityResponseType> {
    return this.http.patch<IRawmaterial>(`${this.resourceUrl}/${getRawmaterialIdentifier(rawmaterial) as number}`, rawmaterial, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRawmaterial>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRawmaterial[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRawmaterialToCollectionIfMissing(
    rawmaterialCollection: IRawmaterial[],
    ...rawmaterialsToCheck: (IRawmaterial | null | undefined)[]
  ): IRawmaterial[] {
    const rawmaterials: IRawmaterial[] = rawmaterialsToCheck.filter(isPresent);
    if (rawmaterials.length > 0) {
      const rawmaterialCollectionIdentifiers = rawmaterialCollection.map(rawmaterialItem => getRawmaterialIdentifier(rawmaterialItem)!);
      const rawmaterialsToAdd = rawmaterials.filter(rawmaterialItem => {
        const rawmaterialIdentifier = getRawmaterialIdentifier(rawmaterialItem);
        if (rawmaterialIdentifier == null || rawmaterialCollectionIdentifiers.includes(rawmaterialIdentifier)) {
          return false;
        }
        rawmaterialCollectionIdentifiers.push(rawmaterialIdentifier);
        return true;
      });
      return [...rawmaterialsToAdd, ...rawmaterialCollection];
    }
    return rawmaterialCollection;
  }
}
