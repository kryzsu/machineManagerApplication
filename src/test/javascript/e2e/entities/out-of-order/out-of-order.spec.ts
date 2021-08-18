import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { OutOfOrderComponentsPage, OutOfOrderDeleteDialog, OutOfOrderUpdatePage } from './out-of-order.page-object';

const expect = chai.expect;

describe('OutOfOrder e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let outOfOrderComponentsPage: OutOfOrderComponentsPage;
  let outOfOrderUpdatePage: OutOfOrderUpdatePage;
  let outOfOrderDeleteDialog: OutOfOrderDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load OutOfOrders', async () => {
    await navBarPage.goToEntity('out-of-order');
    outOfOrderComponentsPage = new OutOfOrderComponentsPage();
    await browser.wait(ec.visibilityOf(outOfOrderComponentsPage.title), 5000);
    expect(await outOfOrderComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.outOfOrder.home.title');
    await browser.wait(ec.or(ec.visibilityOf(outOfOrderComponentsPage.entities), ec.visibilityOf(outOfOrderComponentsPage.noResult)), 1000);
  });

  it('should load create OutOfOrder page', async () => {
    await outOfOrderComponentsPage.clickOnCreateButton();
    outOfOrderUpdatePage = new OutOfOrderUpdatePage();
    expect(await outOfOrderUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.outOfOrder.home.createOrEditLabel');
    await outOfOrderUpdatePage.cancel();
  });

  it('should create and save OutOfOrders', async () => {
    const nbButtonsBeforeCreate = await outOfOrderComponentsPage.countDeleteButtons();

    await outOfOrderComponentsPage.clickOnCreateButton();

    await promise.all([
      outOfOrderUpdatePage.setDateInput('2000-12-31'),
      outOfOrderUpdatePage.setDescriptionInput('description'),
      // outOfOrderUpdatePage.machineSelectLastOption(),
    ]);

    await outOfOrderUpdatePage.save();
    expect(await outOfOrderUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await outOfOrderComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last OutOfOrder', async () => {
    const nbButtonsBeforeDelete = await outOfOrderComponentsPage.countDeleteButtons();
    await outOfOrderComponentsPage.clickOnLastDeleteButton();

    outOfOrderDeleteDialog = new OutOfOrderDeleteDialog();
    expect(await outOfOrderDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.outOfOrder.delete.question');
    await outOfOrderDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(outOfOrderComponentsPage.title), 5000);

    expect(await outOfOrderComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
