import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ViewDetailComponent } from './view-detail.component';

describe('Component Tests', () => {
  describe('View Management Detail Component', () => {
    let comp: ViewDetailComponent;
    let fixture: ComponentFixture<ViewDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ViewDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ view: { id: 123 } }) },
          },
        ],
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
        expect(comp.view).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
