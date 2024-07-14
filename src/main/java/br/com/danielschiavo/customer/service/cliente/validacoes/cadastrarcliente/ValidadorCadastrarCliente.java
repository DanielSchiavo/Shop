package br.com.danielschiavo.customer.service.cliente.validacoes.cadastrarcliente;

import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;

public interface ValidadorCadastrarCliente {

    void validar(RegisterCustomerRequest request);
}
