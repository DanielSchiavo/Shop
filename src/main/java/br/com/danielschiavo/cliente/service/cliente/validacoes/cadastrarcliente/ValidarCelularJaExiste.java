package br.com.danielschiavo.cliente.service.cliente.validacoes.cadastrarcliente;

import br.com.danielschiavo.cliente.dto.request.cliente.CadastrarClienteRequest;
import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.repository.ClienteRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class ValidarCelularJaExiste implements ValidadorCadastrarCliente {

    @Autowired
    private ClienteRepository repository;

    @Override
    public void validar(CadastrarClienteRequest request) {
        Cliente probe = new Cliente();
        probe.setCelular(request.celular());

        Example<Cliente> example = Example.of(probe);

        boolean exists = repository.exists(example);
        if (exists) {
            throw new ValidacaoException("Numero de celular já cadastrado");
        }
    }
}
