import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { RawmaterialComponentsPage, RawmaterialDeleteDialog, RawmaterialUpdatePage } from './rawmaterial.page-object';

const expect = chai.expect;

describe('Rawmaterial e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rawmaterialComponentsPage: RawmaterialComponentsPage;
  let rawmaterialUpdatePage: RawmaterialUpdatePage;
  let rawmaterialDeleteDialog: RawmaterialDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Rawmaterials', async () => {
    await navBarPage.goToEntity('rawmaterial');
    rawmaterialComponentsPage = new RawmaterialComponentsPage();
    await browser.wait(ec.visibilityOf(rawmaterialComponentsPage.title), 5000);
    expect(await rawmaterialComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.rawmaterial.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(rawmaterialComponentsPage.entities), ec.visibilityOf(rawmaterialComponentsPage.noResult)),
      1000
    );
  });

  it('should load create Rawmaterial page', async () => {
    await rawmaterialComponentsPage.clickOnCreateButton();
    rawmaterialUpdatePage = new RawmaterialUpdatePage();
    expect(await rawmaterialUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.rawmaterial.home.createOrEditLabel');
    await rawmaterialUpdatePage.cancel();
  });

  it('should create and save Rawmaterials', async () => {
    const nbButtonsBeforeCreate = await rawmaterialComponentsPage.countDeleteButtons();

    await rawmaterialComponentsPage.clickOnCreateButton();

    await promise.all([rawmaterialUpdatePage.setNameInput('name'), rawmaterialUpdatePage.setCommentInput('comment')]);

    await rawmaterialUpdatePage.save();
    expect(await rawmaterialUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await rawmaterialComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Rawmaterial', async () => {
    const nbButtonsBeforeDelete = await rawmaterialComponentsPage.countDeleteButtons();
    await rawmaterialComponentsPage.clickOnLastDeleteButton();

    rawmaterialDeleteDialog = new RawmaterialDeleteDialog();
    expect(await rawmaterialDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.rawmaterial.delete.question');
    await rawmaterialDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(rawmaterialComponentsPage.title), 5000);

    expect(await rawmaterialComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
