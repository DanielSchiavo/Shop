package br.com.danielschiavo.shop.models.cartao.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.CartaoRepository;

@Service
public class ValidadorCartaoPadraoTrue implements ValidadorCadastrarNovoCartao {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Override
	public void validar(CadastrarCartaoDTO cartaoDTO, Cliente cliente) {
		if (cartaoDTO.cartaoPadrao() == true) {
			var optionalCartao = cartaoRepository.findByClienteAndCartaoPadraoTrue(cliente);
			
			if (optionalCartao.isPresent()) {
				var cartao = optionalCartao.get();
				cartao.setCartaoPadrao(false);
				cartaoRepository.save(cartao);
			}
		}
	}

}
