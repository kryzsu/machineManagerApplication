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
  estimationInput = element(by.id('field_estimation'));
  productCountInput = element(by.id('field_productCount'));
  startDateInput = element(by.id('field_startDate'));
  endDateInput = element(by.id('field_endDate'));
  factInput = element(by.id('field_fact'));
  orderNumberInput = element(by.id('field_orderNumber'));
  drawingNumberInput = element(by.id('field_drawingNumber'));
  drawingInput = element(by.id('file_drawing'));
  worknumberInput = element(by.id('field_worknumber'));

  productSelect = element(by.id('field_product'));
  machineSelect = element(by.id('field_machine'));
  customerSelect = element(by.id('field_customer'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setEstimationInput(estimation: string): Promise<void> {
    await this.estimationInput.sendKeys(estimation);
  }

  async getEstimationInput(): Promise<string> {
    return await this.estimationInput.getAttribute('value');
  }

  async setProductCountInput(productCount: string): Promise<void> {
    await this.productCountInput.sendKeys(productCount);
  }

  async getProductCountInput(): Promise<string> {
    return await this.productCountInput.getAttribute('value');
  }

  async setStartDateInput(startDate: string): Promise<void> {
    await this.startDateInput.sendKeys(startDate);
  }

  async getStartDateInput(): Promise<string> {
    return await this.startDateInput.getAttribute('value');
  }

  async setEndDateInput(endDate: string): Promise<void> {
    await this.endDateInput.sendKeys(endDate);
  }

  async getEndDateInput(): Promise<string> {
    return await this.endDateInput.getAttribute('value');
  }

  async setFactInput(fact: string): Promise<void> {
    await this.factInput.sendKeys(fact);
  }

  async getFactInput(): Promise<string> {
    return await this.factInput.getAttribute('value');
  }

  async setOrderNumberInput(orderNumber: string): Promise<void> {
    await this.orderNumberInput.sendKeys(orderNumber);
  }

  async getOrderNumberInput(): Promise<string> {
    return await this.orderNumberInput.getAttribute('value');
  }

  async setDrawingNumberInput(drawingNumber: string): Promise<void> {
    await this.drawingNumberInput.sendKeys(drawingNumber);
  }

  async getDrawingNumberInput(): Promise<string> {
    return await this.drawingNumberInput.getAttribute('value');
  }

  async setDrawingInput(drawing: string): Promise<void> {
    await this.drawingInput.sendKeys(drawing);
  }

  async getDrawingInput(): Promise<string> {
    return await this.drawingInput.getAttribute('value');
  }

  async setWorknumberInput(worknumber: string): Promise<void> {
    await this.worknumberInput.sendKeys(worknumber);
  }

  async getWorknumberInput(): Promise<string> {
    return await this.worknumberInput.getAttribute('value');
  }

  async productSelectLastOption(): Promise<void> {
    await this.productSelect.all(by.tagName('option')).last().click();
  }

  async productSelectOption(option: string): Promise<void> {
    await this.productSelect.sendKeys(option);
  }

  getProductSelect(): ElementFinder {
    return this.productSelect;
  }

  async getProductSelectedOption(): Promise<string> {
    return await this.productSelect.element(by.css('option:checked')).getText();
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

  async customerSelectLastOption(): Promise<void> {
    await this.customerSelect.all(by.tagName('option')).last().click();
  }

  async customerSelectOption(option: string): Promise<void> {
    await this.customerSelect.sendKeys(option);
  }

  getCustomerSelect(): ElementFinder {
    return this.customerSelect;
  }

  async getCustomerSelectedOption(): Promise<string> {
    return await this.customerSelect.element(by.css('option:checked')).getText();
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
