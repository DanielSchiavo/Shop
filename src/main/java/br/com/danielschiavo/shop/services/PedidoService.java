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
import br.com.danielschiavo.shop.repositories.ClienteRepository;
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
	private ClienteRepository clienteRepository;

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
		validador.forEach(v -> v.validar(pedidoDTO));

		Pedido pedido = null;
		//SE NÃO É PELO CARRINHO FAZ ISSO
		if (pedidoDTO.item() != null) {
			Long idProduto = pedidoDTO.item().idProduto();
			produtoService.verificarSeProdutoExistePorIdEAtivoTrue(idProduto);
			
			pedido = criarEntidadePedidoERelacionamentos(pedidoDTO, null);
		}
		//SE É PELO CARRINHO FAZ ISSO
		else {
			List<MostrarItemCarrinhoDTO> produtosCarrinho = carrinhoService.pegarItensNoCarrinhoCliente();
			
			pedido = criarEntidadePedidoERelacionamentos(pedidoDTO, produtosCarrinho);
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
		var cliente = clienteRepository.getReferenceById(id);

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

	private Pedido criarEntidadePedidoERelacionamentos(CriarPedidoDTO pedidoDTO, List<MostrarItemCarrinhoDTO> produtosCarrinho) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		Long idEndereco = pedidoDTO.entrega().idEndereco();
		EnderecoPedido enderecoPedido = null;
		if (idEndereco != null) {
			Endereco endereco = enderecoService.verificarSeEnderecoExistePorIdEnderecoECliente(idEndereco, cliente.getEnderecos());
			enderecoPedido = new EnderecoPedido(endereco);
		}
		
		Long idCartao = pedidoDTO.pagamento().idCartao();
		CartaoPedido cartaoPedido = null;
		if (idCartao != null) {
			Cartao cartao = cartaoService.verificarSeCartaoExistePorIdCartaoECliente(idCartao);
			cartaoPedido = new CartaoPedido(cartao, pedidoDTO.pagamento().numeroParcelas());
		}
		
		MetodoPagamento metodoPagamentoDTO = pedidoDTO.pagamento().metodoPagamento();
		var pedido = new Pedido(cliente, cliente.getNome(), cliente.getCpf(), StatusPedido.A_PAGAR);
		var pagamento = new Pagamento(metodoPagamentoDTO, StatusPagamento.EM_PROCESSAMENTO, cartaoPedido, pedido);
		var entrega = new Entrega(pedido, pedidoDTO.entrega().tipoEntrega(), enderecoPedido);
		
        AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(BigDecimal.ZERO);
		if (pedidoDTO.item() != null) {
			AdicionarItemPedidoDTO adicionarItemPedidoDTO = pedidoDTO.item();
			Produto produto = produtoService.verificarSeProdutoExistePorIdEAtivoTrue(adicionarItemPedidoDTO.idProduto());
			ArquivosProduto first = produto.getArquivosProduto().stream().filter(arquivoProduto -> arquivoProduto.getPosicao() == 0).findFirst().get();
			String nomeImagemPedido = fileService.persistirOuRecuperarImagemPedido(first.getNome(), produto.getId());
			ItemPedido itemPedido = new ItemPedido(produto, adicionarItemPedidoDTO.quantidade(), nomeImagemPedido, pedido);
			pedido.getItemsPedido().add(itemPedido);
			
			BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(adicionarItemPedidoDTO.quantidade()));
			itemPedido.setSubTotal(subTotal);
			valorTotal.set(subTotal);
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
		        valorTotal.updateAndGet(v -> v.add(subTotal));
			});
		}
		pedido.setValorTotal(valorTotal.get());
		pedido.setPagamento(pagamento);
		pedido.setEntrega(entrega);

		return pedido;
	}

}
