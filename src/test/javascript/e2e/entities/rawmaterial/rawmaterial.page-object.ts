import { element, by, ElementFinder } from 'protractor';

export class RawmaterialComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-rawmaterial div table .btn-danger'));
  title = element.all(by.css('jhi-rawmaterial div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class RawmaterialUpdatePage {
  pageTitle = element(by.id('jhi-rawmaterial-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  commentInput = element(by.id('field_comment'));
  gradeInput = element(by.id('field_grade'));
  dimensionInput = element(by.id('field_dimension'));
  coatingInput = element(by.id('field_coating'));
  supplierInput = element(by.id('field_supplier'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setCommentInput(comment: string): Promise<void> {
    await this.commentInput.sendKeys(comment);
  }

  async getCommentInput(): Promise<string> {
    return await this.commentInput.getAttribute('value');
  }

  async setGradeInput(grade: string): Promise<void> {
    await this.gradeInput.sendKeys(grade);
  }

  async getGradeInput(): Promise<string> {
    return await this.gradeInput.getAttribute('value');
  }

  async setDimensionInput(dimension: string): Promise<void> {
    await this.dimensionInput.sendKeys(dimension);
  }

  async getDimensionInput(): Promise<string> {
    return await this.dimensionInput.getAttribute('value');
  }

  async setCoatingInput(coating: string): Promise<void> {
    await this.coatingInput.sendKeys(coating);
  }

  async getCoatingInput(): Promise<string> {
    return await this.coatingInput.getAttribute('value');
  }

  async setSupplierInput(supplier: string): Promise<void> {
    await this.supplierInput.sendKeys(supplier);
  }

  async getSupplierInput(): Promise<string> {
    return await this.supplierInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class RawmaterialDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-rawmaterial-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-rawmaterial'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
