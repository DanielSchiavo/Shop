package br.com.danielschiavo.shop.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.danielschiavo.shop.JwtUtilTest;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.MostrarItemCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.services.CarrinhoService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class CarrinhoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();

	@MockBean
	private CarrinhoService carrinhoService;
	
	@Autowired
	private JacksonTester<MostrarCarrinhoClienteDTO> mostrarCarrinhoClienteDTOJson;
	
	@Autowired
	private JacksonTester<ItemCarrinhoDTO> itemCarrinhoDTOJson;

	@Test
	@DisplayName("Deletar produto no carrinho deve retornar http 204 quando token e idProduto válidos são enviados")
	void deletarProdutoNoCarrinhoPorIdToken_TokenValido_DeveRetornarOkNoContent() throws IOException, Exception {
		doNothing().when(carrinhoService).deletarProdutoNoCarrinhoPorIdToken(any());
		
		Long idProduto = 1L;
		
		var response = mvc.perform(delete("/shop/cliente/carrinho/{idProduto}", idProduto)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar produto no carrinho deve retornar http 401 quando token inválido é enviado")
	void deletarProdutoNoCarrinhoPorIdToken_TokenInvalido_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idProduto = 1L;
		var response = mvc.perform(delete("/shop/cliente/carrinho/{idProduto}", idProduto))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Pegar carrinho cliente por id token deve retornar codigo http 200 quando token é valido")
	void pegarCarrinhoClientePorIdToken_TokenValido_DeveRetornarOk() throws IOException, Exception {
		var mostrarItemCarrinhoClienteDTO = new MostrarItemCarrinhoClienteDTO(1L, "Produto1", 2, BigDecimal.valueOf(200.00), null);
		var mostrarItemCarrinhoClienteDTO2 = new MostrarItemCarrinhoClienteDTO(1L, "Produto2", 2, BigDecimal.valueOf(300.00), null);
		List<MostrarItemCarrinhoClienteDTO> lista = new ArrayList<>();
		lista.add(mostrarItemCarrinhoClienteDTO);
		lista.add(mostrarItemCarrinhoClienteDTO2);
		BigDecimal valorTotal = BigDecimal.ZERO;
		lista.forEach(item -> {
			valorTotal.add(item.preco());
			
		});
		MostrarCarrinhoClienteDTO mostrarCarrinhoClienteDTO = new MostrarCarrinhoClienteDTO(2L, lista, valorTotal);
		
		when(carrinhoService.pegarCarrinhoClientePorIdToken()).thenReturn(mostrarCarrinhoClienteDTO);
		
		var response = mvc.perform(get("/shop/cliente/carrinho")
				  				.header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = mostrarCarrinhoClienteDTOJson.write(mostrarCarrinhoClienteDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Pegar carrinho cliente por id token deve retornar codigo http 401 quando token não é enviado")
	void pegarCarrinhoClientePorIdToken_TokenInvalido_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(get("/shop/cliente/carrinho"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Adicionar produtos no carrinho por id token deve retornar http 200 quando token e dto são válidos")
	void adicionarProdutosNoCarrinhoPorIdToken_TokenValido_DeveRetornarOk() throws IOException, Exception {
		
		var response = mvc.perform(post("/shop/cliente/carrinho")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(itemCarrinhoDTOJson.write(new ItemCarrinhoDTO(1L, 2))
								  .getJson()))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("Adicionar produtos no carrinho por id token deve retornar http 401 quando token não é enviado")
	void adicionarProdutosNoCarrinhoPorIdToken_TokenInvalido_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/cliente/carrinho"))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Setar quantidade produto no carrinho por id token deve retornar http 200 quando token e corpo validos são informado")
	void setarQuantidadeProdutoNoCarrinhoPorIdToken_TokenEBodyValido_DeveRetornarOk() throws IOException, Exception {
		doNothing().when(carrinhoService).setarQuantidadeProdutoNoCarrinhoPorIdToken(any());
		
		var response = mvc.perform(put("/shop/cliente/carrinho")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(itemCarrinhoDTOJson.write(new ItemCarrinhoDTO(1L, 2))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("Setar quantidade produto no carrinho por id token deve retornar http 401 quando token não é informado")
	void setarQuantidadeProdutoNoCarrinhoPorIdToken_TokenInvalido_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(put("/shop/cliente/carrinho"))
								.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
