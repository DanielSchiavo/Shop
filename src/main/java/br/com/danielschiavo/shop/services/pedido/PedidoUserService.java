package br.com.danielschiavo.shop.services.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.mapper.pedido.PedidoMapper;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.cartao.Cartao;
import br.com.danielschiavo.shop.models.cliente.endereco.Endereco;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;
import br.com.danielschiavo.shop.models.pedido.dto.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.dto.MostrarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.dto.MostrarProdutoDoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.entrega.EnderecoPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.Entrega;
import br.com.danielschiavo.shop.models.pedido.itempedido.ItemPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.CartaoPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.Pagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.StatusPagamento;
import br.com.danielschiavo.shop.models.pedido.validacoes.ValidadorCriarNovoPedido;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.PedidoRepository;
import br.com.danielschiavo.shop.services.cliente.CarrinhoUtilidadeService;
import br.com.danielschiavo.shop.services.cliente.user.CartaoUserService;
import br.com.danielschiavo.shop.services.cliente.user.EnderecoUserService;
import br.com.danielschiavo.shop.services.filestorage.FileStoragePedidoService;
import br.com.danielschiavo.shop.services.produto.ProdutoUtilidadeService;
import jakarta.transaction.Transactional;
import lombok.Setter;

@Service
@Setter
public class PedidoUserService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;

	@Autowired
	private FileStoragePedidoService fileStoragePedidoService;

	@Autowired
	private ProdutoUtilidadeService produtoUtilidadeService;
	
	@Autowired
	private EnderecoUserService enderecoService;
	
	@Autowired
	private CartaoUserService cartaoService;
	
	@Autowired
	private CarrinhoUtilidadeService carrinhoUtilidadeService;
	
	@Autowired
	private List<ValidadorCriarNovoPedido> validador;
	
	@Autowired
	private PedidoMapper pedidoMapper;

	public Page<MostrarPedidoDTO> pegarPedidosClientePorIdToken(Pageable pageable) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Page<Pedido> pagePedidos = pedidoRepository.findAllByCliente(cliente, pageable);

		List<MostrarPedidoDTO> list = new ArrayList<>();

		for (Pedido pedido : pagePedidos) {
			List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO = pedidoMapper.pedidoParaMostrarProdutoDoPedidoDTO(pedido, fileStoragePedidoService);

			var mostrarPedidoDTO = new MostrarPedidoDTO(pedido, listaMostrarProdutoDoPedidoDTO);
			list.add(mostrarPedidoDTO);
		}
		return new PageImpl<>(list, pagePedidos.getPageable(),
				pagePedidos.getTotalElements());
	}
	
	@Transactional
	public MostrarPedidoDTO criarPedidoPorIdToken(CriarPedidoDTO pedidoDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		validador.forEach(v -> v.validar(pedidoDTO, cliente));
		
		Pedido pedido = criarEntidadePedidoERelacionamentos(pedidoDTO, cliente);
		if (pedidoDTO.veioPeloCarrinho()) {
			List<Long> ids = pedidoDTO.items().stream().map(item -> item.idProduto()).collect(Collectors.toList());
			carrinhoUtilidadeService.deletarItemsCarrinhoAposPedidoGerado(ids, cliente);
		}
		processarPagamento(pedidoDTO);
		processarEntrega(pedidoDTO);
		pedidoRepository.save(pedido);
		
		List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO = pedidoMapper.pedidoParaMostrarProdutoDoPedidoDTO(pedido, fileStoragePedidoService);
		return new MostrarPedidoDTO(pedido, listaMostrarProdutoDoPedidoDTO);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITÁRIOS
//	------------------------------
//	------------------------------
	
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
		}
	}

	private Pedido criarEntidadePedidoERelacionamentos(CriarPedidoDTO pedidoDTO, Cliente cliente) {
		Pedido pedido = criarPedido(cliente);
		criarESetarPagamento(pedidoDTO, pedido, cliente);
		criarESetarEntrega(pedidoDTO, pedido, cliente);
		criarESetarItemsPedido(pedidoDTO, pedido);
		calcularESetarValorTotal(pedido);
		return pedido;
	}

	private Entrega criarESetarEntrega(CriarPedidoDTO pedidoDTO, Pedido pedido, Cliente cliente) {
		Long idEndereco = pedidoDTO.entrega().idEndereco();
		EnderecoPedido enderecoPedido = null;
		if (idEndereco != null) {
			Endereco endereco = enderecoService.verificarSeEnderecoExistePorIdEnderecoECliente(idEndereco, cliente);
			enderecoPedido = new EnderecoPedido(endereco);
		}
		Entrega entrega = new Entrega(null, pedidoDTO.entrega().tipoEntrega(), enderecoPedido, pedido);
		pedido.setEntrega(entrega);
		return entrega;
	}

	private Pedido criarPedido(Cliente cliente) {
		return new Pedido(null,
						  null,
						  LocalDateTime.now(),
						  cliente.getNome() + " " + cliente.getSobrenome(),
						  cliente.getCpf(),
						  StatusPedido.A_PAGAR,
						  new ArrayList<ItemPedido>(),
						  null,
						  null,
						  cliente);
		
	}

	private void calcularESetarValorTotal(Pedido pedido) {
		 AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(BigDecimal.ZERO);
		 pedido.getItemsPedido().forEach(item -> {
			 valorTotal.updateAndGet(v -> v.add(item.getSubTotal()));
		 });
		 pedido.setValorTotal(valorTotal.get());
	}

	private void criarESetarItemsPedido(CriarPedidoDTO pedidoDTO, Pedido pedido) {
		pedidoDTO.items().forEach(item -> {
			Produto produto = produtoUtilidadeService.verificarSeProdutoExistePorIdEAtivoTrue(item.idProduto());
			String first = produtoUtilidadeService.pegarNomePrimeiraImagem(produto);
			String nomeImagemPedido = fileStoragePedidoService.persistirOuRecuperarImagemPedido(first, produto.getId());
			ItemPedido itemPedido = new ItemPedido(null, produto.getPreco(), item.quantidade(), produto.getNome(), nomeImagemPedido, null, produto.getId(), pedido);
			pedido.adicionarItemPedido(itemPedido);

			BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(item.quantidade()));
			itemPedido.setSubTotal(subTotal);
		});
	}

	private Pagamento criarESetarPagamento(CriarPedidoDTO pedidoDTO, Pedido pedido, Cliente cliente) {
		MetodoPagamento metodoPagamentoDTO = pedidoDTO.pagamento().metodoPagamento();
		System.out.println("TESTE" + metodoPagamentoDTO);
		Pagamento pagamento = new Pagamento(null, metodoPagamentoDTO, null, null, null, pedido);
		if (metodoPagamentoDTO == MetodoPagamento.BOLETO || metodoPagamentoDTO == MetodoPagamento.PIX) {
			pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
		}
		if (metodoPagamentoDTO == MetodoPagamento.CARTAO_CREDITO || metodoPagamentoDTO == MetodoPagamento.CARTAO_DEBITO) {
			pagamento.setStatusPagamento(StatusPagamento.EM_PROCESSAMENTO);
			Long idCartao = pedidoDTO.pagamento().idCartao();
			CartaoPedido cartaoPedido = null;
			if (idCartao != null) {
				Cartao cartao = cartaoService.verificarSeCartaoExistePorIdCartaoECliente(idCartao, cliente);
				cartaoPedido = new CartaoPedido(cartao, pedidoDTO.pagamento().numeroParcelas());
				pagamento.setCartaoPedido(cartaoPedido);
			}
		}
		pedido.setPagamento(pagamento);
		return pagamento;
	}
}
