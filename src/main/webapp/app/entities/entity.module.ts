import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'machine',
        loadChildren: () => import('./machine/machine.module').then(m => m.MachineManagerApplicationMachineModule),
      },
      {
        path: 'job',
        loadChildren: () => import('./job/job.module').then(m => m.MachineManagerApplicationJobModule),
      },
      {
        path: 'out-of-order',
        loadChildren: () => import('./out-of-order/out-of-order.module').then(m => m.MachineManagerApplicationOutOfOrderModule),
      },
      {
        path: 'product',
        loadChildren: () => import('./product/product.module').then(m => m.MachineManagerApplicationProductModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class MachineManagerApplicationEntityModule {}
