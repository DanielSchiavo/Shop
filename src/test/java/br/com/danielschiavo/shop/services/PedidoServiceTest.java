package br.com.danielschiavo.shop.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.pedido.CriarPagamentoDTO;
import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarProdutoDoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.pedido.entrega.CriarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.entrega.EnderecoPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.Entrega;
import br.com.danielschiavo.shop.models.pedido.entrega.MostrarEnderecoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.entrega.MostrarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.ItemPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.MostrarPagamentoDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.Pagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.StatusPagamento;
import br.com.danielschiavo.shop.models.pedido.validacoes.ValidadorCriarNovoPedido;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.tipoentregaproduto.TipoEntregaProduto;
import br.com.danielschiavo.shop.repositories.PedidoRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {
	
	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@InjectMocks
	private PedidoService pedidoService;
	
	@Mock
	private PedidoRepository pedidoRepository;
	
	@Mock
	private ProdutoRepository produtoRepository;
	
	@Mock
	private FileStorageService fileService;
	
	@Mock
	private ProdutoService produtoService;
	
	@Mock
	private Cliente cliente;
	
	@Mock
	private ClienteService clienteService;
	
	@Mock
	private CarrinhoService carrinhoService;
	
	@Mock
	private EnderecoService enderecoService;
	
	@Mock
	private CartaoService cartaoService;
	
	@Spy
	private List<ValidadorCriarNovoPedido> validadores = new ArrayList<>();

	@Mock
	private ValidadorCriarNovoPedido validador1;
	
	@Mock
	private ValidadorCriarNovoPedido validador2;
	
	@Test
	void pegarPedidosClientePorIdToken() {
		//ARRANGE
		//Produto
		Produto produto = new Produto(1L, "Mouse gamer", "Descricao Mouse gamer", BigDecimal.valueOf(200.00), 100, true, null, null, null, null);
		Produto produto2 = new Produto(2L, "Teclado gamer", "Descricao Teclado gamer", BigDecimal.valueOf(200.00), 100, true, null, null, null, null);
		//ItemPedido
		List<ItemPedido> listaItemPedido = new ArrayList<>();
		List<ItemPedido> listaItemPedido2 = new ArrayList<>();
		ItemPedido itemPedido = new ItemPedido(1L, BigDecimal.valueOf(200.00), 5, "Mouse gamer", "Padrao.jpeg", BigDecimal.valueOf(1000.00), produto, null);
		ItemPedido itemPedido2 = new ItemPedido(2L, BigDecimal.valueOf(150.00), 3, "Teclado gamer", "Padrao.jpeg", BigDecimal.valueOf(600.00), produto2, null);
		listaItemPedido.addAll(List.of(itemPedido, itemPedido2));
		listaItemPedido2.add(itemPedido2);
		//Pagamento
		Pagamento pagamento = new Pagamento(1L, MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null, null, null);
		//Entrega
		EnderecoPedido enderecoPedido = new EnderecoPedido("12345678", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES");
		Entrega entrega = new Entrega(1L, TipoEntrega.CORREIOS, enderecoPedido, null);
		//Pedido
		List<Pedido> listaPedido = new ArrayList<>();
		Pedido pedido = new Pedido(1L, BigDecimal.valueOf(1450.00), LocalDateTime.now(), "Jucilene", "12345678912", StatusPedido.A_PAGAR, listaItemPedido, pagamento, entrega, cliente);
		Pedido pedido2 = new Pedido(2L, BigDecimal.valueOf(450.00), LocalDateTime.now(), "Jucilene", "12345678912", StatusPedido.A_PAGAR, listaItemPedido2, pagamento, entrega, cliente);
		listaPedido.addAll(List.of(pedido, pedido2));
		Page<Pedido> pagePedido = new PageImpl<>(listaPedido);
		
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Padrao.jpeg", bytesImagem);
		when(fileService.pegarImagemPedidoPorNome(any())).thenReturn(arquivoInfoDTO);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		Pageable pageable = PageRequest.of(0, 10);
		BDDMockito.when(pedidoRepository.findAllByCliente(cliente, pageable)).thenReturn(pagePedido);
		
		//ACT
		Page<MostrarPedidoDTO> pageMostrarPedidoDTO = pedidoService.pegarPedidosClientePorIdToken(pageable);
		
		//ASSERT
		Assertions.assertEquals(pagePedido.getTotalElements(), pageMostrarPedidoDTO.getTotalElements(), "O número total de elementos deve ser igual");
		List<MostrarPedidoDTO> listaMostrarPedidoDTO = pageMostrarPedidoDTO.getContent();

	    for (int i = 0; i < listaPedido.size(); i++) {
	        Pedido pedidoVerificar = listaPedido.get(i);
	        MostrarPedidoDTO dto = listaMostrarPedidoDTO.get(i);
	        
	        Assertions.assertEquals(pedidoVerificar.getCliente().getId(), dto.idCliente());
	        Assertions.assertEquals(pedidoVerificar.getValorTotal(), dto.valorTotal());
	        Assertions.assertEquals(pedidoVerificar.getDataPedido(), dto.dataPedido());
	        Assertions.assertEquals(pedidoVerificar.getStatusPedido(), dto.statusPedido());
	        
	        // Comparando entrega
	        Entrega entregaVerificar = pedidoVerificar.getEntrega();
	        MostrarEntregaDTO entregaDTO = dto.entrega();
	        Assertions.assertEquals(entregaVerificar.getTipoEntrega(), entregaDTO.tipoEntrega());
	        
	        // Comparando endereço de entrega
	        EnderecoPedido endereco = entregaVerificar.getEnderecoPedido();
	        MostrarEnderecoPedidoDTO enderecoDTO = entregaDTO.endereco();
	        if (endereco != null && enderecoDTO != null) {
	            Assertions.assertEquals(endereco.getCep(), enderecoDTO.cep());
	            Assertions.assertEquals(endereco.getRua(), enderecoDTO.rua());
	            Assertions.assertEquals(endereco.getNumero(), enderecoDTO.numero());
	            Assertions.assertEquals(endereco.getComplemento(), enderecoDTO.complemento());
	            Assertions.assertEquals(endereco.getBairro(), enderecoDTO.bairro());
	            Assertions.assertEquals(endereco.getCidade(), enderecoDTO.cidade());
	            Assertions.assertEquals(endereco.getEstado(), enderecoDTO.estado());
	        }
	        
	        // Comparando pagamento
	        Pagamento pagamentoVerificar = pedidoVerificar.getPagamento();
	        MostrarPagamentoDTO pagamentoDTO = dto.pagamento();
	        Assertions.assertEquals(pagamentoVerificar.getMetodoPagamento(), pagamentoDTO.metodoPagamento());
	        Assertions.assertEquals(pagamentoVerificar.getStatusPagamento(), pagamentoDTO.statusPagamento());
	        
	        // Comparando itens do pedido
	        List<ItemPedido> itensPedido = pedidoVerificar.getItemsPedido();
	        List<MostrarProdutoDoPedidoDTO> itensPedidoDTO = dto.produtos();
	        Assertions.assertEquals(itensPedido.size(), itensPedidoDTO.size(), "Os tamanhos das listas de itens do pedido devem ser iguais");
	        
	        for (int j = 0; j < itensPedido.size(); j++) {
	            ItemPedido item = itensPedido.get(j);
	            MostrarProdutoDoPedidoDTO itemDTO = itensPedidoDTO.get(j);
	            Assertions.assertEquals(item.getProduto().getId(), itemDTO.idProduto());
	            Assertions.assertEquals(item.getNomeProduto(), itemDTO.nomeProduto());
	            Assertions.assertEquals(item.getPreco(), itemDTO.preco());
	            Assertions.assertEquals(item.getQuantidade(), itemDTO.quantidade());
	            Assertions.assertEquals(item.getSubTotal(), itemDTO.subTotal());
	            Assertions.assertArrayEquals(bytesImagem, itemDTO.primeiraImagem());
	        }
	    }
	}
	
	@Test
	void criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken_CompraPeloBotaoComprarAgora() {
		//ARRANGE
		validadores.addAll(List.of(validador1, validador2));
		//Pedido
		CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(MetodoPagamento.BOLETO, null, null);
		CriarEntregaDTO criarEntregaDTO = new CriarEntregaDTO(TipoEntrega.ENTREGA_DIGITAL, null);
		AdicionarItemPedidoDTO adicionarItemPedidoDTO = new AdicionarItemPedidoDTO(1L, 3);
		CriarPedidoDTO criarPedidoDTO = new CriarPedidoDTO(criarPagamentoDTO, criarEntregaDTO, adicionarItemPedidoDTO);
		//Produto
		Set<TipoEntregaProduto> tipoEntrega = Set.of(new TipoEntregaProduto(1L, TipoEntrega.ENTREGA_DIGITAL, null));
		List<ArquivosProduto> listaArquivosProduto = new ArrayList<>();
		ArquivosProduto arquivosProduto = new ArquivosProduto(1L, "Padrao.jpeg", (byte) 0, null);
		listaArquivosProduto.add(arquivosProduto);
		Produto produto = new Produto(1L, "Software", "Descricao software", BigDecimal.valueOf(200.00), 100, true, tipoEntrega, listaArquivosProduto, null, null);
		//When
		when(produtoService.verificarSeProdutoExistePorIdEAtivoTrue(produto.getId())).thenReturn(produto);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		when(fileService.persistirOuRecuperarImagemPedido(any(String.class), any(Long.class))).thenReturn(arquivosProduto.getNome().toString());
		when(fileService.pegarImagemPedidoPorNome(any(String.class))).thenReturn(new ArquivoInfoDTO("Padrao.jpeg", "Bytes arquivo padrao.jpeg".getBytes()));
		
		//ACT
		MostrarPedidoDTO mostrarPedidoDTO = pedidoService.criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken(criarPedidoDTO);
		
		//ASSERT
		BDDMockito.then(validador1).should().validar(criarPedidoDTO, cliente);
		BDDMockito.then(validador2).should().validar(criarPedidoDTO, cliente);
		
		//Comparando MostrarPedidoDTO
		Assertions.assertEquals(cliente.getId(), mostrarPedidoDTO.idCliente());
		Assertions.assertEquals(BigDecimal.valueOf(600.00), mostrarPedidoDTO.valorTotal());
		Assertions.assertNotNull(mostrarPedidoDTO.dataPedido());
		Assertions.assertEquals(StatusPedido.A_PAGAR, mostrarPedidoDTO.statusPedido());
		
		//Comparando MostrarEntregaDTO
		MostrarEntregaDTO mostrarEntregaDTO = mostrarPedidoDTO.entrega();
		Assertions.assertEquals(criarEntregaDTO.tipoEntrega(), mostrarEntregaDTO.tipoEntrega());
		Assertions.assertNull(mostrarEntregaDTO.endereco());
	
		//Comparando MostrarPagamentoDTO
		MostrarPagamentoDTO mostrarPagamentoDTO = mostrarPedidoDTO.pagamento();
		Assertions.assertEquals(MetodoPagamento.BOLETO, mostrarPagamentoDTO.metodoPagamento());
		Assertions.assertEquals(StatusPagamento.PENDENTE, mostrarPagamentoDTO.statusPagamento());
		

	}
	
	@Test
	void criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken_CompraPeloCarrinho() {
		//ARRANGE
		validadores.addAll(List.of(validador1, validador2));
		//Cartao
		Cartao cartao = new Cartao(1L, "Santander", "1123444255591132", "Daniel schiavo rosseto", "03/25", true, TipoCartao.CREDITO, cliente);
		//Endereco
		Endereco endereco = new Endereco(1L, "12345621", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		//Pedido
		CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(MetodoPagamento.CARTAO_CREDITO, cartao.getId(), "10");
		CriarEntregaDTO criarEntregaDTO = new CriarEntregaDTO(TipoEntrega.CORREIOS, endereco.getId());
		CriarPedidoDTO criarPedidoDTO = new CriarPedidoDTO(criarPagamentoDTO, criarEntregaDTO, null);
		//Produto
		Set<TipoEntregaProduto> tipoEntrega = Set.of(new TipoEntregaProduto(1L, TipoEntrega.ENTREGA_DIGITAL, null));
		List<ArquivosProduto> listaArquivosProduto = new ArrayList<>();
		ArquivosProduto arquivosProduto = new ArquivosProduto(1L, "Padrao.jpeg", (byte) 0, null);
		listaArquivosProduto.add(arquivosProduto);
		List<Produto> listaProdutos = new ArrayList<>();
		Produto produto = new Produto(1L, "Software", "Descricao software", BigDecimal.valueOf(200.00), 100, true, tipoEntrega, listaArquivosProduto, null, null);
		Produto produto2 = new Produto(2L, "Software2", "Descricao software2", BigDecimal.valueOf(500.00), 100, true, tipoEntrega, listaArquivosProduto, null, null);
		listaProdutos.addAll(List.of(produto, produto2));
		//Carrinho
		List<MostrarItemCarrinhoDTO> produtosCarrinho = new ArrayList<>();
		MostrarItemCarrinhoDTO mostrarItemCarrinhoDTO = new MostrarItemCarrinhoDTO(1L, 3);
		MostrarItemCarrinhoDTO mostrarItemCarrinhoDTO2 = new MostrarItemCarrinhoDTO(2L, 1);
		produtosCarrinho.addAll(List.of(mostrarItemCarrinhoDTO, mostrarItemCarrinhoDTO2));
		//ArquivoInfoDto
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Padrao.jpeg", "Bytes arquivo padrao.jpeg".getBytes());
		//When
		when(carrinhoService.pegarItensNoCarrinhoCliente()).thenReturn(produtosCarrinho);
		when(enderecoService.verificarSeEnderecoExistePorIdEnderecoECliente(1L, cliente)).thenReturn(endereco);
		when(cartaoService.verificarSeCartaoExistePorIdCartaoECliente(1L, cliente)).thenReturn(cartao);
		when(produtoService.verificarSeProdutoExistePorIdEAtivoTrue(1L)).thenReturn(produto);
		when(produtoService.verificarSeProdutoExistePorIdEAtivoTrue(2L)).thenReturn(produto2);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		when(fileService.persistirOuRecuperarImagemPedido(any(String.class), any(Long.class))).thenReturn(arquivosProduto.getNome().toString());
		when(fileService.pegarImagemPedidoPorNome(any(String.class))).thenReturn(arquivoInfoDTO);
		
		//ACT
		MostrarPedidoDTO mostrarPedidoDTO = pedidoService.criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken(criarPedidoDTO);
		
		//ASSERT
		BDDMockito.then(validador1).should().validar(criarPedidoDTO, cliente);
		BDDMockito.then(validador2).should().validar(criarPedidoDTO, cliente);
		
		//Comparando MostrarPedidoDTO
		Assertions.assertEquals(cliente.getId(), mostrarPedidoDTO.idCliente());
		Assertions.assertEquals(BigDecimal.valueOf(1100.00), mostrarPedidoDTO.valorTotal());
		Assertions.assertNotNull(mostrarPedidoDTO.dataPedido());
		Assertions.assertEquals(StatusPedido.A_PAGAR, mostrarPedidoDTO.statusPedido());
		
		//Comparando MostrarEntregaDTO
		MostrarEntregaDTO mostrarEntregaDTO = mostrarPedidoDTO.entrega();
		Assertions.assertEquals(criarEntregaDTO.tipoEntrega(), mostrarEntregaDTO.tipoEntrega());
		Assertions.assertEquals(endereco.getCep(), mostrarEntregaDTO.endereco().cep());
		Assertions.assertEquals(endereco.getRua(), mostrarEntregaDTO.endereco().rua());
		Assertions.assertEquals(endereco.getNumero(), mostrarEntregaDTO.endereco().numero());
		Assertions.assertEquals(endereco.getComplemento(), mostrarEntregaDTO.endereco().complemento());
		Assertions.assertEquals(endereco.getBairro(), mostrarEntregaDTO.endereco().bairro());
		Assertions.assertEquals(endereco.getCidade(), mostrarEntregaDTO.endereco().cidade());
		Assertions.assertEquals(endereco.getEstado(), mostrarEntregaDTO.endereco().estado());
	
		//Comparando MostrarPagamentoDTO
		MostrarPagamentoDTO mostrarPagamentoDTO = mostrarPedidoDTO.pagamento();
		Assertions.assertEquals(MetodoPagamento.CARTAO_CREDITO, mostrarPagamentoDTO.metodoPagamento());
		Assertions.assertEquals(StatusPagamento.EM_PROCESSAMENTO, mostrarPagamentoDTO.statusPagamento());
		Assertions.assertEquals(cartao.getNomeBanco(), mostrarPagamentoDTO.cartaoPedido().nomeBanco());
		Assertions.assertEquals(cartao.getNumeroCartao(), mostrarPagamentoDTO.cartaoPedido().numeroCartao());
		Assertions.assertEquals(cartao.getNomeNoCartao(), mostrarPagamentoDTO.cartaoPedido().nomeNoCartao());
		Assertions.assertEquals(criarPagamentoDTO.numeroParcelas(), mostrarPagamentoDTO.cartaoPedido().numeroDeParcelas());
		Assertions.assertEquals(cartao.getTipoCartao(), mostrarPagamentoDTO.cartaoPedido().tipoCartao());
		
		//Comparando MostrarProdutoDoPedidoDTO
		List<MostrarProdutoDoPedidoDTO> listaMostrarProdutoDoPedido = mostrarPedidoDTO.produtos();
		for (int i = 0; i < listaMostrarProdutoDoPedido.size(); i++) {
			Assertions.assertEquals(listaProdutos.get(i).getId(), listaMostrarProdutoDoPedido.get(i).idProduto());
			Assertions.assertEquals(listaProdutos.get(i).getNome(), listaMostrarProdutoDoPedido.get(i).nomeProduto());
			Assertions.assertEquals(listaProdutos.get(i).getPreco(), listaMostrarProdutoDoPedido.get(i).preco());
			Assertions.assertEquals(produtosCarrinho.get(i).quantidade(), listaMostrarProdutoDoPedido.get(i).quantidade());
			Assertions.assertNotNull(listaMostrarProdutoDoPedido.get(i).subTotal());	
			Assertions.assertEquals(arquivoInfoDTO.bytesArquivo(), listaMostrarProdutoDoPedido.get(i).primeiraImagem());
		}
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------
	
	@Test
	void pegarPedidosClientePorId() {
		//ARRANGE
		//Produto
		Produto produto = new Produto(1L, "Mouse gamer", "Descricao Mouse gamer", BigDecimal.valueOf(200.00), 100, true, null, null, null, null);
		Produto produto2 = new Produto(2L, "Teclado gamer", "Descricao Teclado gamer", BigDecimal.valueOf(200.00), 100, true, null, null, null, null);
		//ItemPedido
		List<ItemPedido> listaItemPedido = new ArrayList<>();
		List<ItemPedido> listaItemPedido2 = new ArrayList<>();
		ItemPedido itemPedido = new ItemPedido(1L, BigDecimal.valueOf(200.00), 5, "Mouse gamer", "Padrao.jpeg", BigDecimal.valueOf(1000.00), produto, null);
		ItemPedido itemPedido2 = new ItemPedido(2L, BigDecimal.valueOf(150.00), 3, "Teclado gamer", "Padrao.jpeg", BigDecimal.valueOf(600.00), produto2, null);
		listaItemPedido.addAll(List.of(itemPedido, itemPedido2));
		listaItemPedido2.add(itemPedido2);
		//Pagamento
		Pagamento pagamento = new Pagamento(1L, MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null, null, null);
		//Entrega
		EnderecoPedido enderecoPedido = new EnderecoPedido("12345678", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES");
		Entrega entrega = new Entrega(1L, TipoEntrega.CORREIOS, enderecoPedido, null);
		//Pedido
		List<Pedido> listaPedido = new ArrayList<>();
		Pedido pedido = new Pedido(1L, BigDecimal.valueOf(1450.00), LocalDateTime.now(), "Jucilene", "12345678912", StatusPedido.A_PAGAR, listaItemPedido, pagamento, entrega, cliente);
		Pedido pedido2 = new Pedido(2L, BigDecimal.valueOf(450.00), LocalDateTime.now(), "Jucilene", "12345678912", StatusPedido.A_PAGAR, listaItemPedido2, pagamento, entrega, cliente);
		listaPedido.addAll(List.of(pedido, pedido2));
		Page<Pedido> pagePedido = new PageImpl<>(listaPedido);
		
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Padrao.jpeg", bytesImagem);
		when(fileService.pegarImagemPedidoPorNome(any())).thenReturn(arquivoInfoDTO);
		Long idCliente = 1L;
		Cliente cliente = new Cliente(null, "12345678912", "Daniel", "Schiavo Rosseto", LocalDate.of(2000, 3, 3), LocalDate.now(), "daniel.schiavo35@gmail.com", "{noop}123456", "27996101055", null, null, null, null);
		BDDMockito.when(clienteService.verificarSeClienteExistePorId(idCliente)).thenReturn(cliente);
		Pageable pageable = PageRequest.of(0, 10);
		BDDMockito.when(pedidoRepository.findAllByCliente(cliente, pageable)).thenReturn(pagePedido);
		
		//ACT
		Page<MostrarPedidoDTO> pageMostrarPedidoDTO = pedidoService.pegarPedidosClientePorId(idCliente, pageable);
		
		//ASSERT
		Assertions.assertEquals(pagePedido.getTotalElements(), pageMostrarPedidoDTO.getTotalElements(), "O número total de elementos deve ser igual");
		List<MostrarPedidoDTO> listaMostrarPedidoDTO = pageMostrarPedidoDTO.getContent();

	    for (int i = 0; i < listaPedido.size(); i++) {
	        Pedido pedidoVerificar = listaPedido.get(i);
	        MostrarPedidoDTO dto = listaMostrarPedidoDTO.get(i);
	        
	        Assertions.assertEquals(pedidoVerificar.getCliente().getId(), dto.idCliente());
	        Assertions.assertEquals(pedidoVerificar.getValorTotal(), dto.valorTotal());
	        Assertions.assertEquals(pedidoVerificar.getDataPedido(), dto.dataPedido());
	        Assertions.assertEquals(pedidoVerificar.getStatusPedido(), dto.statusPedido());
	        
	        // Comparando entrega
	        Entrega entregaVerificar = pedidoVerificar.getEntrega();
	        MostrarEntregaDTO entregaDTO = dto.entrega();
	        Assertions.assertEquals(entregaVerificar.getTipoEntrega(), entregaDTO.tipoEntrega());
	        
	        // Comparando endereço de entrega
	        EnderecoPedido endereco = entregaVerificar.getEnderecoPedido();
	        MostrarEnderecoPedidoDTO enderecoDTO = entregaDTO.endereco();
	        if (endereco != null && enderecoDTO != null) {
	            Assertions.assertEquals(endereco.getCep(), enderecoDTO.cep());
	            Assertions.assertEquals(endereco.getRua(), enderecoDTO.rua());
	            Assertions.assertEquals(endereco.getNumero(), enderecoDTO.numero());
	            Assertions.assertEquals(endereco.getComplemento(), enderecoDTO.complemento());
	            Assertions.assertEquals(endereco.getBairro(), enderecoDTO.bairro());
	            Assertions.assertEquals(endereco.getCidade(), enderecoDTO.cidade());
	            Assertions.assertEquals(endereco.getEstado(), enderecoDTO.estado());
	        }
	        
	        // Comparando pagamento
	        Pagamento pagamentoVerificar = pedidoVerificar.getPagamento();
	        MostrarPagamentoDTO pagamentoDTO = dto.pagamento();
	        Assertions.assertEquals(pagamentoVerificar.getMetodoPagamento(), pagamentoDTO.metodoPagamento());
	        Assertions.assertEquals(pagamentoVerificar.getStatusPagamento(), pagamentoDTO.statusPagamento());
	        
	        // Comparando itens do pedido
	        List<ItemPedido> itensPedido = pedidoVerificar.getItemsPedido();
	        List<MostrarProdutoDoPedidoDTO> itensPedidoDTO = dto.produtos();
	        Assertions.assertEquals(itensPedido.size(), itensPedidoDTO.size(), "Os tamanhos das listas de itens do pedido devem ser iguais");
	        
	        for (int j = 0; j < itensPedido.size(); j++) {
	            ItemPedido item = itensPedido.get(j);
	            MostrarProdutoDoPedidoDTO itemDTO = itensPedidoDTO.get(j);
	            Assertions.assertEquals(item.getProduto().getId(), itemDTO.idProduto());
	            Assertions.assertEquals(item.getNomeProduto(), itemDTO.nomeProduto());
	            Assertions.assertEquals(item.getPreco(), itemDTO.preco());
	            Assertions.assertEquals(item.getQuantidade(), itemDTO.quantidade());
	            Assertions.assertEquals(item.getSubTotal(), itemDTO.subTotal());
	            Assertions.assertArrayEquals(bytesImagem, itemDTO.primeiraImagem());
	        }
	    }
	}

}
