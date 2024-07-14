package br.com.danielschiavo.pedido.service.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.service.cliente.CustomerService;
import br.com.danielschiavo.filestorage.model.File;
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
import br.com.danielschiavo.pedido.model.entity.ItemPedido;
import br.com.danielschiavo.pedido.model.entity.Pagamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.pedido.service.pedido.validacoes.fazerpedido.ValidadorFazerPedido;
import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private EntregaService entregaService;

	@Autowired
	private FileStoragePedidoService fileStoragePedidoService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private List<ValidadorFazerPedido> validador;

	@Autowired
	private CarrinhoService carrinhoService;

    @Autowired
    private CustomerService clienteService;

	public Page<Pedido> pegarTodosPedidosPorClienteId(Pageable pageable, Long clienteId) {
		return pedidoRepository.findAllByClienteId(pageable, clienteId);
	}
	
	@Transactional
	public Pedido realizarPedido(Long clienteId, Pedido pedido) {
		Customer customer = clienteService.getCustomerById(clienteId);
		validador.forEach(v -> v.validar(pedido, customer));

		List<ItemPedido> itemsPedido = pegarItemsPedido(pedido.getItemsPedido());

		BigDecimal valorTotal = itemsPedido.stream().map(ItemPedido::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		Pagamento pagamento = pagamentoService.executarPagamento(pedido.getPagamento(), valorTotal, customer);
		Entrega entrega = entregaService.executarEntrega(pedido.getEntrega(), customer);

		pedido.setNomeCliente(customer.getName() + " " + customer.getSurname());
		pedido.setCpf(customer.getCpf());
		pedido.setDataPedido(LocalDateTime.now());
		pedido.setStatusPedido(StatusPedido.A_PAGAR);
		pedido.adicionarItemPedido(itemsPedido);
		pedido.setPagamento(pagamento);
		pedido.setEntrega(entrega);

		if (pedido.getComprouPeloCarrinho()) {
			Long[] ids = pedido.getItemsPedido().stream().map(ItemPedido::getProdutoId).toArray(Long[]::new);
			carrinhoService.removerProdutoDoCarrinho(clienteId, ids);
		}

		return pedidoRepository.save(pedido);
	}

	public Pedido pegarPedidoPorId(UUID pedidoId, Long clienteId) {
		return pedidoRepository.findByIdAndClienteId(pedidoId, clienteId).orElseThrow(() -> new ValidacaoException("Usuário não possui um pedido com esse ID"));
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITÁRIOS
//	------------------------------
//	------------------------------

	private List<ItemPedido> pegarItemsPedido(List<ItemPedido> items) {
	List<ItemPedido> itemsPedido = new ArrayList<>();
		items.forEach(item -> {
			var produto = produtoService.pegarProdutoPorId(item.getProdutoId());
			BigDecimal subTotal = produto.getPreco().multiply(new BigDecimal(item.getQuantidade()));

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
}
