package br.com.danielschiavo.cliente.service.cartao.validacoes.cadastrarcartao;


import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;

import java.util.List;

public interface ValidadorCadastrarCartao {
	
	void validar(CadastrarCartaoRequest request, List<Cartao> cartoes, Long clienteId);

}
