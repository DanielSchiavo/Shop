package br.com.danielschiavo.cliente.service.validacoes;


import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;

import java.util.List;

public interface ValidadorCadastrarNovoCartao {
	
	void validar(CadastrarCartaoRequest request, List<Cartao> cartoes, Long clienteId);

}
