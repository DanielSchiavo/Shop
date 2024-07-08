package br.com.danielschiavo.cliente.service.validacoes;

import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;
import br.com.danielschiavo.cliente.repository.CartaoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ValidadorCartaoJaCadastrado implements ValidadorCadastrarNovoCartao {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Override
	public void validar(CadastrarCartaoRequest request, List<Cartao> cartoes, Long clienteId) {
		cartaoRepository.findByNumeroCartaoAndTipoCartaoAndClienteId(request.numeroCartao(), request.tipoCartao(), clienteId)
					.ifPresent(cartao -> { 
						throw new ValidacaoException("O usuário já possui um cartão com esse número e tipoCartao cadastrado");
						});
	}

}
