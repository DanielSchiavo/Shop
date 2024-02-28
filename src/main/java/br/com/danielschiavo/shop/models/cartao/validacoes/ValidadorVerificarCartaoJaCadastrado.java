package br.com.danielschiavo.shop.models.cartao.validacoes;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.CartaoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.CartaoRepository;

@Service
public class ValidadorVerificarCartaoJaCadastrado implements ValidadorCadastrarNovoCartao {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Override
	public void validar(CartaoDTO cartaoDTO, Cliente cliente) {
		String numeroCartao = cartaoDTO.numeroCartao();
		
		Optional<Cartao> optionalCartao = cartaoRepository.findByNumeroCartao(numeroCartao);
		
		if (optionalCartao.isPresent()) {
			throw new ValidacaoException("O usuário já possui um cartão com esse número");
		}

	}

}
