import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { HolidayComponentsPage, HolidayDeleteDialog, HolidayUpdatePage } from './holiday.page-object';

const expect = chai.expect;

describe('Holiday e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let holidayComponentsPage: HolidayComponentsPage;
  let holidayUpdatePage: HolidayUpdatePage;
  let holidayDeleteDialog: HolidayDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Holidays', async () => {
    await navBarPage.goToEntity('holiday');
    holidayComponentsPage = new HolidayComponentsPage();
    await browser.wait(ec.visibilityOf(holidayComponentsPage.title), 5000);
    expect(await holidayComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.holiday.home.title');
    await browser.wait(ec.or(ec.visibilityOf(holidayComponentsPage.entities), ec.visibilityOf(holidayComponentsPage.noResult)), 1000);
  });

  it('should load create Holiday page', async () => {
    await holidayComponentsPage.clickOnCreateButton();
    holidayUpdatePage = new HolidayUpdatePage();
    expect(await holidayUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.holiday.home.createOrEditLabel');
    await holidayUpdatePage.cancel();
  });

  it('should create and save Holidays', async () => {
    const nbButtonsBeforeCreate = await holidayComponentsPage.countDeleteButtons();

    await holidayComponentsPage.clickOnCreateButton();

    await promise.all([holidayUpdatePage.setDayInput('2000-12-31'), holidayUpdatePage.setCommentInput('comment')]);

    expect(await holidayUpdatePage.getDayInput()).to.eq('2000-12-31', 'Expected day value to be equals to 2000-12-31');
    expect(await holidayUpdatePage.getCommentInput()).to.eq('comment', 'Expected Comment value to be equals to comment');

    await holidayUpdatePage.save();
    expect(await holidayUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await holidayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Holiday', async () => {
    const nbButtonsBeforeDelete = await holidayComponentsPage.countDeleteButtons();
    await holidayComponentsPage.clickOnLastDeleteButton();

    holidayDeleteDialog = new HolidayDeleteDialog();
    expect(await holidayDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.holiday.delete.question');
    await holidayDeleteDialog.clickOnConfirmButton();

    expect(await holidayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
