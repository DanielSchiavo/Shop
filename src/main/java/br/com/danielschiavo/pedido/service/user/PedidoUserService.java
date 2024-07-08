package br.com.danielschiavo.pedido.service.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.vendas.service.CarrinhoUserService;
import br.com.danielschiavo.filestorage.service.FileStoragePedidoService;
import br.com.danielschiavo.pedido.model.*;
import br.com.danielschiavo.pedido.model.entrega.Entrega;
import br.com.danielschiavo.pedido.model.itempedido.AdicionarItemPedidoDTO;
import br.com.danielschiavo.pedido.model.itempedido.ItemPedido;
import br.com.danielschiavo.pedido.model.pagamento.Pagamento;
import br.com.danielschiavo.pedido.repository.user.PedidoRepository;
import br.com.danielschiavo.produto.service.user.ProdutoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shared.infra.security.SecurityService;
import br.com.danielschiavo.pedido.service.user.validacoes.ValidadorCriarNovoPedido;
import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class PedidoUserService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private FileStoragePedidoService fileStoragePedidoService;

	@Autowired
	private ProdutoUserService produtoService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private List<ValidadorCriarNovoPedido> validador;
	
	@Autowired
	private PedidoComumMapper pedidoMapper;
	
    @Autowired
    private EntregaService entregaService;

	@Autowired
	private CarrinhoUserService carrinhoUserService;

	public Page<MostrarPedidoResponse> pegarPedidosClientePorIdToken(Pageable pageable) {
		Cliente cliente = securityService.getCliente();
		Page<Pedido> pagePedidos = pedidoRepository.findAllByCliente(cliente, pageable);

		List<MostrarPedidoResponse> list = new ArrayList<>();
		for (Pedido pedido : pagePedidos) {
			List<MostrarProdutoDoPedidoResponse> listaMostrarProdutoDoPedidoDTO = pedidoMapper.pedidoParaMostrarProdutoDoPedidoDTO(pedido, fileStoragePedidoService);

			var mostrarPedidoDTO = new MostrarPedidoResponse(pedido, listaMostrarProdutoDoPedidoDTO);
			list.add(mostrarPedidoDTO);
		}
		
		return new PageImpl<>(list, pagePedidos.getPageable(),
				pagePedidos.getTotalElements());
	}
	
	@Transactional
	public String criarPedidoPorIdToken(FazerPedidoRequest request) {
		Cliente cliente = securityService.getCliente();
		validador.forEach(v -> v.validar(request, cliente));

		List<ItemPedido> itemsPedido = pegarItemsPedido(request.items());

		BigDecimal valorTotal = itemsPedido.stream().map(ItemPedido::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		Pagamento pagamento = pagamentoService.executarPagamento(request.pagamento(), valorTotal, cliente);
		Entrega entrega = entregaService.executarEntrega(request.entrega(), cliente);

		Pedido pedido = Pedido.builder()
				.nomeCliente(cliente.getNome() + " " + cliente.getSobrenome())
				.cpf(cliente.getCpf())
				.clienteId(cliente.getId())
				.dataPedido(LocalDateTime.now())
				.statusPedido(StatusPedido.A_PAGAR)
				.itemsPedido(itemsPedido)
				.pagamento(pagamento)
				.entrega(entrega).build();

		if (request.veioPeloCarrinho()) {
			List<Long> ids = request.items().stream().map(AdicionarItemPedidoDTO::produtoId).collect(Collectors.toList());
			carrinhoUserService.deletarProdutoNoCarrinhoPorIdToken(ids);
		}

		pedidoRepository.save(pedido);
		
		return "Pedido realizado com sucesso!";
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITÁRIOS
//	------------------------------
//	------------------------------

	private List<ItemPedido> pegarItemsPedido(List<AdicionarItemPedidoDTO> items) {
	List<ItemPedido> itemsPedido = new ArrayList<>();
		items.forEach(item -> {
			var produto = produtoService.detalharProdutoPorId(item.produtoId());
			BigDecimal subTotal = produto.getPreco().multiply(new BigDecimal(item.quantidade()));

			String nomeImagemPedido = fileStoragePedidoService.persistirOuRecuperarImagemPedido(produtoService.pegarNomePrimeiraImagem(produto.getArquivos()), produto.getId());

			ItemPedido itemPedido = ItemPedido.builder()
					.preco(produto.getPreco())
					.quantidade(produto.getQuantidade())
					.nomeProduto(produto.getNome())
					.primeiraImagem(nomeImagemPedido)
					.subTotal(subTotal)
					.produtoId(produto.getId()).build();

			itemsPedido.add(itemPedido);
		});
		
		return itemsPedido;
	}
}
