package br.com.danielschiavo.pedido.service.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.service.ClienteService;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.pedido.dto.request.pedido.FazerPedidoRequest;
import br.com.danielschiavo.pedido.mapper.PedidoMapper;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.model.enums.StatusPedido;
import br.com.danielschiavo.pedido.repository.PedidoRepository;
import br.com.danielschiavo.pedido.service.entrega.EntregaService;
import br.com.danielschiavo.pedido.service.pagamento.PagamentoService;
import br.com.danielschiavo.produto.service.produto.ProdutoService;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import br.com.danielschiavo.vendas.service.CarrinhoService;
import br.com.danielschiavo.filestorage.service.FileStoragePedidoService;
import br.com.danielschiavo.pedido.model.entity.Entrega;
import br.com.danielschiavo.pedido.dto.request.itempedido.AdicionarItemPedidoRequest;
import br.com.danielschiavo.pedido.model.entity.ItemPedido;
import br.com.danielschiavo.pedido.model.entity.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.pedido.service.pedido.validacoes.ValidadorCriarNovoPedido;
import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private FileStoragePedidoService fileStoragePedidoService;

	@Autowired
	private PedidoMapper pedidoMapper;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private EntregaService entregaService;

	@Autowired
	private List<ValidadorCriarNovoPedido> validador;

	@Autowired
	private CarrinhoService carrinhoService;
    @Autowired
    private ClienteService clienteService;

	public Page<Pedido> pegarTodosPedidosPorClienteId(Pageable pageable, Long clienteId) {
		return pedidoRepository.findAllByClienteId(pageable, clienteId);
	}
	
	@Transactional
	public Pedido realizarPedido(FazerPedidoRequest request, Long clienteId) {
		Cliente cliente = clienteService.pegarClientePorId(clienteId);
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
			Long[] ids = request.items().stream().map(AdicionarItemPedidoRequest::produtoId).toArray(Long[]::new);
			carrinhoService.removerProdutoDoCarrinho(clienteId, ids);
		}

		return pedidoRepository.save(pedido);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITÁRIOS
//	------------------------------
//	------------------------------

	private List<ItemPedido> pegarItemsPedido(List<AdicionarItemPedidoRequest> items) {
	List<ItemPedido> itemsPedido = new ArrayList<>();
		items.forEach(item -> {
			var produto = produtoService.pegarProdutoPorId(item.produtoId());
			BigDecimal subTotal = produto.getPreco().multiply(new BigDecimal(item.quantidade()));

			File file = fileStoragePedidoService.handleImagemPedido(produto.pegarNomePrimeiraImagem(), produto.getId());

			ItemPedido itemPedido = ItemPedido.builder()
					.preco(produto.getPreco())
					.quantidade(produto.getQuantidade())
					.nomeProduto(produto.getNome())
					.primeiraImagem(file.getFileName())
					.subTotal(subTotal)
					.produtoId(produto.getId()).build();

			itemsPedido.add(itemPedido);
		});
		
		return itemsPedido;
	}

	public Pedido pegarPedidoPorId(UUID pedidoId, Long clienteId) {
		return pedidoRepository.findByIdAndClienteId(pedidoId, clienteId).orElseThrow(() -> new ValidacaoException("Usuário não possui um pedido com esse ID"));
	}
}
