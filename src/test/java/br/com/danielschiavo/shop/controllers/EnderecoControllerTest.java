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
import java.time.LocalDate;
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
import br.com.danielschiavo.shop.models.cliente.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.endereco.AlterarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.CadastrarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.services.EnderecoService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class EnderecoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();
	
    @MockBean
    private EnderecoService enderecoService;
    
	@Autowired
	private JacksonTester<List<MostrarEnderecoDTO>> listMostrarEnderecoDTOJson;
	
	@Autowired
	private JacksonTester<MostrarEnderecoDTO> mostrarEnderecoDTOJson;
	
	@Autowired
	private JacksonTester<CadastrarEnderecoDTO> cadastrarEnderecoDTOJson;
	
	@Autowired
	private JacksonTester<AlterarEnderecoDTO> alterarEnderecoDTOJson;

	@Test
	@DisplayName("Deletar endereco por id token deve retornar http 204 quando token e idEndereco válidos são enviados")
	void deletarEnderecoPorIdToken_TokenValido_DeveRetornarNoContent() throws IOException, Exception {
		doNothing().when(enderecoService).deletarEnderecoPorIdToken(any());
		
		Long idEndereco = 1L;
		
		var response = mvc.perform(delete("/shop/cliente/endereco/{idEndereco}", idEndereco)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar endereco por id token deve retornar http 401 quando token não é enviado")
	void deletarEnderecoPorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idEndereco = 1L;
		
		var response = mvc.perform(delete("/shop/cliente/endereco/{idEndereco}", idEndereco))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Pegar enderecos cliente por id token deve retornar codigo http 200 quando enviado token correto")
	void pegarEnderecosClientePorIdToken_TokenValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEnderecoDTO mostrarEnderecoDTO = new MostrarEnderecoDTO(1L, "29142321", "15", "bela vista", "", "itapua", "vila velha", "es", true);
		MostrarEnderecoDTO mostrarEnderecoDTO2 = new MostrarEnderecoDTO(1L, "29142321", "15", "bela vista", "", "itapua", "vila velha", "es", true);
		List<MostrarEnderecoDTO> lista = new ArrayList<>();
		lista.add(mostrarEnderecoDTO);
		lista.add(mostrarEnderecoDTO2);
		when(enderecoService.pegarEnderecosClientePorIdToken()).thenReturn(lista);
		
		var response = mvc.perform(get("/shop/cliente/endereco")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = listMostrarEnderecoDTOJson.write(lista).getJson();
        
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Pegar enderecos cliente por id token deve retornar codigo http 401 quando token não é enviado")
	void pegarEnderecosClientePorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(get("/shop/cliente/endereco"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Cadastrar novo endereco deve retornar http 201 quando dto e token são validos")
	void cadastrarNovoEndereco_CadastroETokenValido_DeveRetornarCreated() throws IOException, Exception {
		MostrarEnderecoDTO mostrarEnderecoDTO = new MostrarEnderecoDTO(1L, "29142321", "15", "bela vista", "", "itapua", "vila velha", "es", true);
		when(enderecoService.cadastrarNovoEnderecoPorIdToken(any())).thenReturn(mostrarEnderecoDTO);
		
		var response = mvc.perform(post("/shop/cliente/endereco")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(cadastrarEnderecoDTOJson.write(
										  new CadastrarEnderecoDTO("29142286", "15", "bela vista", "", "itapua", "vila velha", "es", true))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = mostrarEnderecoDTOJson.write(mostrarEnderecoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Cadastrar novo endereco deve retornar http 401 quando token não é enviado")
	void cadastrarNovoEndereco_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/cliente/endereco"))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Alterar endereco cliente por id token deve retornar http 200 quando informacoes estão válidas")
	void alterarEnderecoPorIdToken_DtoETokenValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEnderecoDTO mostrarEnderecoDTO = new MostrarEnderecoDTO(1L, "29142321", "15", "bela vista", "", "itapua", "vila velha", "es", true);
		when(enderecoService.alterarEnderecoPorIdToken(any(), any())).thenReturn(mostrarEnderecoDTO);
		Long idEndereco = 1L;
		var response = mvc.perform(put("/shop/cliente/endereco/{idEndereco}", idEndereco)
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(alterarEnderecoDTOJson.write(
										  new AlterarEnderecoDTO("29142321", "15", "bela vista", "", "itapua", "vila velha", "es", true))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = mostrarEnderecoDTOJson.write(mostrarEnderecoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Alterar endereco cliente por id token deve retornar http 200 quando informacoes estão válidas")
	void alterarEnderecoPorIdToken_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idEndereco = 1L;
		var response = mvc.perform(put("/shop/cliente/endereco/{idEndereco}", idEndereco)
								  .contentType(MediaType.APPLICATION_JSON))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

}
