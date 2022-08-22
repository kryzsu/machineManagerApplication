
const login = () => {
  cy.visit('http://localhost:9000/login')

  cy.get('#username').type('admin');
  cy.get('#password').type('admin');
  cy.get('button').click();
};

const logout = () => {
  cy.get('a#account-menu').click();
  cy.get('a#logout').click();
};

const createMaterial = () =>{
  cy.visit('http://localhost:9000/rawmaterial')

  cy.get('button.create-rawmaterial').click()
  cy.get('#field_grade').type('grade');
  cy.get('#field_name').type('name_' + new Date());
  cy.get('#field_dimension').type('dimension');
  cy.get('#field_coating').type('coating');
  cy.get('#field_supplier').type('supplier');

  cy.get('button#save-entity').click();
};

const createProduct = () =>{
  cy.visit('http://localhost:9000/product')

  cy.get('button.create-product').click()
  cy.get('#field_name').type('name_' + new Date());
  cy.get('#field_drawingNumber').type('drawingNumber');
  cy.get('#field_itemNumber').type('itemNumber');
  cy.get('#field_weight').type('123');

  //cy.get('button#save-entity').click();
};

describe('empty spec', () => {
  it('passes', () => {
    login();
    createMaterial();
    createProduct();
    logout();

  })
})
