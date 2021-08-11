import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { WorkedComponentsPage, WorkedDeleteDialog, WorkedUpdatePage } from './worked.page-object';

const expect = chai.expect;

describe('Worked e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let workedComponentsPage: WorkedComponentsPage;
  let workedUpdatePage: WorkedUpdatePage;
  let workedDeleteDialog: WorkedDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Workeds', async () => {
    await navBarPage.goToEntity('worked');
    workedComponentsPage = new WorkedComponentsPage();
    await browser.wait(ec.visibilityOf(workedComponentsPage.title), 5000);
    expect(await workedComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.worked.home.title');
    await browser.wait(ec.or(ec.visibilityOf(workedComponentsPage.entities), ec.visibilityOf(workedComponentsPage.noResult)), 1000);
  });

  it('should load create Worked page', async () => {
    await workedComponentsPage.clickOnCreateButton();
    workedUpdatePage = new WorkedUpdatePage();
    expect(await workedUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.worked.home.createOrEditLabel');
    await workedUpdatePage.cancel();
  });

  it('should create and save Workeds', async () => {
    const nbButtonsBeforeCreate = await workedComponentsPage.countDeleteButtons();

    await workedComponentsPage.clickOnCreateButton();

    await promise.all([workedUpdatePage.setDayInput('2000-12-31'), workedUpdatePage.setCommentInput('comment')]);

    await workedUpdatePage.save();
    expect(await workedUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await workedComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Worked', async () => {
    const nbButtonsBeforeDelete = await workedComponentsPage.countDeleteButtons();
    await workedComponentsPage.clickOnLastDeleteButton();

    workedDeleteDialog = new WorkedDeleteDialog();
    expect(await workedDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.worked.delete.question');
    await workedDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(workedComponentsPage.title), 5000);

    expect(await workedComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
