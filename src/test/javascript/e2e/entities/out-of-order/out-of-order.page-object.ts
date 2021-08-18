import { element, by, ElementFinder } from 'protractor';

export class OutOfOrderComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-out-of-order div table .btn-danger'));
  title = element.all(by.css('jhi-out-of-order div h2#page-heading span')).first();
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

export class OutOfOrderUpdatePage {
  pageTitle = element(by.id('jhi-out-of-order-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  dateInput = element(by.id('field_date'));
  descriptionInput = element(by.id('field_description'));

  machineSelect = element(by.id('field_machine'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setDateInput(date: string): Promise<void> {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput(): Promise<string> {
    return await this.dateInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async machineSelectLastOption(): Promise<void> {
    await this.machineSelect.all(by.tagName('option')).last().click();
  }

  async machineSelectOption(option: string): Promise<void> {
    await this.machineSelect.sendKeys(option);
  }

  getMachineSelect(): ElementFinder {
    return this.machineSelect;
  }

  async getMachineSelectedOption(): Promise<string> {
    return await this.machineSelect.element(by.css('option:checked')).getText();
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

export class OutOfOrderDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-outOfOrder-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-outOfOrder'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
