package br.com.danielschiavo.cliente.service.cliente.validacoes.cadastrarcliente;

import br.com.danielschiavo.cliente.dto.request.cliente.CadastrarClienteRequest;

public interface ValidadorCadastrarCliente {

    void validar(CadastrarClienteRequest request);
}
