package br.com.danielschiavo.shop.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.danielschiavo.shop.JwtUtilTest;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.pedido.CriarPagamentoDTO;
import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.MostrarProdutoDoPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.pedido.entrega.CriarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.entrega.MostrarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.MostrarPagamentoDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.StatusPagamento;
import br.com.danielschiavo.shop.services.ClienteService;
import br.com.danielschiavo.shop.services.PedidoService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class PedidoControllerTest {

	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();
	
    @MockBean
    private PedidoService pedidoService;
    
	@Autowired
	private JacksonTester<Page<MostrarPedidoDTO>> pageMostrarPedidoDTOJson;
	
	@Autowired
	private JacksonTester<MostrarPedidoDTO> mostrarPedidoDTOJson;
	
	@Autowired
	private JacksonTester<CriarPedidoDTO> criarPedidoDTOJson;
	
	@Test
	@DisplayName("Pegar pedidos cliente por id token deve retornar http 200 quando token enviado é válido")
	void pegarPedidosClientePorIdToken_ClienteValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEntregaDTO mostrarEntregaDTO = new MostrarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		MostrarPagamentoDTO mostrarPagamentoDTO = new MostrarPagamentoDTO(MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null);
		byte[] bytesImagem = "Hello world".getBytes();
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO = new MostrarProdutoDoPedidoDTO("Produto 1", BigDecimal.valueOf(400.00), 2, bytesImagem);
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO2 = new MostrarProdutoDoPedidoDTO("Produto 2", BigDecimal.valueOf(200.00), 2, bytesImagem);
		List<MostrarProdutoDoPedidoDTO> produtos = new ArrayList<>();
		produtos.add(mostrarProdutoDoPedidoDTO);
		produtos.add(mostrarProdutoDoPedidoDTO2);
		MostrarPedidoDTO mostrarPedidoDTO = new MostrarPedidoDTO(2L, BigDecimal.valueOf(400.00), LocalDateTime.now(), StatusPedido.A_PAGAR, mostrarEntregaDTO, mostrarPagamentoDTO, produtos);
		Page<MostrarPedidoDTO> pageMostrarPedidoDTO = new PageImpl<>(List.of(mostrarPedidoDTO));
		
		when(pedidoService.pegarPedidosClientePorIdToken(any())).thenReturn(pageMostrarPedidoDTO);
		
		var response = mvc.perform(get("/shop/cliente/pedido")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageMostrarPedidoDTOJson.write(pageMostrarPedidoDTO).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Pegar pedidos cliente por id token deve retornar http 401 quando token não é enviado")
	void pegarPedidosClientePorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(get("/shop/cliente/pedido"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Criar pedido botao comprar agora e comprar do carrinho por id token deve retornar http 200 quando token e dto enviado são válido")
	void criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken_PeloCarrinhoETokenValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEntregaDTO mostrarEntregaDTO = new MostrarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		MostrarPagamentoDTO mostrarPagamentoDTO = new MostrarPagamentoDTO(MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null);
		byte[] bytesImagem = "Hello world".getBytes();
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO = new MostrarProdutoDoPedidoDTO("Produto 1", BigDecimal.valueOf(400.00), 2, bytesImagem);
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO2 = new MostrarProdutoDoPedidoDTO("Produto 2", BigDecimal.valueOf(200.00), 2, bytesImagem);
		List<MostrarProdutoDoPedidoDTO> produtos = new ArrayList<>();
		produtos.add(mostrarProdutoDoPedidoDTO);
		produtos.add(mostrarProdutoDoPedidoDTO2);
		MostrarPedidoDTO mostrarPedidoDTO = new MostrarPedidoDTO(2L, BigDecimal.valueOf(400.00), LocalDateTime.now(), StatusPedido.A_PAGAR, mostrarEntregaDTO, mostrarPagamentoDTO, produtos);
		Page<MostrarPedidoDTO> pageMostrarPedidoDTO = new PageImpl<>(List.of(mostrarPedidoDTO));
		when(pedidoService.pegarPedidosClientePorIdToken(any())).thenReturn(pageMostrarPedidoDTO);
		
		CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(MetodoPagamento.BOLETO, null, null);
		CriarEntregaDTO criarEntregaDTO = new CriarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		String json = criarPedidoDTOJson.write(new CriarPedidoDTO(criarPagamentoDTO, criarEntregaDTO, null)).getJson();
		
		var response = mvc.perform(get("/shop/cliente/pedido")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(json))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageMostrarPedidoDTOJson.write(pageMostrarPedidoDTO).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Criar pedido botao comprar agora e comprar do carrinho por id token deve retornar http 200 quando token e dto enviado são válido")
	void criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken_BotaoComprarAgoraETokenValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEntregaDTO mostrarEntregaDTO = new MostrarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		MostrarPagamentoDTO mostrarPagamentoDTO = new MostrarPagamentoDTO(MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null);
		byte[] bytesImagem = "Hello world".getBytes();
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO = new MostrarProdutoDoPedidoDTO("Produto 1", BigDecimal.valueOf(400.00), 2, bytesImagem);
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO2 = new MostrarProdutoDoPedidoDTO("Produto 2", BigDecimal.valueOf(200.00), 2, bytesImagem);
		List<MostrarProdutoDoPedidoDTO> produtos = new ArrayList<>();
		produtos.add(mostrarProdutoDoPedidoDTO);
		produtos.add(mostrarProdutoDoPedidoDTO2);
		MostrarPedidoDTO mostrarPedidoDTO = new MostrarPedidoDTO(2L, BigDecimal.valueOf(400.00), LocalDateTime.now(), StatusPedido.A_PAGAR, mostrarEntregaDTO, mostrarPagamentoDTO, produtos);
		when(pedidoService.criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken(any())).thenReturn(mostrarPedidoDTO);
		
		CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(MetodoPagamento.BOLETO, null, null);
		CriarEntregaDTO criarEntregaDTO = new CriarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		String json = criarPedidoDTOJson.write(new CriarPedidoDTO(criarPagamentoDTO, criarEntregaDTO, null)).getJson();
		
		var response = mvc.perform(post("/shop/cliente/pedido")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(json))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = mostrarPedidoDTOJson.write(mostrarPedidoDTO).getJson();
        
        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Criar pedido botao comprar agora e comprar do carrinho por id token deve retornar http 200 quando token e dto enviado são válido")
	void criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken_CarrinhoETokenValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEntregaDTO mostrarEntregaDTO = new MostrarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		MostrarPagamentoDTO mostrarPagamentoDTO = new MostrarPagamentoDTO(MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null);
		byte[] bytesImagem = "Hello world".getBytes();
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO = new MostrarProdutoDoPedidoDTO("Produto 1", BigDecimal.valueOf(400.00), 2, bytesImagem);
		List<MostrarProdutoDoPedidoDTO> produtos = new ArrayList<>();
		produtos.add(mostrarProdutoDoPedidoDTO);
		MostrarPedidoDTO mostrarPedidoDTO = new MostrarPedidoDTO(2L, BigDecimal.valueOf(400.00), LocalDateTime.now(), StatusPedido.A_PAGAR, mostrarEntregaDTO, mostrarPagamentoDTO, produtos);
		when(pedidoService.criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken(any())).thenReturn(mostrarPedidoDTO);
		
		CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(MetodoPagamento.BOLETO, null, null);
		CriarEntregaDTO criarEntregaDTO = new CriarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		AdicionarItemPedidoDTO adicionarItemPedidoDTO = new AdicionarItemPedidoDTO(1L, 2);
		String json = criarPedidoDTOJson.write(new CriarPedidoDTO(criarPagamentoDTO, criarEntregaDTO, adicionarItemPedidoDTO)).getJson();
		
		var response = mvc.perform(post("/shop/cliente/pedido")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(json))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = mostrarPedidoDTOJson.write(mostrarPedidoDTO).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Criar pedido botao comprar agora e comprar do carrinho por id token deve retornar http 401 quando token não é enviado")
	void criarPedidoBotaoComprarAgoraEComprarDoCarrinhoPorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/cliente/pedido"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	
//	------------------------------
//	------------------------------
//	ENDPOINTS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------
	
	@Test
	@DisplayName("Pegar pedidos cliente por id token deve retornar http 200 quando token enviado é válido")
	void pegarPedidosClientePorId_ClienteETokenAdminValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEntregaDTO mostrarEntregaDTO = new MostrarEntregaDTO(TipoEntrega.RETIRADA_NA_LOJA, null);
		MostrarPagamentoDTO mostrarPagamentoDTO = new MostrarPagamentoDTO(MetodoPagamento.BOLETO, StatusPagamento.PENDENTE, null);
		byte[] bytesImagem = "Hello world".getBytes();
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO = new MostrarProdutoDoPedidoDTO("Produto 1", BigDecimal.valueOf(400.00), 2, bytesImagem);
		MostrarProdutoDoPedidoDTO mostrarProdutoDoPedidoDTO2 = new MostrarProdutoDoPedidoDTO("Produto 2", BigDecimal.valueOf(200.00), 2, bytesImagem);
		List<MostrarProdutoDoPedidoDTO> produtos = new ArrayList<>();
		produtos.add(mostrarProdutoDoPedidoDTO);
		produtos.add(mostrarProdutoDoPedidoDTO2);
		MostrarPedidoDTO mostrarPedidoDTO = new MostrarPedidoDTO(2L, BigDecimal.valueOf(400.00), LocalDateTime.now(), StatusPedido.A_PAGAR, mostrarEntregaDTO, mostrarPagamentoDTO, produtos);
		Page<MostrarPedidoDTO> pageMostrarPedidoDTO = new PageImpl<>(List.of(mostrarPedidoDTO));
		
		when(pedidoService.pegarPedidosClientePorId(any(), any())).thenReturn(pageMostrarPedidoDTO);
		
		Long idCliente = 2L;
		var response = mvc.perform(get("/shop/admin/pedido/{idCliente}", idCliente)
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageMostrarPedidoDTOJson.write(pageMostrarPedidoDTO).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Pegar pedidos cliente por id token deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void pegarPedidosClientePorId_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		Long idCliente = 2L;
		var response = mvc.perform(get("/shop/admin/pedido/{idCliente}", idCliente)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Pegar pedidos cliente por id token deve retornar http 401 quando nenhum token é enviado")
	void pegarPedidosClientePorId_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idCliente = 2L;
		var response = mvc.perform(get("/shop/admin/pedido/{idCliente}", idCliente))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
