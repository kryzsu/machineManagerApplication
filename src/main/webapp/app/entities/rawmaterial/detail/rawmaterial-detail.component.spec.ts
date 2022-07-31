import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RawmaterialDetailComponent } from './rawmaterial-detail.component';

describe('Component Tests', () => {
  describe('Rawmaterial Management Detail Component', () => {
    let comp: RawmaterialDetailComponent;
    let fixture: ComponentFixture<RawmaterialDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RawmaterialDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ rawmaterial: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RawmaterialDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RawmaterialDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load rawmaterial on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rawmaterial).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
