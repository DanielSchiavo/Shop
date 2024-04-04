package br.com.danielschiavo.shop.services.cliente.user;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.mapper.cliente.CarrinhoMapper;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.cliente.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho.AdicionarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.cliente.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.produto.ProdutoRepository;
import br.com.danielschiavo.shop.services.cliente.CarrinhoUtilidadeService;
import br.com.danielschiavo.shop.services.filestorage.FileStorageProdutoService;
import br.com.danielschiavo.shop.services.produto.ProdutoUtilidadeService;
import lombok.Setter;

@Service
@Setter
public class CarrinhoUserService {

	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	@Autowired
	private ProdutoUtilidadeService produtoUtilidadeService;
	
	@Autowired
	private CarrinhoMapper carrinhoMapper;
	
	@Autowired
	private FileStorageProdutoService fileService;
	
	@Autowired
	private CarrinhoUtilidadeService carrinhoUtilidadeService;
	
	@Transactional
	public void deletarProdutoNoCarrinhoPorIdToken(Long idProduto) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		Carrinho carrinho = carrinhoUtilidadeService.verificarEPegarCarrinhoCliente(cliente);
		
		Iterator<ItemCarrinho> iterator = carrinho.getItemsCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProduto().getId() == idProduto) {
				iterator.remove();
				carrinhoRepository.save(carrinho);
				return;
			}
		}
		throw new ValidacaoException("Não existe produto de id número " + idProduto + " no carrinho");
	}
	
	@Transactional
	public MostrarCarrinhoClienteDTO pegarCarrinhoClientePorIdToken() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		Carrinho carrinho = carrinhoUtilidadeService.verificarEPegarCarrinhoCliente(cliente);
		
		if (carrinho.getItemsCarrinho().size() == 0) {
			throw new ValidacaoException("Cliente não tem produtos no carrinho");
		}

		List<Long> ids = carrinho.getItemsCarrinho().stream()
				.map(ic -> ic.getProduto().getId())
				.collect(Collectors.toList());

		List<Produto> produtosDesordenados = produtoRepository.findAllById(ids);
		
		List<Produto> produtosOrdenados = ids.stream()
		        .map(id -> produtosDesordenados.stream()
		                .filter(produto -> produto.getId().equals(id))
		                .findFirst()
		                .orElse(null))
		        .collect(Collectors.toList());
		
		return carrinhoMapper.carrinhoParaMostrarCarrinhoClienteDTO(fileService, produtoUtilidadeService, carrinho, produtosOrdenados);
	}
	
	@Transactional
	public void adicionarProdutosNoCarrinhoPorIdToken(AdicionarItemCarrinhoDTO itemCarrinhoDTO) {
		if (itemCarrinhoDTO.quantidade() <= 0) {
			throw new ValidacaoException("A quantidade do produto deve ser maior ou igual a 1, o valor fornecido foi: "
					+ itemCarrinhoDTO.quantidade());
		}
		
		Cliente cliente = usuarioAutenticadoService.getCliente();
		Produto produto = produtoUtilidadeService.verificarSeProdutoExistePorId(itemCarrinhoDTO.idProduto());
		Carrinho carrinho = carrinhoUtilidadeService.verificarEPegarCarrinhoCliente(cliente);

		List<ItemCarrinho> itensCarrinho = carrinho.getItemsCarrinho();
		for (ItemCarrinho item : itensCarrinho) {
			if (item.getProduto().getId() == itemCarrinhoDTO.idProduto()) {
				item.setQuantidade(item.getQuantidade() + itemCarrinhoDTO.quantidade());
				carrinhoRepository.save(carrinho);
				return;
			}
		}
		
		carrinho.getItemsCarrinho().add(new ItemCarrinho(null, itemCarrinhoDTO.quantidade(), produto, carrinho));
		carrinhoRepository.save(carrinho);
	}

	@Transactional
	public void setarQuantidadeProdutoNoCarrinhoPorIdToken(AdicionarItemCarrinhoDTO itemCarrinhoDTO) {
		produtoUtilidadeService.verificarSeProdutoExistePorId(itemCarrinhoDTO.idProduto());
	
		Cliente cliente = usuarioAutenticadoService.getCliente();
		Carrinho carrinho = carrinhoUtilidadeService.verificarEPegarCarrinhoCliente(cliente);

		Iterator<ItemCarrinho> iterator = carrinho.getItemsCarrinho().iterator();
		while (iterator.hasNext()) {
			ItemCarrinho itemCarrinho = iterator.next();
			if (itemCarrinho.getProduto().getId() == itemCarrinhoDTO.idProduto()) {
				if (itemCarrinhoDTO.quantidade() == 0) {
					carrinho.removerItemCarrinho(itemCarrinho);
					carrinhoRepository.save(carrinho);
					return;
				}
				itemCarrinho.setQuantidade(itemCarrinhoDTO.quantidade());
				carrinhoRepository.save(carrinho);
				return;
			}
		}
	}
}
