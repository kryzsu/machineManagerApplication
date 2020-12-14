import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MachineManagerApplicationTestModule } from '../../../test.module';
import { MachineComponent } from 'app/entities/machine/machine.component';
import { MachineService } from 'app/entities/machine/machine.service';
import { Machine } from 'app/shared/model/machine.model';

describe('Component Tests', () => {
  describe('Machine Management Component', () => {
    let comp: MachineComponent;
    let fixture: ComponentFixture<MachineComponent>;
    let service: MachineService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MachineManagerApplicationTestModule],
        declarations: [MachineComponent],
      })
        .overrideTemplate(MachineComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MachineComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MachineService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Machine(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.machines && comp.machines[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
