import { element, by, ElementFinder } from 'protractor';

export class ProductComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-product div table .btn-danger'));
  title = element.all(by.css('jhi-product div h2#page-heading span')).first();
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

export class ProductUpdatePage {
  pageTitle = element(by.id('jhi-product-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  drawingNumberInput = element(by.id('field_drawingNumber'));
  itemNumberInput = element(by.id('field_itemNumber'));
  weightInput = element(by.id('field_weight'));
  commentInput = element(by.id('field_comment'));
  drawingInput = element(by.id('file_drawing'));

  rawmaterialSelect = element(by.id('field_rawmaterial'));

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

  async setDrawingNumberInput(drawingNumber: string): Promise<void> {
    await this.drawingNumberInput.sendKeys(drawingNumber);
  }

  async getDrawingNumberInput(): Promise<string> {
    return await this.drawingNumberInput.getAttribute('value');
  }

  async setItemNumberInput(itemNumber: string): Promise<void> {
    await this.itemNumberInput.sendKeys(itemNumber);
  }

  async getItemNumberInput(): Promise<string> {
    return await this.itemNumberInput.getAttribute('value');
  }

  async setWeightInput(weight: string): Promise<void> {
    await this.weightInput.sendKeys(weight);
  }

  async getWeightInput(): Promise<string> {
    return await this.weightInput.getAttribute('value');
  }

  async setCommentInput(comment: string): Promise<void> {
    await this.commentInput.sendKeys(comment);
  }

  async getCommentInput(): Promise<string> {
    return await this.commentInput.getAttribute('value');
  }

  async setDrawingInput(drawing: string): Promise<void> {
    await this.drawingInput.sendKeys(drawing);
  }

  async getDrawingInput(): Promise<string> {
    return await this.drawingInput.getAttribute('value');
  }

  async rawmaterialSelectLastOption(): Promise<void> {
    await this.rawmaterialSelect.all(by.tagName('option')).last().click();
  }

  async rawmaterialSelectOption(option: string): Promise<void> {
    await this.rawmaterialSelect.sendKeys(option);
  }

  getRawmaterialSelect(): ElementFinder {
    return this.rawmaterialSelect;
  }

  async getRawmaterialSelectedOption(): Promise<string> {
    return await this.rawmaterialSelect.element(by.css('option:checked')).getText();
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

export class ProductDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-product-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-product'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
