package br.com.danielschiavo.customer.service.cliente.validacoes.cadastrarcliente;

import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.repository.CustomerRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class ValidarEmailJaExiste implements ValidadorCadastrarCliente {

    @Autowired
    private CustomerRepository repository;

    @Override
    public void validar(RegisterCustomerRequest request) {
        Customer probe = new Customer();
        probe.setEmail(request.email());

        Example<Customer> example = Example.of(probe);

        boolean exists = repository.exists(example);
        if (exists) {
            throw new ValidacaoException("Email já cadastrado");
        }
    }
}
