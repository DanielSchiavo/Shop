package br.com.danielschiavo.shop.models.cliente.cartao.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.repositories.cliente.CartaoRepository;

@Service
public class ValidadorVerificarNumeroCartaoJaCadastrado implements ValidadorCadastrarNovoCartao {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Override
	public void validar(CadastrarCartaoDTO cartaoDTO, Cliente cliente) {
		cartaoRepository.findByNumeroCartaoAndTipoCartaoAndCliente(cartaoDTO.numeroCartao(), cartaoDTO.tipoCartao(), cliente)
					.ifPresent(cartao -> { 
						throw new ValidacaoException("O usuário já possui um cartão com esse número e tipoCartao cadastrado");
						});
	}

}
