import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MachineManagerApplicationTestModule } from '../../../test.module';
import { ViewDetailComponent } from 'app/entities/view/view-detail.component';
import { View } from 'app/shared/model/view.model';

describe('Component Tests', () => {
  describe('View Management Detail Component', () => {
    let comp: ViewDetailComponent;
    let fixture: ComponentFixture<ViewDetailComponent>;
    const route = ({ data: of({ view: new View(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MachineManagerApplicationTestModule],
        declarations: [ViewDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ViewDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ViewDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load view on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.view).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
