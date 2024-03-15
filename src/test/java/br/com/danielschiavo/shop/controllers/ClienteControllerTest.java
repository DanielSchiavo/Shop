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
import br.com.danielschiavo.shop.models.cliente.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.AlterarFotoPerfilDTO;
import br.com.danielschiavo.shop.models.cliente.CadastrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClientePaginaInicialDTO;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.services.ClienteService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class ClienteControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();

	@Autowired
	private JacksonTester<CadastrarClienteDTO> cadastrarClienteDTOJson;
	
	@Autowired
	private JacksonTester<MostrarClienteDTO> mostrarClienteDTOJson;
	
	@Autowired
	private JacksonTester<Page<MostrarClienteDTO>> pageMostrarClienteDTOJson;
	
	@Autowired
	private JacksonTester<MostrarClientePaginaInicialDTO> mostrarClientePaginaInicialDTOJson;
	
	@Autowired
	private JacksonTester<AlterarClienteDTO> alterarClienteDTOJson;
	
	@Autowired
	private JacksonTester<AlterarFotoPerfilDTO> alterarFotoPerfilDTOJson;
	
	@Autowired
	private JacksonTester<ArquivoInfoDTO> arquivoInfoDTOJson;
	
    @MockBean
    private ClienteService clienteService;
    
	@Test
	@DisplayName("Deletar foto perfil deve retornar http 204 quando token válido é enviado")
	void deletarFotoPerfil_ClienteValido_DeveRetornarOkNoContent() throws IOException, Exception {
		doNothing().when(clienteService).deletarFotoPerfilPorIdToken();
		
		var response = mvc.perform(delete("/shop/cliente/foto-perfil")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar foto perfil deve retornar http 401 quando token não é enviado")
	void deletarFotoPerfil_ClienteInvalido_DeveRetornarForbidden() throws IOException, Exception {
		doNothing().when(clienteService).deletarFotoPerfilPorIdToken();
		
		var response = mvc.perform(delete("/shop/cliente/foto-perfil"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
    
	@Test
	@DisplayName("Detalhar cliente página inicial deve retornar codigo http 201 quando token válido é enviado")
	void detalharClientePaginaInicial_ClienteValido_DeveRetornarOk() throws IOException, Exception {
		MostrarClientePaginaInicialDTO mostrarClientePaginaInicialDTO = new MostrarClientePaginaInicialDTO("Daniel", new ArquivoInfoDTO("Padrao.jpeg", null));
		when(clienteService.detalharClientePorIdTokenPaginaInicial()).thenReturn(mostrarClientePaginaInicialDTO);
		
		var response = mvc.perform(get("/shop/cliente/pagina-inicial")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = mostrarClientePaginaInicialDTOJson.write(mostrarClientePaginaInicialDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Detalhar cliente página inicial deve retornar codigo http 401 quando token não é enviado")
	void detalharClientePaginaInicial_ClienteInvalido_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(get("/shop/cliente/pagina-inicial"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	@DisplayName("Detalhar cliente deve retornar http 200 quando token enviado é válido")
	void detalharCliente_ClienteValido_DeveRetornarOk() throws IOException, Exception {
		MostrarEnderecoDTO mostrarEnderecoDTO = new MostrarEnderecoDTO(null, "29142298", "divinopolis", "35", null, "bela vista", "divino", "es", true);
		List<MostrarEnderecoDTO> lista = new ArrayList<>();
		lista.add(mostrarEnderecoDTO);
		var mostrarClienteDTO = new MostrarClienteDTO(null, "12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "juniordasilva@gmail.com", "123456", "27996101055", null, lista, null);
		when(clienteService.detalharClientePorIdToken()).thenReturn(mostrarClienteDTO);
		
		var response = mvc.perform(get("/shop/cliente")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = mostrarClienteDTOJson.write(mostrarClienteDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Detalhar cliente deve retornar http 401 quando token não é enviado")
	void detalharCliente_ClienteInvalido_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(get("/shop/cliente"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Cadastrar cliente deve retornar http 201 quando informacoes estão válidas sem endereço")
	void cadastrarCliente_ClienteValidoSemEndereco_DeveRetornarCreated() throws IOException, Exception {
		var mostrarClienteDTO = new MostrarClienteDTO(null, "12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "juniordasilva@gmail.com", "123456", "27996101055", null, null, null);
		when(clienteService.cadastrarCliente(any())).thenReturn(mostrarClienteDTO);
		
		var response = mvc.perform(post("/shop/publico/cadastrar/cliente")
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(cadastrarClienteDTOJson.write(
										  new CadastrarClienteDTO("12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), "juniordasilva@gmail.com", "123456", "27996101055", "Padrao.jpeg", null))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = mostrarClienteDTOJson.write(mostrarClienteDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Cadastrar cliente deve retornar http 201 quando informacoes estão válidas com endereço")
	void cadastrarCliente_ClienteValidoComEndereco_DeveRetornarCreated() throws IOException, Exception {
		MostrarEnderecoDTO mostrarEnderecoDTO = new MostrarEnderecoDTO(null, "29142298", "divinopolis", "35", null, "bela vista", "divino", "es", true);
		List<MostrarEnderecoDTO> lista = new ArrayList<>();
		lista.add(mostrarEnderecoDTO);
		var mostrarClienteDTO = new MostrarClienteDTO(null, "12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "juniordasilva@gmail.com", "123456", "27996101055", null, lista, null);
		when(clienteService.cadastrarCliente(any())).thenReturn(mostrarClienteDTO);
		
		var response = mvc.perform(post("/shop/publico/cadastrar/cliente")
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(cadastrarClienteDTOJson.write(
										  new CadastrarClienteDTO("12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), "juniordasilva@gmail.com", "123456", "27996101055", "Padrao.jpeg", null))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = mostrarClienteDTOJson.write(mostrarClienteDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}

    @Test
    @DisplayName("Cadastrar cliente deve retornar http 400 BAD_REQUEST enviando corpo requisição nulo")
    void cadastrarCliente_ClienteInvalido_DeveRetornarBadRequest() throws Exception {
        var response = mvc.perform(post("/shop/publico/cadastrar/cliente"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

	@Test
	@DisplayName("Alterar cliente deve retornar http 200 quando informacoes estão válidas")
	void alterarClientePorId_ClienteValido_DeveRetornarOk() throws IOException, Exception {
		var mostrarClienteDTO = new MostrarClienteDTO(null, "12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "juniordasilva@gmail.com", "123456", "27996101055", null, null, null);
		when(clienteService.alterarClientePorIdToken(any())).thenReturn(mostrarClienteDTO);
		
		var response = mvc.perform(put("/shop/cliente")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(alterarClienteDTOJson.write(
										  new AlterarClienteDTO("12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), "juniordasilva@gmail.com", "123456", "27996101055"))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = mostrarClienteDTOJson.write(mostrarClienteDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Alterar cliente deve retornar http 401 quando token não é enviado")
	void alterarClientePorId_ClienteInvalido_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(put("/shop/cliente")
								  .contentType(MediaType.APPLICATION_JSON))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	@DisplayName("Detalhar cliente página inicial deve retornar codigo http 200 quando token e corpo da requisição é válido enviado")
	void alterarFotoPerfilPorIdToken_ClienteValido_DeveRetornarOk() throws IOException, Exception {
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Nome", null);
		when(clienteService.alterarFotoPerfilPorIdToken(any())).thenReturn(arquivoInfoDTO);

		var response = mvc.perform(put("/shop/cliente/foto-perfil")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(alterarFotoPerfilDTOJson.write(
										  new AlterarFotoPerfilDTO("nomeNovaImagem.jpeg"))
										  .getJson())
								  )
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = arquivoInfoDTOJson.write(arquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Detalhar cliente página inicial deve retornar codigo http 400 quando token válido e corpo inválido é enviado")
	void alterarFotoPerfilPorIdToken_ClienteInvalido_DeveRetornarBadRequest() throws IOException, Exception {
		var response = mvc.perform(put("/shop/cliente/foto-perfil")
								  .header("Authorization", "Bearer " + tokenUser)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(alterarFotoPerfilDTOJson.write(
										  new AlterarFotoPerfilDTO(""))
										  .getJson())
								  )
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
	
	
//	------------------------------
//	------------------------------
//	ENDPOINTS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------
	
	@Test
	@DisplayName("Admin deletar cliente deve retornar http 201 quando informacoes estão válidas")
	void deletarClientePorId_AdminValido_DeveRetornarNoContent() throws IOException, Exception {
		doNothing().when(clienteService).adminDeletarClientePorId(any());
		Long idCliente = 2L;
		var response = mvc.perform(delete("/shop/admin/cliente/{idCliente}", idCliente)
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Admin deletar cliente deve retornar http 403 quando não-administradores tentarem usar o endpoint")
	void deletarClientePorId_AdminInvalido_DeveRetornarForbidden() throws IOException, Exception {
		doNothing().when(clienteService).adminDeletarClientePorId(any());
		Long idCliente = 3L;
		var response = mvc.perform(delete("/shop/admin/cliente/{idCliente}", idCliente)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Admin detalhar todos clientes deve retornar http 200 quando token informado é válido")
	void adminDetalharTodosClientes_AdminValido_DeveRetornarOk() throws IOException, Exception {
		var mostrarClienteDTO = new MostrarClienteDTO(null, "12345671012", "Junior", "da Silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "juniordasilva@gmail.com", "123456", "27996101055", null, null, null);
		var mostrarClienteDTO2 = new MostrarClienteDTO(null, "12345612342", "Anderson", "Emiliano", LocalDate.of(2000, 2, 6), LocalDate.now(), "jorlan@gmail.com", "123456", "27999833653", null, null, null);
		Page<MostrarClienteDTO> pageCliente = new PageImpl<>(List.of(mostrarClienteDTO, mostrarClienteDTO2));
        when(clienteService.adminDetalharTodosClientes(any())).thenReturn(pageCliente);
        
		var response = mvc.perform(get("/shop/admin/cliente")
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageMostrarClienteDTOJson.write(pageCliente).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Admin detalhar todos clientes deve retornar http 403 quando não-administradores tentarem usar o endpoint")
	void adminDetalharTodosClientes_AdminInvalido_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(get("/shop/admin/cliente")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}

}
