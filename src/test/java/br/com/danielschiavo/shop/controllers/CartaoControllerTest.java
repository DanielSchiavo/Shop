package br.com.danielschiavo.shop.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.IOException;
import java.time.LocalDate;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.danielschiavo.shop.JwtUtilTest;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import br.com.danielschiavo.shop.models.cliente.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.CadastrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.services.CartaoService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class CartaoControllerTest {

	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();

	@MockBean
	private CartaoService cartaoService;
	
	@Autowired
	private JacksonTester<Page<MostrarCartaoDTO>> pageMostrarCartaoDTOJson;
	
	@Autowired
	private JacksonTester<MostrarCartaoDTO> mostrarCartaoDTOJson;
	
	@Autowired
	private JacksonTester<CadastrarCartaoDTO> cadastrarCartaoDTOJson;
	
	@Test
	@DisplayName("Deletar cartão por id token deve retornar http 204 quando token e idCartao válidos são enviados")
	void deletarCartaoPorIdToken_TokenEIdCartaoValido_DeveRetornarOkNoContent() throws IOException, Exception {
		doNothing().when(cartaoService).deletarCartaoPorIdToken(any());
		
		Long idCartao = 1L;
		
		var response = mvc.perform(delete("/shop/cliente/cartao/{idCartao}", idCartao)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar cartão por id token deve retornar http 401 quando token não é enviado")
	void deletarCartaoPorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idCartao = 1L;
		var response = mvc.perform(delete("/shop/cliente/cartao/{idCartao}", idCartao))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Pegar cartoes cliente por id token deve retornar http 200 quando token informado é válido")
	void pegarCartoesClientePorIdToken_TokenValido_DeveRetornarOk() throws IOException, Exception {
		MostrarCartaoDTO mostrarCartaoDTO = new MostrarCartaoDTO(null, "SANTANDER", "1234567812345678", TipoCartao.CREDITO, true);
		MostrarCartaoDTO mostrarCartaoDTO2 = new MostrarCartaoDTO(null, "CAIXA", "8765432187654321", TipoCartao.DEBITO, false);
		Page<MostrarCartaoDTO> pageMostrarCartao = new PageImpl<>(List.of(mostrarCartaoDTO, mostrarCartaoDTO2));
        when(cartaoService.pegarCartoesClientePorIdToken(any())).thenReturn(pageMostrarCartao);
        
		var response = mvc.perform(get("/shop/cliente/cartao")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageMostrarCartaoDTOJson.write(pageMostrarCartao).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Pegar cartoes cliente por id token deve retornar http 401 quando token não é enviado")
	void pegarCartoesClientePorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(get("/shop/cliente/cartao"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Cadastrar novo cartão por id token deve retornar http 200 quando token e dto estão validos")
	void cadastrarNovoCartaoPorIdToken_ClienteValidoComEndereco_DeveRetornarCreated() throws IOException, Exception {
		MostrarCartaoDTO mostrarCartaoDTO = new MostrarCartaoDTO(null, "SANTANDER", "1234567812345678", TipoCartao.CREDITO, true);
		when(cartaoService.cadastrarNovoCartaoPorIdToken(any())).thenReturn(mostrarCartaoDTO);
		
		var response = mvc.perform(post("/shop/cliente/cartao")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(cadastrarCartaoDTOJson.write(
										  new CadastrarCartaoDTO("03/29", "1234567812345678", "Jucelino Kubchecker", true, TipoCartao.CREDITO))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = mostrarCartaoDTOJson.write(mostrarCartaoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Cadastrar novo cartão por id token deve retornar http 401 quando token não é enviado")
	void cadastrarNovoCartaoPorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/cliente/cartao"))
										.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Alterar cartao padrao por id token deve retornar http 200 quando token e id cartão valido")
	void alterarCartaoPadraoPorIdToken_TokenEIdCartaoValido_DeveRetornarOk() throws IOException, Exception {
		doNothing().when(cartaoService).alterarCartaoPadraoPorIdToken(any());
		Long idCartao = 2L;
		var response = mvc.perform(put("/shop/cliente/cartao/{idCartao}", idCartao)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}
	
	@Test
	@DisplayName("Alterar cartao padrao por id token deve retornar http 401 quando token não é enviado")
	void alterarCartaoPadraoPorIdToken_TokenInvalido_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idCartao = 2L;
		var response = mvc.perform(put("/shop/cliente/cartao/{idCartao}", idCartao))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
