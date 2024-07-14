package br.com.danielschiavo.customer.service.cliente.validators.registercustomer;

import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.model.entity.Customer;

public interface ValidatorRegisterCustomer {

    void validate(Customer customer);
}
