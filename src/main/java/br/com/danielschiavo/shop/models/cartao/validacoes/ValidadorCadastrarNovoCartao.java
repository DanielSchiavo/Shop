package br.com.danielschiavo.shop.models.cartao.validacoes;

import br.com.danielschiavo.shop.models.cartao.CartaoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;

public interface ValidadorCadastrarNovoCartao {
	
	void validar(CartaoDTO cartaoDTO, Cliente cliente);

}
