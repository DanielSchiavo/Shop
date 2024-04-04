package br.com.danielschiavo.shop.models.cliente.cartao.validacoes;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.cartao.CadastrarCartaoDTO;

public interface ValidadorCadastrarNovoCartao {
	
	void validar(CadastrarCartaoDTO cartaoDTO, Cliente cliente);

}
