import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'machine',
        data: { pageTitle: 'machineManagerApplicationApp.machine.home.title' },
        loadChildren: () => import('./machine/machine.module').then(m => m.MachineModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'machineManagerApplicationApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'machineManagerApplicationApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'out-of-order',
        data: { pageTitle: 'machineManagerApplicationApp.outOfOrder.home.title' },
        loadChildren: () => import('./out-of-order/out-of-order.module').then(m => m.OutOfOrderModule),
      },
      {
        path: 'view',
        data: { pageTitle: 'machineManagerApplicationApp.view.home.title' },
        loadChildren: () => import('./view/view.module').then(m => m.ViewModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'machineManagerApplicationApp.customer.home.title' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
