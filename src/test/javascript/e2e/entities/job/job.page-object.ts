import { element, by, ElementFinder } from 'protractor';

export class JobComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-job div table .btn-danger'));
  title = element.all(by.css('jhi-job div h2#page-heading span')).first();
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

export class JobUpdatePage {
  pageTitle = element(by.id('jhi-job-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  customerNameInput = element(by.id('field_customerName'));
  daysInput = element(by.id('field_days'));
  productNameInput = element(by.id('field_productName'));
  countInput = element(by.id('field_count'));
  productTypeInput = element(by.id('field_productType'));
  commentInput = element(by.id('field_comment'));
  createDateTimeInput = element(by.id('field_createDateTime'));
  updateDateTimeInput = element(by.id('field_updateDateTime'));
  deletedInput = element(by.id('field_deleted'));
  inProgressInput = element(by.id('field_inProgress'));
  daysDoneInput = element(by.id('field_daysDone'));

  workedSelect = element(by.id('field_worked'));
  machineSelect = element(by.id('field_machine'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setCustomerNameInput(customerName: string): Promise<void> {
    await this.customerNameInput.sendKeys(customerName);
  }

  async getCustomerNameInput(): Promise<string> {
    return await this.customerNameInput.getAttribute('value');
  }

  async setDaysInput(days: string): Promise<void> {
    await this.daysInput.sendKeys(days);
  }

  async getDaysInput(): Promise<string> {
    return await this.daysInput.getAttribute('value');
  }

  async setProductNameInput(productName: string): Promise<void> {
    await this.productNameInput.sendKeys(productName);
  }

  async getProductNameInput(): Promise<string> {
    return await this.productNameInput.getAttribute('value');
  }

  async setCountInput(count: string): Promise<void> {
    await this.countInput.sendKeys(count);
  }

  async getCountInput(): Promise<string> {
    return await this.countInput.getAttribute('value');
  }

  async setProductTypeInput(productType: string): Promise<void> {
    await this.productTypeInput.sendKeys(productType);
  }

  async getProductTypeInput(): Promise<string> {
    return await this.productTypeInput.getAttribute('value');
  }

  async setCommentInput(comment: string): Promise<void> {
    await this.commentInput.sendKeys(comment);
  }

  async getCommentInput(): Promise<string> {
    return await this.commentInput.getAttribute('value');
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

  getInProgressInput(): ElementFinder {
    return this.inProgressInput;
  }

  async setDaysDoneInput(daysDone: string): Promise<void> {
    await this.daysDoneInput.sendKeys(daysDone);
  }

  async getDaysDoneInput(): Promise<string> {
    return await this.daysDoneInput.getAttribute('value');
  }

  async workedSelectLastOption(): Promise<void> {
    await this.workedSelect.all(by.tagName('option')).last().click();
  }

  async workedSelectOption(option: string): Promise<void> {
    await this.workedSelect.sendKeys(option);
  }

  getWorkedSelect(): ElementFinder {
    return this.workedSelect;
  }

  async getWorkedSelectedOption(): Promise<string> {
    return await this.workedSelect.element(by.css('option:checked')).getText();
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

export class JobDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-job-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-job'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
