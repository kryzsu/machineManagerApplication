import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ProductComponentsPage, ProductDeleteDialog, ProductUpdatePage } from './product.page-object';

const expect = chai.expect;

describe('Product e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let productComponentsPage: ProductComponentsPage;
  let productUpdatePage: ProductUpdatePage;
  let productDeleteDialog: ProductDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Products', async () => {
    await navBarPage.goToEntity('product');
    productComponentsPage = new ProductComponentsPage();
    await browser.wait(ec.visibilityOf(productComponentsPage.title), 5000);
    expect(await productComponentsPage.getTitle()).to.eq('machineManagerApplicationApp.product.home.title');
    await browser.wait(ec.or(ec.visibilityOf(productComponentsPage.entities), ec.visibilityOf(productComponentsPage.noResult)), 1000);
  });

  it('should load create Product page', async () => {
    await productComponentsPage.clickOnCreateButton();
    productUpdatePage = new ProductUpdatePage();
    expect(await productUpdatePage.getPageTitle()).to.eq('machineManagerApplicationApp.product.home.createOrEditLabel');
    await productUpdatePage.cancel();
  });

  it('should create and save Products', async () => {
    const nbButtonsBeforeCreate = await productComponentsPage.countDeleteButtons();

    await productComponentsPage.clickOnCreateButton();

    await promise.all([productUpdatePage.setNameInput('name'), productUpdatePage.setCommentInput('comment')]);

    expect(await productUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await productUpdatePage.getCommentInput()).to.eq('comment', 'Expected Comment value to be equals to comment');

    await productUpdatePage.save();
    expect(await productUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await productComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Product', async () => {
    const nbButtonsBeforeDelete = await productComponentsPage.countDeleteButtons();
    await productComponentsPage.clickOnLastDeleteButton();

    productDeleteDialog = new ProductDeleteDialog();
    expect(await productDeleteDialog.getDialogTitle()).to.eq('machineManagerApplicationApp.product.delete.question');
    await productDeleteDialog.clickOnConfirmButton();

    expect(await productComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});