package br.com.danielschiavo.customer.service.customer.validators.registercustomer;

import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.repository.CustomerRepository;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class ValidateCpfAlreadyExist implements ValidatorRegisterCustomer {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void validate(RegisterCustomerRequest request) {
        Customer probe = new Customer();
        probe.setCpf(request.cpf());

        Example<Customer> example = Example.of(probe);

        boolean exists = repository.exists(example);
        if (exists) {
            throw new ValidationException("CPF already exist");
        }
    }
}
