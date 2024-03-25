package br.com.danielschiavo.shop.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarProdutoDoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.EnderecoPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.Entrega;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.ItemPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.CartaoPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.Pagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.StatusPagamento;
import br.com.danielschiavo.shop.models.pedido.validacoes.ValidadorCriarNovoPedido;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivosProduto;
import br.com.danielschiavo.shop.repositories.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@Autowired
	private FileStorageService fileService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private CartaoService cartaoService;
	
	@Autowired
	private CarrinhoService carrinhoService;
	
	@Autowired
	private List<ValidadorCriarNovoPedido> validador;

	public Page<MostrarPedidoDTO> pegarPedidosClientePorIdToken(Pageable pageable) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Page<Pedido> pagePedidos = pedidoRepository.findAllByCliente(cliente, pageable);

		List<MostrarPedidoDTO> list = new ArrayList<>();

		for (Pedido pedido : pagePedidos) {
			List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO = montarMostrarProdutoDoPedidoDTO(pedido);

			var mostrarPedidoDTO = new MostrarPedidoDTO(pedido, listaMostrarProdutoDoPedidoDTO);
			list.add(mostrarPedidoDTO);
		}
		return new PageImpl<>(list, pagePedidos.getPageable(),
				pagePedidos.getTotalElements());
	}
	
	@Transactional
	public MostrarPedidoDTO criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken(CriarPedidoDTO pedidoDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		validador.forEach(v -> v.validar(pedidoDTO, cliente));

		Pedido pedido = null;
		//Botão comprar agora faz isso
		if (pedidoDTO.item() != null) {
			pedido = criarEntidadePedidoERelacionamentos(pedidoDTO, null, cliente);
		}
		//Comprar do carrinho faz isso
		else {
			List<MostrarItemCarrinhoDTO> produtosCarrinho = carrinhoService.pegarItensNoCarrinhoCliente();
			
			pedido = criarEntidadePedidoERelacionamentos(pedidoDTO, produtosCarrinho, cliente);
		}
		
		List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO = montarMostrarProdutoDoPedidoDTO(pedido);
		
		processarPagamento(pedidoDTO);
		
		processarEntrega(pedidoDTO);
		
		pedidoRepository.save(pedido);
		
		
		return new MostrarPedidoDTO(pedido, listaMostrarProdutoDoPedidoDTO);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------

	public Page<MostrarPedidoDTO> pegarPedidosClientePorId(Long id, Pageable pageable) {
		Cliente cliente = clienteService.verificarSeClienteExistePorId(id);

		Page<Pedido> pagePedidos = pedidoRepository.findAllByCliente(cliente, pageable);

		List<MostrarPedidoDTO> list = new ArrayList<>();

		for (Pedido pedido : pagePedidos) {
			List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO = montarMostrarProdutoDoPedidoDTO(pedido);

			var mostrarPedidoDTO = new MostrarPedidoDTO(pedido, listaMostrarProdutoDoPedidoDTO);
			list.add(mostrarPedidoDTO);
		}
		return new PageImpl<>(list, pagePedidos.getPageable(),
				pagePedidos.getTotalElements());
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS PARA UTILITARIOS
//	------------------------------
//	------------------------------

	private List<MostrarProdutoDoPedidoDTO> montarMostrarProdutoDoPedidoDTO(Pedido pedido) {
		List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedidoDTO = new ArrayList<>();
		pedido.getItemsPedido().forEach(itemPedido -> {
			ArquivoInfoDTO arquivoInfoDTO = fileService.pegarImagemPedidoPorNome(itemPedido.getPrimeiraImagem());
			
			listaMostrarProdutoDoPedidoDTO.add(new MostrarProdutoDoPedidoDTO(itemPedido, arquivoInfoDTO.bytesArquivo()));
		});
		return listaMostrarProdutoDoPedidoDTO;
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
		}
	}

	private Pedido criarEntidadePedidoERelacionamentos(CriarPedidoDTO pedidoDTO, List<MostrarItemCarrinhoDTO> produtosCarrinho, Cliente cliente) {
		EnderecoPedido enderecoPedido = criarEnderecoPedido(pedidoDTO, cliente);
		CartaoPedido cartaoPedido = criarCartaoPedido(pedidoDTO, cliente);
		Pedido pedido = new Pedido(cliente, cliente.getNome(), cliente.getCpf(), StatusPedido.A_PAGAR);
		Pagamento pagamento = criarPagamento(pedidoDTO, cartaoPedido, pedido);
		Entrega entrega = new Entrega(pedido, pedidoDTO.entrega().tipoEntrega(), enderecoPedido);
		
		criarItemsPedido(pedidoDTO, produtosCarrinho, pedido);
		calcularEDefinirValorTotal(pedido);
		pedido.setPagamento(pagamento);
		pedido.setEntrega(entrega);

		return pedido;
	}

	private void calcularEDefinirValorTotal(Pedido pedido) {
		 AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(BigDecimal.ZERO);
		 pedido.getItemsPedido().forEach(item -> {
			 valorTotal.updateAndGet(v -> v.add(item.getSubTotal()));
		 });
		 pedido.setValorTotal(valorTotal.get());
	}

	private void criarItemsPedido(CriarPedidoDTO pedidoDTO, List<MostrarItemCarrinhoDTO> produtosCarrinho, Pedido pedido) {
		if (pedidoDTO.item() != null) {
			AdicionarItemPedidoDTO adicionarItemPedidoDTO = pedidoDTO.item();
			Produto produto = produtoService.verificarSeProdutoExistePorIdEAtivoTrue(adicionarItemPedidoDTO.idProduto());
			ArquivosProduto first = produto.getArquivosProduto().stream().filter(arquivoProduto -> arquivoProduto.getPosicao() == 0).findFirst().get();
			String nomeImagemPedido = fileService.persistirOuRecuperarImagemPedido(first.getNome(), produto.getId());
			ItemPedido itemPedido = new ItemPedido(produto, adicionarItemPedidoDTO.quantidade(), nomeImagemPedido, pedido);
			pedido.getItemsPedido().add(itemPedido);
			
			BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(adicionarItemPedidoDTO.quantidade()));
			itemPedido.setSubTotal(subTotal);
		}
		else {
			produtosCarrinho.forEach(p -> {
				Produto produto = produtoService.verificarSeProdutoExistePorIdEAtivoTrue(p.idProduto());
				String first = produto.pegarNomePrimeiraImagem();
				String nomeImagemPedido = fileService.persistirOuRecuperarImagemPedido(first, produto.getId());
				ItemPedido itemPedido = new ItemPedido(produto, p.quantidade(), nomeImagemPedido, pedido);
				pedido.getItemsPedido().add(itemPedido);
				
				BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(p.quantidade()));
				itemPedido.setSubTotal(subTotal);
			});
		}
	}

	private Pagamento criarPagamento(CriarPedidoDTO pedidoDTO, CartaoPedido cartaoPedido, Pedido pedido) {
		MetodoPagamento metodoPagamentoDTO = pedidoDTO.pagamento().metodoPagamento();
		if (metodoPagamentoDTO == MetodoPagamento.BOLETO || metodoPagamentoDTO == MetodoPagamento.PIX) {
			return new Pagamento(metodoPagamentoDTO, StatusPagamento.PENDENTE, cartaoPedido, pedido);
		}
		else {
			return new Pagamento(metodoPagamentoDTO, StatusPagamento.EM_PROCESSAMENTO, cartaoPedido, pedido);
		}
	}

	private CartaoPedido criarCartaoPedido(CriarPedidoDTO pedidoDTO, Cliente cliente) {
		Long idCartao = pedidoDTO.pagamento().idCartao();
		if (idCartao != null) {			Cartao cartao = cartaoService.verificarSeCartaoExistePorIdCartaoECliente(idCartao, cliente);
			return new CartaoPedido(cartao, pedidoDTO.pagamento().numeroParcelas());
		}
		return null;
	}

	private EnderecoPedido criarEnderecoPedido(CriarPedidoDTO pedidoDTO, Cliente cliente) {
		Long idEndereco = pedidoDTO.entrega().idEndereco();
		if (idEndereco != null) {
			Endereco endereco = enderecoService.verificarSeEnderecoExistePorIdEnderecoECliente(idEndereco, cliente);
			EnderecoPedido enderecoPedido = new EnderecoPedido(endereco);
			return enderecoPedido;
		}
		return null;
	}

}
