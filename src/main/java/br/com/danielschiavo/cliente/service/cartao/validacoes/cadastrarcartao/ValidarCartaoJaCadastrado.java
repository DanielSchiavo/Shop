package br.com.danielschiavo.cliente.service.cartao.validacoes.cadastrarcartao;

import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.repository.CartaoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ValidarCartaoJaCadastrado implements ValidadorCadastrarCartao {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Override
	public void validar(Cartao cartao, List<Cartao> cartoesJaCadastrados, Long clienteId) {
		boolean match = cartoesJaCadastrados.stream()
				.anyMatch(car -> cartao.getNumeroCartao().equals(car.getNumeroCartao()) && cartao.getTipoCartao().equals(car.getTipoCartao()));
		if (match) {
			throw new ValidacaoException("O usuário já possui um cartão com esse número e tipoCartao cadastrado");
		}
	}

}
