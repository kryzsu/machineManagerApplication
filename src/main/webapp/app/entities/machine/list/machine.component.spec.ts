import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MachineService } from '../service/machine.service';

import { MachineComponent } from './machine.component';

describe('Component Tests', () => {
  describe('Machine Management Component', () => {
    let comp: MachineComponent;
    let fixture: ComponentFixture<MachineComponent>;
    let service: MachineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MachineComponent],
      })
        .overrideTemplate(MachineComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MachineComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MachineService);

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
      expect(comp.machines?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
