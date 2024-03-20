package br.com.danielschiavo.shop.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void deletarCartaoPorIdToken(Long id) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Iterator<Cartao> iteratorCartao = cliente.getCartoes().iterator();
		while (iteratorCartao.hasNext()) {
			Cartao cartao = iteratorCartao.next();
			if (cartao.getId() == id) {
				iteratorCartao.remove();
				cartaoRepository.delete(cartao);
				return;
			}
		}
		throw new ValidacaoException("Não existe um cartão de id número " + id + " para esse cliente");
	}
	
	public List<MostrarCartaoDTO> pegarCartoesClientePorIdToken() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		List<Cartao> cartoes = cliente.getCartoes();
		
		if (cartoes.size() == 0) {
			throw new ValidacaoException("Cliente não possue cartão cadastrado");
		}
		
		List<MostrarCartaoDTO> listaMostrarCartaoDTO = new ArrayList<>();
		cartoes.forEach(cartao -> {
			listaMostrarCartaoDTO.add(new MostrarCartaoDTO(cartao));
		});
		
		return listaMostrarCartaoDTO;
	}

	@Transactional
	public MostrarCartaoDTO cadastrarNovoCartaoPorIdToken(CadastrarCartaoDTO cartaoDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();

		validadores.forEach(v -> v.validar(cartaoDTO, cliente));
		
		Cartao novoCartao = new Cartao(cartaoDTO, cliente);
		
		List<Cartao> cartoes = cliente.getCartoes();
		if (cartaoDTO.cartaoPadrao() == true && !cartoes.isEmpty()) {
			cartoes.forEach(cartao -> {
				if (cartao.getCartaoPadrao() == true) {
					cartao.setCartaoPadrao(false);
					cartaoRepository.save(cartao);
				}
			});
		}
		
		if (cartaoDTO.cartaoPadrao() == false && cartoes.isEmpty()) {
			novoCartao.setCartaoPadrao(true);
		}
			
		novoCartao.setNomeBanco("Falta implementar API banco");
		cartaoRepository.save(novoCartao);
		
		return new MostrarCartaoDTO(novoCartao);
	}
	
	@Transactional
	public void alterarCartaoPadraoPorIdToken(Long id) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		List<Cartao> cartoes = cliente.getCartoes();
		
		AtomicBoolean comoCartaoEstaDepoisDeSerAlterado = new AtomicBoolean();
		AtomicBoolean foiAlterado = new AtomicBoolean(false);
		cartoes.forEach(cartao -> {
			if (cartao.getId() == id) {
				if (cartao.getCartaoPadrao() == false) {
					cartao.setCartaoPadrao(true);
					comoCartaoEstaDepoisDeSerAlterado.set(true);
					foiAlterado.set(true);
				} else if (cartao.getCartaoPadrao() == true) {
					cartao.setCartaoPadrao(false);
					foiAlterado.set(true);
				}
				cartaoRepository.save(cartao);
			}
		});
		
		if (comoCartaoEstaDepoisDeSerAlterado.get() != false) {
			cartoes.forEach(cartao -> {
				if (cartao.getId() != id && cartao.getCartaoPadrao() == true) {
					cartao.setCartaoPadrao(false);
					cartaoRepository.save(cartao);
				}
			});
		}
		
		if (foiAlterado.get() == false) {
			throw new ValidacaoException("ID do cartão de número: " + id + " não existe para esse cliente");
		}
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

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
