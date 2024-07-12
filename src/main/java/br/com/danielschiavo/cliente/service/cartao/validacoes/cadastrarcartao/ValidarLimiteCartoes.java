package br.com.danielschiavo.cliente.service.cartao.validacoes.cadastrarcartao;

import java.util.List;

import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidarLimiteCartoes implements ValidadorCadastrarCartao {

	@Override
	public void validar(Cartao cartao, List<Cartao> cartoesJaCadastrados, Long clienteId) {
		long quantidadeCartoes = cartoesJaCadastrados.size();

		if (quantidadeCartoes == 10) {
			throw new ValidacaoException("Limite de quantidade de cartões por cliente atingido, que são 10 cartões");
		}
	}

}
