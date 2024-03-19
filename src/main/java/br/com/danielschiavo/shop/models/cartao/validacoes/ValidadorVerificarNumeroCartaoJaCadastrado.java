package br.com.danielschiavo.shop.models.cartao.validacoes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.CartaoRepository;

@Service
public class ValidadorVerificarNumeroCartaoJaCadastrado implements ValidadorCadastrarNovoCartao {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Override
	public void validar(CadastrarCartaoDTO cartaoDTO, Cliente cliente) {
		String numeroCartao = cartaoDTO.numeroCartao();
		List<Cartao> listaCartoes = cartaoRepository.findByNumeroCartao(numeroCartao);
		
		if (listaCartoes.size() == 1) {
			Cartao cartao = listaCartoes.get(0);
			if (cartao.getTipoCartao() == cartaoDTO.tipoCartao()) {
				throw new ValidacaoException("O usuário já possui um cartão com esse número e ele também é do tipo " + cartaoDTO.tipoCartao().toString());
			}
		}
		
		if (listaCartoes.size() == 2) {
			throw new ValidacaoException("O usuário já possui um cartão com esse número e ele também é do tipo " + cartaoDTO.tipoCartao().toString());
		}
	}

}
