package br.com.danielschiavo.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.validacoes.ValidadorCadastrarNovoCartao;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.CartaoRepository;

@Service
public class CartaoService {
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@Autowired
	private List<ValidadorCadastrarNovoCartao> validadores;

	@Transactional
	public MostrarCartaoDTO cadastrarNovoCartaoPorIdToken(CadastrarCartaoDTO cartaoDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
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
	
	public Page<MostrarCartaoDTO> pegarCartoesClientePorIdToken(Pageable pageable) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Page<Cartao> pageCartao = cartaoRepository.findAllByCliente(cliente, pageable);
		return pageCartao.map(MostrarCartaoDTO::converterParaMostrarCartaoDTO);
	}
	
	@Transactional
	public void deletarCartaoPorIdToken(Long id) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		var cartao = cartaoRepository.findById(id).orElseThrow();
		
		cliente.getCartoes().forEach(c -> {
			if (c.getId() == id) {
				cartaoRepository.delete(cartao);
				return;
			}
		});
		
		throw new ValidacaoException("O Cartão de id número " + id + " não é do cliente logado");
		
	}

	@Transactional
	public void alterarCartaoPadraoPorIdToken(Long id) {
		Optional<Cartao> optionalCartao = cartaoRepository.findById(id);
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		if (optionalCartao.isPresent()) {
			var cartao = optionalCartao.get();
			cliente.getCartoes().forEach(c -> {
				if (c.getId() == id) { //verifica se o cartão é do cliente mesmo
					if (cartao.getCartaoPadrao() == false) {
						Optional<Cartao> optionalCartaoTrue = cartaoRepository.findByCartaoPadraoTrue();
						if (optionalCartaoTrue.isPresent()) {
							Cartao cartaoTrue = optionalCartaoTrue.get();
							cartaoTrue.setCartaoPadrao(false);
							cartaoRepository.save(cartaoTrue);
						}
						cartao.setCartaoPadrao(true);
					}
					else {
						cartao.setCartaoPadrao(false);
					}
					cartaoRepository.save(cartao);
				}
			});
		}
		else {
			throw new ValidacaoException("ID do cartão de número: " + id + " não existe");
		}
		
	}

	public Cartao verificarSeCartaoExistePorIdCartaoECliente(Long idCartao) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Optional<Cartao> optionalCartao = cartaoRepository.findByIdAndCliente(idCartao, cliente);
		
		if (optionalCartao.isPresent()) {
			return optionalCartao.get();
		}
		else {
			throw new ValidacaoException("Não existe o cartão de ID número " + idCartao + " para o cliente de ID número " + cliente.getId());
		}
	}
	
}
