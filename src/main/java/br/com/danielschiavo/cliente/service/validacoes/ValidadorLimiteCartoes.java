package br.com.danielschiavo.cliente.service.validacoes;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.repository.CartaoRepository;
import br.com.danielschiavo.cliente.service.CartaoService;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ValidadorLimiteCartoes implements ValidadorCadastrarNovoCartao {

	@Override
	public void validar(CadastrarCartaoRequest request, List<Cartao> cartoes, Long clienteId) {
		long quantidadeCartoes = cartoes.size();

		if (quantidadeCartoes == 10) {
			throw new ValidacaoException("Limite de quantidade de cartões por cliente atingido, que são 10 cartões");
		}
	}

}
