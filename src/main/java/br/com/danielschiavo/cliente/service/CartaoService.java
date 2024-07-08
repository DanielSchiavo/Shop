package br.com.danielschiavo.cliente.service;

import java.util.List;

import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.repository.CartaoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shared.infra.security.SecurityService;
import br.com.danielschiavo.cliente.mapper.CartaoMapper;
import br.com.danielschiavo.cliente.service.validacoes.ValidadorCadastrarNovoCartao;
import lombok.Setter;

@Service
@Setter
public class CartaoService {

	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private List<ValidadorCadastrarNovoCartao> validadores;
	
	@Autowired
	private CartaoMapper cartaoMapper;
	
	@Transactional
	public void deletarCartaoPorId(Long cartaoId, Long clienteId) {
		cartaoRepository.deleteByIdAndClienteId(cartaoId, clienteId);
	}
	
	public List<Cartao> pegarTodosCartoesPorClienteId(Long clienteId) {
		return cartaoRepository.findAllByClienteId(clienteId)
				.orElseThrow(() -> new ValidacaoException("Cliente não possui nenhum cartão cadastrado"));
	}
	
	public Cartao pegarCartao(Long cartaoId, Long clienteId) {
		return cartaoRepository.findByIdAndClienteId(cartaoId, clienteId)
				.orElseThrow(() -> new ValidacaoException("Cliente não tem cartão com o ID " + cartaoId));
	}

	@Transactional
	public Cartao cadastrarCartao(CadastrarCartaoRequest request, Long clienteId) {
		List<Cartao> cartoes = pegarTodosCartoesPorClienteId(clienteId);
		validadores.forEach(v -> v.validar(request, cartoes, clienteId));
		
		Cartao cartao = cartaoMapper.toEntity(request, clienteId);

		if (request.cartaoPadrao()) {
			cartoes.stream().filter(car -> car.getCartaoPadrao().equals(true)).forEach(car -> car.setCartaoPadrao(false));
		}

		cartao.setNomeBanco("Falta implementar API banco");
		cartoes.add(cartao);
		cartaoRepository.saveAll(cartoes);
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
					.filter(c -> !c.getId().equals(cartaoId) && c.getCartaoPadrao())
					.forEach(c -> c.setCartaoPadrao(false));
		}

		cartaoRepository.saveAll(cartoes);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------


	
}
