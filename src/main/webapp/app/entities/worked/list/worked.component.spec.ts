import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { WorkedService } from '../service/worked.service';

import { WorkedComponent } from './worked.component';

describe('Component Tests', () => {
  describe('Worked Management Component', () => {
    let comp: WorkedComponent;
    let fixture: ComponentFixture<WorkedComponent>;
    let service: WorkedService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [WorkedComponent],
      })
        .overrideTemplate(WorkedComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorkedComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(WorkedService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.workeds?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
