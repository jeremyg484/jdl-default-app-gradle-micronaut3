import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'bank',
        data: { pageTitle: 'jhipsterSampleApplicationApp.bank.home.title' },
        loadChildren: () => import('./bank/bank.module').then(m => m.BankModule),
      },
      {
        path: 'bank-account-my-suffix',
        data: { pageTitle: 'jhipsterSampleApplicationApp.testRootBankAccount.home.title' },
        loadChildren: () =>
          import('./test-root/bank-account-my-suffix/bank-account-my-suffix.module').then(m => m.BankAccountMySuffixModule),
      },
      {
        path: 'the-label',
        data: { pageTitle: 'jhipsterSampleApplicationApp.testRootTheLabel.home.title' },
        loadChildren: () => import('./test-root/the-label/the-label.module').then(m => m.TheLabelModule),
      },
      {
        path: 'operation',
        data: { pageTitle: 'jhipsterSampleApplicationApp.testRootOperation.home.title' },
        loadChildren: () => import('./test-root/operation/operation.module').then(m => m.OperationModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'jhipsterSampleApplicationApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'job-history',
        data: { pageTitle: 'jhipsterSampleApplicationApp.jobHistory.home.title' },
        loadChildren: () => import('./job-history/job-history.module').then(m => m.JobHistoryModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'jhipsterSampleApplicationApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'jhipsterSampleApplicationApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'jhipsterSampleApplicationApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'jhipsterSampleApplicationApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'golden-badge',
        data: { pageTitle: 'jhipsterSampleApplicationApp.goldenBadge.home.title' },
        loadChildren: () => import('./golden-badge/golden-badge.module').then(m => m.GoldenBadgeModule),
      },
      {
        path: 'silver-badge',
        data: { pageTitle: 'jhipsterSampleApplicationApp.silverBadge.home.title' },
        loadChildren: () => import('./silver-badge/silver-badge.module').then(m => m.SilverBadgeModule),
      },
      {
        path: 'identifier',
        data: { pageTitle: 'jhipsterSampleApplicationApp.identifier.home.title' },
        loadChildren: () => import('./identifier/identifier.module').then(m => m.IdentifierModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'jhipsterSampleApplicationApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'jhipsterSampleApplicationApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
