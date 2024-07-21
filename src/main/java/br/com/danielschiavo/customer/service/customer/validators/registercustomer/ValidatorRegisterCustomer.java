package br.com.danielschiavo.customer.service.customer.validators.registercustomer;

import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.model.entity.Customer;

public interface ValidatorRegisterCustomer {

    void validate(RegisterCustomerRequest request);
}
