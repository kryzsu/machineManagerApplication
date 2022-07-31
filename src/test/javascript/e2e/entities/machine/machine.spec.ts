import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { MachineComponentsPage, MachineDeleteDialog, MachineUpdatePage } from './machine.page-object';

const expect = chai.expect;

describe('Machine e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let machineComponentsPage: MachineComponentsPage;
  let machineUpdatePage: MachineUpdatePage;
  let machineDeleteDialog: MachineDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Machines', async () => {
    await navBarPage.goToEntity('machine');
    machineComponentsPage = new MachineComponentsPage();
    await browser.wait(ec.visibilityOf(machineComponentsPage.title), 5000);
    expect(await machineComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.machine.home.title');
    await browser.wait(ec.or(ec.visibilityOf(machineComponentsPage.entities), ec.visibilityOf(machineComponentsPage.noResult)), 1000);
  });

  it('should load create Machine page', async () => {
    await machineComponentsPage.clickOnCreateButton();
    machineUpdatePage = new MachineUpdatePage();
    expect(await machineUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.machine.home.createOrEditLabel');
    await machineUpdatePage.cancel();
  });

  it('should create and save Machines', async () => {
    const nbButtonsBeforeCreate = await machineComponentsPage.countDeleteButtons();

    await machineComponentsPage.clickOnCreateButton();

    await promise.all([
      machineUpdatePage.setNameInput('name'),
      machineUpdatePage.setDescriptionInput('description'),
      machineUpdatePage.runningJobSelectLastOption(),
    ]);

    await machineUpdatePage.save();
    expect(await machineUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await machineComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Machine', async () => {
    const nbButtonsBeforeDelete = await machineComponentsPage.countDeleteButtons();
    await machineComponentsPage.clickOnLastDeleteButton();

    machineDeleteDialog = new MachineDeleteDialog();
    expect(await machineDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.machine.delete.question');
    await machineDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(machineComponentsPage.title), 5000);

    expect(await machineComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
