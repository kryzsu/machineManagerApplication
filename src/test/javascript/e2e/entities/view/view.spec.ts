import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ViewComponentsPage, ViewDeleteDialog, ViewUpdatePage } from './view.page-object';

const expect = chai.expect;

describe('View e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let viewComponentsPage: ViewComponentsPage;
  let viewUpdatePage: ViewUpdatePage;
  let viewDeleteDialog: ViewDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Views', async () => {
    await navBarPage.goToEntity('view');
    viewComponentsPage = new ViewComponentsPage();
    await browser.wait(ec.visibilityOf(viewComponentsPage.title), 5000);
    expect(await viewComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.view.home.title');
    await browser.wait(ec.or(ec.visibilityOf(viewComponentsPage.entities), ec.visibilityOf(viewComponentsPage.noResult)), 1000);
  });

  it('should load create View page', async () => {
    await viewComponentsPage.clickOnCreateButton();
    viewUpdatePage = new ViewUpdatePage();
    expect(await viewUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.view.home.createOrEditLabel');
    await viewUpdatePage.cancel();
  });

  it('should create and save Views', async () => {
    const nbButtonsBeforeCreate = await viewComponentsPage.countDeleteButtons();

    await viewComponentsPage.clickOnCreateButton();

    await promise.all([
      viewUpdatePage.setNameInput('name'),
      // viewUpdatePage.machineSelectLastOption(),
    ]);

    expect(await viewUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');

    await viewUpdatePage.save();
    expect(await viewUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await viewComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last View', async () => {
    const nbButtonsBeforeDelete = await viewComponentsPage.countDeleteButtons();
    await viewComponentsPage.clickOnLastDeleteButton();

    viewDeleteDialog = new ViewDeleteDialog();
    expect(await viewDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.view.delete.question');
    await viewDeleteDialog.clickOnConfirmButton();

    expect(await viewComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
