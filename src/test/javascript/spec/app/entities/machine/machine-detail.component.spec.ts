import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MachineManagerApplicationTestModule } from '../../../test.module';
import { MachineDetailComponent } from 'app/entities/machine/machine-detail.component';
import { Machine } from 'app/shared/model/machine.model';

describe('Component Tests', () => {
  describe('Machine Management Detail Component', () => {
    let comp: MachineDetailComponent;
    let fixture: ComponentFixture<MachineDetailComponent>;
    const route = ({ data: of({ machine: new Machine(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MachineManagerApplicationTestModule],
        declarations: [MachineDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MachineDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MachineDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load machine on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.machine).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
