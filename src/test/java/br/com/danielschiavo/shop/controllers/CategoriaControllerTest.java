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
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.categoria.CriarCategoriaDTO;
import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaDTO;
import br.com.danielschiavo.shop.services.CategoriaService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class CategoriaControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();
	
	@Autowired
	private JacksonTester<Page<Categoria>> pageCategoriaJson;
	
	@Autowired
	private JacksonTester<CriarCategoriaDTO> criarCategoriaDTOJson;
	
	@Autowired
	private JacksonTester<MostrarCategoriaDTO> mostrarCategoriaDTOJson;
	
	@MockBean
	private CategoriaService categoriaService;
	
	@Test
	@DisplayName("Listar categorias deve retornar codigo http 200 quando qualquer usuário solicitar o endpoint")
	void listarCategorias_QualquerUsuario_DeveRetornarOk() throws IOException, Exception {
		Categoria categoria = new Categoria(null, "Computadores", null);
		Categoria categoria2 = new Categoria(null, "Celulares", null);
		Page<Categoria> pageCategorias = new PageImpl<>(List.of(categoria, categoria2));
		when(categoriaService.listarCategorias(any())).thenReturn(pageCategorias);
		
		var response = mvc.perform(get("/shop/publico/categoria"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageCategoriaJson.write(pageCategorias).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	
//	------------------------------
//	------------------------------
//	ENDPOINTS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------

	@Test
	@DisplayName("Deletar categoria por id deve retornar http 204 quando token e id de categoria válido são enviados")
	void deletarCategoriaPorId_TokenAdminEIdCategoriaValido_DeveRetornarOkNoContent() throws IOException, Exception {
		doNothing().when(categoriaService).deletarCategoriaPorId(any());
		Long idCategoria = 2L;
		
		var response = mvc.perform(delete("/shop/admin/categoria/{idCategoria}", idCategoria)
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar categoria por id deve retornar http 401 quando token não é enviado")
	void deletarCategoriaPorId_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idCategoria = 2L;
		
		var response = mvc.perform(delete("/shop/admin/categoria/{idCategoria}", idCategoria))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Deletar categoria por id deve retornar http 403 quando token de user normal é enviado")
	void deletarCategoriaPorId_TokenUserEnviado_DeveRetornarForbidden() throws IOException, Exception {
		Long idCategoria = 2L;
		
		var response = mvc.perform(delete("/shop/admin/categoria/{idCategoria}", idCategoria)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
    @Test
    @DisplayName("Criar categoria deve retornar http 201 quando dados válidos são enviados")
    void criarCategoria_DadosValidos_DeveRetornarCreated() throws Exception {
        CriarCategoriaDTO criarCategoriaDTO = new CriarCategoriaDTO("Eletronicos");
        MostrarCategoriaDTO mostrarCategoriaDTO = new MostrarCategoriaDTO(1L, "Eletronicos");

        when(categoriaService.criarCategoria(any())).thenReturn(mostrarCategoriaDTO);

		var response = mvc.perform(post("/shop/admin/categoria")
				  .header("Authorization", "Bearer " + tokenAdmin)
				  .contentType(MediaType.APPLICATION_JSON)
				  .content(criarCategoriaDTOJson.write(criarCategoriaDTO)
						  .getJson())
				  )
		.andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        
        var jsonEsperado = mostrarCategoriaDTOJson.write(mostrarCategoriaDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    
    @Test
    @DisplayName("Criar categoria deve retornar http 403 quando token de usuário comum é enviado")
    void criarCategoria_TokenUser_DeveRetornarForbidden() throws Exception {
        CriarCategoriaDTO categoriaDTO = new CriarCategoriaDTO("Eletrônicos");

        var response = mvc.perform(post("/shop/admin/categoria")
        		.header("Authorization", "Bearer " + tokenUser) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(criarCategoriaDTOJson.write(categoriaDTO).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
    
    @Test
    @DisplayName("Criar categoria deve retornar http 401 quando nenhum token é enviado")
    void criarCategoria_TokenNaoEnviado_DeveRetornarUnauthorized() throws Exception {
        CriarCategoriaDTO categoriaDTO = new CriarCategoriaDTO("Eletrônicos");

        var response = mvc.perform(post("/shop/admin/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(criarCategoriaDTOJson.write(categoriaDTO).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("Alterar o nome da categoria por ID deve retornar http 200 quando dados válidos são enviados")
    void alterarNomeCategoriaPorId_DadosValidos_DeveRetornarOk() throws Exception {
        Long idCategoria = 1L;
        CriarCategoriaDTO criarCategoriaDTO = new CriarCategoriaDTO("Tecnologia");
        MostrarCategoriaDTO mostrarCategoriaDTO = new MostrarCategoriaDTO(idCategoria, "Tecnologia");

        when(categoriaService.alterarNomeCategoriaPorId(any(), any())).thenReturn(mostrarCategoriaDTO);

        var response = mvc.perform(put("/shop/admin/categoria/{idCategoria}", idCategoria)
        						.header("Authorization", "Bearer " + tokenAdmin)
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(criarCategoriaDTOJson.write(criarCategoriaDTO)
				                		.getJson())
				                )
				                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        
        var jsonEsperado = mostrarCategoriaDTOJson.write(mostrarCategoriaDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
    
    @Test
    @DisplayName("Alterar o nome da categoria por ID deve retornar http 403 quando token de usuário comum é enviado")
    void alterarNomeCategoriaPorId_TokenUser_DeveRetornarForbidden() throws Exception {
        Long idCategoria = 1L;
        CriarCategoriaDTO categoriaDTO = new CriarCategoriaDTO("Tecnologia");

        var response = mvc.perform(put("/shop/admin/categoria/{idCategoria}", idCategoria)
        		.header("Authorization", "Bearer " + tokenUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(criarCategoriaDTOJson.write(categoriaDTO).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
    
    @Test
    @DisplayName("Alterar o nome da categoria por ID deve retornar http 401 quando nenhum token é enviado")
    void alterarNomeCategoriaPorId_SemToken_DeveRetornarUnauthorized() throws Exception {
        Long idCategoria = 1L;
        CriarCategoriaDTO categoriaDTO = new CriarCategoriaDTO("Tecnologia");

        var response = mvc.perform(put("/shop/admin/categoria/{idCategoria}", idCategoria)
                .contentType(MediaType.APPLICATION_JSON)
                .content(criarCategoriaDTOJson.write(categoriaDTO).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
