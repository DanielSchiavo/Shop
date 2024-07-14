package br.com.danielschiavo.customer.service.cliente.validators.registercustomer;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.repository.CustomerRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class ValidateCellphoneNumberAlreadyExist implements ValidatorRegisterCustomer {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void validate(Customer customer) {
        Customer probe = new Customer();
        probe.setCellphoneNumber(customer.getCellphoneNumber());

        Example<Customer> example = Example.of(probe);

        boolean exists = repository.exists(example);
        if (exists) {
            throw new ValidationException("CellphoneNumber already exist");
        }
    }
}
