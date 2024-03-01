package br.com.danielschiavo.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.CartaoDTO;
import br.com.danielschiavo.shop.models.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.validacoes.ValidadorCadastrarNovoCartao;
import br.com.danielschiavo.shop.repositories.CartaoRepository;
import br.com.danielschiavo.shop.repositories.ClienteRepository;

@Service
public class CartaoService {
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private TokenJWTService tokenJWTService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private List<ValidadorCadastrarNovoCartao> validadores;

	@Transactional
	public MostrarCartaoDTO cadastrarNovoCartao(CartaoDTO cartaoDTO) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		validadores.forEach(v -> v.validar(cartaoDTO, cliente));
		
		var novoCartao = new Cartao(cartaoDTO);
		novoCartao.setCliente(cliente);
		novoCartao.setNomeBanco("Falta implementar API banco");
		
		if (cartaoDTO.cartaoPadrao() == false) {
			if (!cartaoRepository.existsByClienteAndCartaoPadraoTrue(cliente)) {
				novoCartao.setCartaoPadrao(true);
			}
		}
		
		cartaoRepository.save(novoCartao);
		
		return new MostrarCartaoDTO(novoCartao);
	}
	
	public Page<MostrarCartaoDTO> pegarCartoesCliente(Pageable pageable) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		 Page<Cartao> pageCartao = cartaoRepository.findAllByCliente(cliente, pageable);
		return pageCartao.map(MostrarCartaoDTO::converterParaMostrarCartaoDTO);
	}
	
	@Transactional
	public void deletarCartao(Long id) {
		var cartao = cartaoRepository.findById(id).orElseThrow();
		
		cartaoRepository.delete(cartao);
	}

	@Transactional
	public void alterarCartaoPadrao(Long id) {
		Optional<Cartao> optionalCartao = cartaoRepository.findById(id);
		
		if (optionalCartao.isPresent()) {
			var cartao = optionalCartao.get();
			if (cartao.getCartaoPadrao() == false) {
				Cartao cartaoTrue = cartaoRepository.findByCartaoPadraoTrue().orElseThrow();
				cartaoTrue.setCartaoPadrao(false);
				cartaoRepository.save(cartaoTrue);
				cartao.setCartaoPadrao(true);
			}
			else {
				throw new ValidacaoException("O cartão não pode ser definido como cartaoPadrao = false porque não existe outro cartão definido como cartão padrão (cartaoPadrao = true). Por favor, defina pelo menos um cartão já cadastrado como cartaoPadrao = true ou cadastre esse como cartaoPadrao = true.");
			}
			cartaoRepository.save(cartao);
		}
		else {
			throw new ValidacaoException("ID do cartão de número: " + id + " não existe");
		}
		
	}

	public Cartao verificarSeCartaoExistePorIdCartaoECliente(Long idCartao) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		Optional<Cartao> optionalCartao = cartaoRepository.findByIdAndCliente(idCartao, cliente);
		
		if (optionalCartao.isPresent()) {
			return optionalCartao.get();
		}
		else {
			throw new ValidacaoException("Não existe o cartão de ID número " + idCartao + " para o cliente de ID número " + idCliente);
		}
	}
	
}
