package br.com.danielschiavo.shop.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarProdutoDoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.EnderecoPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.Entrega;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.ItemPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.Pagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.StatusPagamento;
import br.com.danielschiavo.shop.models.produto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.repositories.EnderecoRepository;
import br.com.danielschiavo.shop.repositories.PedidoRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private TokenJWTService tokenJWTService;
	
	@Autowired
	private FilesStorageService filesStorageService;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	public Page<MostrarPedidoDTO> pegarPedidosUsuario(Pageable pageable) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);

		Page<Pedido> pagePedidos = pedidoRepository.findAllByCliente(cliente, pageable);

		List<MostrarPedidoDTO> list = new ArrayList<>();

		for (Pedido pedido : pagePedidos) {
			List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO = new ArrayList<>();
			pedido.getItemsPedido().forEach(itemPedido -> {
				System.out.println("AQUI " + itemPedido.getIdProduto() + " - " + itemPedido.getNomeProduto());
				byte[] primeiraImagem = filesStorageService.pegarBytesPrimeiraImagemProduto(itemPedido.getIdProduto());
				
				listaMostrarProdutoDoPedidoDTO.add(new MostrarProdutoDoPedidoDTO(itemPedido, primeiraImagem));
			});
			var mostrarPedidoDTO = new MostrarPedidoDTO(pedido, listaMostrarProdutoDoPedidoDTO);
			list.add(mostrarPedidoDTO);
		}
		return new PageImpl<>(list, pagePedidos.getPageable(),
				pagePedidos.getTotalElements());
	}

	public List<Pedido> pegarPedidosPeloIdDoCliente(Long id) {
		return pedidoRepository.findAllByClienteIdOrderByDataPedidoAsc(id);
	}

	@Transactional
	public void criarPedido(CriarPedidoDTO pedidoDTO) {
		List<Long> ids = pedidoDTO.items().stream().map(AdicionarItemPedidoDTO::idProduto).collect(Collectors.toList());

		List<Produto> produtos = produtoRepository.findAllByIdAndAtivoTrue(ids);

		verificarSeEstaFaltandoProduto(ids, produtos);
		
		criarEntidadePedidoESalvar(pedidoDTO);

		processarPagamento(pedidoDTO);

		processarEntrega(pedidoDTO);
	}

	private void processarEntrega(CriarPedidoDTO pedidoDTO) {
		switch (pedidoDTO.entrega().tipoEntrega()) {
		case CORREIOS: {
			System.out.println("Processar entrega via Correios");
			break;
		}

		case ENTREGA_DIGITAL: {
			System.out.println("Processar entrega via digital");
			break;
		}

		case ENTREGA_EXPRESSA: {
			System.out.println("Processar entrega via entrega expressa");
			break;
		}

		case RETIRADA_NA_LOJA: {
			System.out.println("Processar entrega via retirada na loja");
			break;
		}
		}
	}

	private void processarPagamento(CriarPedidoDTO pedidoDTO) {
		switch (pedidoDTO.pagamento().metodoPagamento()) {
		case BOLETO: {
			System.out.println("Gerar boleto");
			break;
		}

		case CARTAO_CREDITO: {
			System.out.println("Efetivar compra cartão de crédito");
			break;
		}

		case CARTAO_DEBITO: {
			System.out.println("Efetivar compra cartão de débito");
			break;
		}

		case PIX: {
			System.out.println("Gerar QR Code Píx");
			break;
		}

		case TRANSFERENCIA_BANCARIA: {
			System.out.println("Transferencia bancária");
			break;
		}
		}
	}

	private Pedido criarEntidadePedidoESalvar(CriarPedidoDTO pedidoDTO) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		var idEndereco = pedidoDTO.entrega().idEndereco();

		EnderecoPedido enderecoPedido = null;
		
		if (idEndereco != null) {
			var endereco = enderecoRepository.findById(idEndereco).get();

			enderecoPedido = new EnderecoPedido(endereco.getCep(), 
												endereco.getRua(), 
												endereco.getNumero(),
												endereco.getComplemento(), 
												endereco.getBairro(), 
												endereco.getCidade(), 
												endereco.getEstado());
		}
		
		var pedido = new Pedido(cliente, cliente.getNome(), cliente.getCpf(), StatusPedido.A_PAGAR);
		var pagamento = new Pagamento(MetodoPagamento.BOLETO, StatusPagamento.EM_PROCESSAMENTO, pedido);
		var entrega = new Entrega(pedido, pedidoDTO.entrega().tipoEntrega(), enderecoPedido);
		
		List<ItemPedido> itemsPedido = pedido.getItemsPedido();
		pedidoDTO.items().forEach(adicionarItemPedidoDTO -> {
			Produto produto = produtoRepository.findById(adicionarItemPedidoDTO.idProduto()).orElseThrow();
			ArquivosProduto first = produto.getArquivosProduto().stream().filter(arquivoProduto -> arquivoProduto.getPosicao() == 0).findFirst().get();
			
			itemsPedido.add(new ItemPedido(produto, adicionarItemPedidoDTO.quantidade(), first.getNome()));
		});
		
		pedido.setPagamento(pagamento);
		pedido.setEntrega(entrega);

		pedidoRepository.save(pedido);
		
		return pedido;
		
	}

	private void verificarSeEstaFaltandoProduto(List<Long> ids, List<Produto> produtos) {
		Set<Long> idsEncontrados = produtos.stream().map(Produto::getId).collect(Collectors.toSet());

		List<Long> idsNaoEncontrados = ids.stream().filter(id -> !idsEncontrados.contains(id))
				.collect(Collectors.toList());

		if (!idsNaoEncontrados.isEmpty()) {
			throw new RuntimeException("Produtos não encontrados para os IDs: " + idsNaoEncontrados);
		}

	}

}
