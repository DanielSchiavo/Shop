package br.com.danielschiavo.shop.models.cartao.validacoes;

import br.com.danielschiavo.shop.models.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;

public interface ValidadorCadastrarNovoCartao {
	
	void validar(CadastrarCartaoDTO cartaoDTO, Cliente cliente);

}
