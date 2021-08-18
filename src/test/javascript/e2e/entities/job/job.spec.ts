import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { JobComponentsPage, JobDeleteDialog, JobUpdatePage } from './job.page-object';

const expect = chai.expect;

describe('Job e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let jobComponentsPage: JobComponentsPage;
  let jobUpdatePage: JobUpdatePage;
  let jobDeleteDialog: JobDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Jobs', async () => {
    await navBarPage.goToEntity('job');
    jobComponentsPage = new JobComponentsPage();
    await browser.wait(ec.visibilityOf(jobComponentsPage.title), 5000);
    expect(await jobComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.job.home.title');
    await browser.wait(ec.or(ec.visibilityOf(jobComponentsPage.entities), ec.visibilityOf(jobComponentsPage.noResult)), 1000);
  });

  it('should load create Job page', async () => {
    await jobComponentsPage.clickOnCreateButton();
    jobUpdatePage = new JobUpdatePage();
    expect(await jobUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.job.home.createOrEditLabel');
    await jobUpdatePage.cancel();
  });

  it('should create and save Jobs', async () => {
    const nbButtonsBeforeCreate = await jobComponentsPage.countDeleteButtons();

    await jobComponentsPage.clickOnCreateButton();

    await promise.all([
      jobUpdatePage.setEstimationInput('5'),
      jobUpdatePage.setProductCountInput('5'),
      jobUpdatePage.setStartDateInput('2000-12-31'),
      jobUpdatePage.setEndDateInput('2000-12-31'),
      jobUpdatePage.setFactInput('5'),
      jobUpdatePage.setOrderNumberInput('orderNumber'),
      // jobUpdatePage.productSelectLastOption(),
      jobUpdatePage.machineSelectLastOption(),
    ]);

    expect(await jobUpdatePage.getEstimationInput()).to.eq('5', 'Expected estimation value to be equals to 5');
    expect(await jobUpdatePage.getProductCountInput()).to.eq('5', 'Expected productCount value to be equals to 5');
    expect(await jobUpdatePage.getStartDateInput()).to.eq('2000-12-31', 'Expected startDate value to be equals to 2000-12-31');
    expect(await jobUpdatePage.getEndDateInput()).to.eq('2000-12-31', 'Expected endDate value to be equals to 2000-12-31');
    expect(await jobUpdatePage.getFactInput()).to.eq('5', 'Expected fact value to be equals to 5');
    expect(await jobUpdatePage.getOrderNumberInput()).to.eq('orderNumber', 'Expected OrderNumber value to be equals to orderNumber');

    await jobUpdatePage.save();
    expect(await jobUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await jobComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Job', async () => {
    const nbButtonsBeforeDelete = await jobComponentsPage.countDeleteButtons();
    await jobComponentsPage.clickOnLastDeleteButton();

    jobDeleteDialog = new JobDeleteDialog();
    expect(await jobDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.job.delete.question');
    await jobDeleteDialog.clickOnConfirmButton();

    expect(await jobComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
