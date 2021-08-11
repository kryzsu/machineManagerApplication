import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WorkedDetailComponent } from './worked-detail.component';

describe('Component Tests', () => {
  describe('Worked Management Detail Component', () => {
    let comp: WorkedDetailComponent;
    let fixture: ComponentFixture<WorkedDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [WorkedDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ worked: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(WorkedDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorkedDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load worked on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.worked).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
