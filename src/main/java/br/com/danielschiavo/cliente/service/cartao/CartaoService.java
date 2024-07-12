package br.com.danielschiavo.cliente.service.cartao;

import java.util.List;

import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.repository.CartaoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.cliente.service.cartao.validacoes.cadastrarcartao.ValidadorCadastrarCartao;
import lombok.Setter;

@Service
@Setter
public class CartaoService {

	@Autowired
	private CartaoRepository repository;
	
	@Autowired
	private List<ValidadorCadastrarCartao> validadores;
	
	@Transactional
	public void deletarCartaoPorId(Long cartaoId, Long clienteId) {
		repository.deleteByIdAndClienteId(cartaoId, clienteId);
	}
	
	public List<Cartao> pegarTodosCartoesPorClienteId(Long clienteId) {
		return repository.findAllByClienteId(clienteId)
				.orElseThrow(() -> new ValidacaoException("Cliente não possui nenhum cartão cadastrado"));
	}
	
	public Cartao pegarCartao(Long cartaoId, Long clienteId) {
		return repository.findByIdAndClienteId(cartaoId, clienteId)
				.orElseThrow(() -> new ValidacaoException("Cliente não tem cartão com o ID " + cartaoId));
	}

	@Transactional
	public Cartao cadastrarCartao(Long clienteId, Cartao cartao) {
		List<Cartao> cartoesJaCadastrados = pegarTodosCartoesPorClienteId(clienteId);
		validadores.forEach(v -> v.validar(cartao, cartoesJaCadastrados, clienteId));

		if (cartao.getCartaoPadrao()) {
			cartoesJaCadastrados.stream().filter(car -> car.getCartaoPadrao().equals(true)).forEach(car -> car.setCartaoPadrao(false));
		}

		cartao.setNomeBanco("Falta implementar API banco");
		cartoesJaCadastrados.add(cartao);
		repository.saveAll(cartoesJaCadastrados);
		return cartao;
	}
	
	@Transactional
	public void alterarCartao(Long cartaoId, Long clienteId) {
		List<Cartao> cartoes = pegarTodosCartoesPorClienteId(clienteId);

		Cartao cartao = cartoes.stream()
				.filter(c -> c.getId().equals(cartaoId))
				.findFirst().orElseThrow(() -> new ValidacaoException("Não existe cartão com esse id"));

		boolean novoEstadoCartaoPadrao = !cartao.getCartaoPadrao(); // Inverte o estado do cartão

		// Define o novo estado do cartão encontrado
		cartao.setCartaoPadrao(novoEstadoCartaoPadrao);

		// Define todos os outros cartões como não padrão, se necessário
		if (novoEstadoCartaoPadrao) {
			cartoes.stream()
					.filter(c -> !c.getId().equals(cartaoId) && c.getCartaoPadrao().equals(true))
					.forEach(c -> c.setCartaoPadrao(false));
		}

		repository.saveAll(cartoes);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------


	
}
