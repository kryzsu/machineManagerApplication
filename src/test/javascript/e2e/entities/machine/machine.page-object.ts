import { element, by, ElementFinder } from 'protractor';

export class MachineComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-machine div table .btn-danger'));
  title = element.all(by.css('jhi-machine div h2#page-heading span')).first();
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

export class MachineUpdatePage {
  pageTitle = element(by.id('jhi-machine-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  createDateTimeInput = element(by.id('field_createDateTime'));
  updateDateTimeInput = element(by.id('field_updateDateTime'));
  deletedInput = element(by.id('field_deleted'));

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

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setCreateDateTimeInput(createDateTime: string): Promise<void> {
    await this.createDateTimeInput.sendKeys(createDateTime);
  }

  async getCreateDateTimeInput(): Promise<string> {
    return await this.createDateTimeInput.getAttribute('value');
  }

  async setUpdateDateTimeInput(updateDateTime: string): Promise<void> {
    await this.updateDateTimeInput.sendKeys(updateDateTime);
  }

  async getUpdateDateTimeInput(): Promise<string> {
    return await this.updateDateTimeInput.getAttribute('value');
  }

  getDeletedInput(): ElementFinder {
    return this.deletedInput;
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

export class MachineDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-machine-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-machine'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
