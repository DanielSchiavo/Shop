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
import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.AlterarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.CadastrarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaComCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.services.SubCategoriaService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class SubCategoriaControllerTest {

	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();
	
	@Autowired
	private JacksonTester<Page<MostrarSubCategoriaComCategoriaDTO>> pageMostrarSubCategoriaComCategoriaDTOJson;
	
	@Autowired
	private JacksonTester<CadastrarSubCategoriaDTO> cadastrarSubCategoriaDTOJson;
	
	@Autowired
	private JacksonTester<AlterarSubCategoriaDTO> alterarSubCategoriaDTOJson;
	
	@MockBean
	private SubCategoriaService subCategoriaService;
	
	@Test
	@DisplayName("Listar categorias deve retornar codigo http 200 quando qualquer usuário solicitar o endpoint")
	void listarSubCategorias_DeveRetornarOk() throws IOException, Exception {
		var categoria = new MostrarCategoriaDTO(null, "Computadores");
		var categoria2 = new MostrarCategoriaDTO(null, "Celulares");
		var mostrarSubCategoriaComCategoriaDTO = new MostrarSubCategoriaComCategoriaDTO(null, "Mouses", categoria);
		var mostrarSubCategoriaComCategoriaDTO2 = new MostrarSubCategoriaComCategoriaDTO(null, "Peliculas", categoria2);
		var pageMostrarSubCategoriaComCategoriaDTO = new PageImpl<>(List.of(mostrarSubCategoriaComCategoriaDTO, mostrarSubCategoriaComCategoriaDTO2));
		when(subCategoriaService.listarSubCategorias(any())).thenReturn(pageMostrarSubCategoriaComCategoriaDTO);
		
		var response = mvc.perform(get("/shop/publico/sub-categoria"))
								  		.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageMostrarSubCategoriaComCategoriaDTOJson.write(pageMostrarSubCategoriaComCategoriaDTO).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Deletar sub categoria por id deve retornar http 204 quando token e id de sub categoria válido são enviados")
	void deletarSubCategoriaPorId_TokenAdminEIdSubCategoriaValido_DeveRetornarOkNoContent() throws IOException, Exception {
		doNothing().when(subCategoriaService).deletarSubCategoriaPorId(any());
		Long idCategoria = 2L;
		
		var response = mvc.perform(delete("/shop/admin/sub-categoria/{idSubCategoria}", idCategoria)
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar sub categoria por id deve retornar http 403 quando token não é enviado")
	void deletarSubCategoriaPorId_TokenNaoEnviado_DeveRetornarForbidden() throws IOException, Exception {
		Long idCategoria = 2L;
		
		var response = mvc.perform(delete("/shop/admin/sub-categoria/{idSubCategoria}", idCategoria))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Deletar sub categoria por id deve retornar http 403 quando token de user normal é enviado")
	void deletarSubCategoriaPorId_TokenUserEnviado_DeveRetornarForbidden() throws IOException, Exception {
		Long idCategoria = 2L;
		
		var response = mvc.perform(delete("/shop/admin/sub-categoria/{idSubCategoria}", idCategoria)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
    @Test
    @DisplayName("Criar sub categoria deve retornar http 201 quando dados válidos são enviados")
    void cadastrarSubCategoria_DtoETokenAdminValido_DeveRetornarCreated() throws Exception {
    	MostrarSubCategoriaDTO mostrarSubCategoriaDTO = new MostrarSubCategoriaDTO(1L, "Mouses");
        when(subCategoriaService.alterarSubCategoriaPorId(any(), any())).thenReturn(mostrarSubCategoriaDTO);

		var response = mvc.perform(post("/shop/admin/sub-categoria")
				  .header("Authorization", "Bearer " + tokenAdmin)
				  .contentType(MediaType.APPLICATION_JSON)
				  .content(cadastrarSubCategoriaDTOJson.write(
						  new CadastrarSubCategoriaDTO("Mouses", 1L))
						  .getJson()))
				  .andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }
    
    @Test
    @DisplayName("Criar sub categoria deve retornar http 403 quando token de usuario é enviado")
    void cadastrarSubCategoria_TokenUser_DeveRetornarForbidden() throws Exception {
		var response = mvc.perform(post("/shop/admin/sub-categoria")
				  .header("Authorization", "Bearer " + tokenUser))
				  .andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
    
    @Test
    @DisplayName("Criar sub categoria deve retornar http 403 quando token não é enviado")
    void cadastrarSubCategoria_TokenNaoEnviado_DeveRetornarForbidden() throws Exception {
		var response = mvc.perform(post("/shop/admin/sub-categoria"))
				  .andReturn().getResponse();
        
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
    
    @Test
    @DisplayName("Alterar o nome da sub categoria por ID deve retornar http 200 quando dados válidos são enviados")
    void alterarSubCategoriaPorId_DadosValidos_DeveRetornarOk() throws Exception {
    	MostrarSubCategoriaDTO mostrarSubCategoriaDTO = new MostrarSubCategoriaDTO(1L, "Mouses");
        when(subCategoriaService.alterarSubCategoriaPorId(any(), any())).thenReturn(mostrarSubCategoriaDTO);

        Long idCategoria = 1L;
        
        var response = mvc.perform(put("/shop/admin/categoria/{idCategoria}", idCategoria)
        						.header("Authorization", "Bearer " + tokenAdmin)
				                .contentType(MediaType.APPLICATION_JSON)
				                .content(alterarSubCategoriaDTOJson.write(new AlterarSubCategoriaDTO("Mouses", 1L))
				                		.getJson())
				                )
				                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
    
    @Test
    @DisplayName("Alterar o nome da sub categoria por ID deve retornar http 403 quando usuario comum tenta acessar o endpoint")
    void alterarSubCategoriaPorId_TokenUser_DeveRetornarForbidden() throws Exception {
        Long idCategoria = 1L;
        
        var response = mvc.perform(put("/shop/admin/categoria/{idCategoria}", idCategoria)
        						.header("Authorization", "Bearer " + tokenUser))
				                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
    
    @Test
    @DisplayName("Alterar o nome da sub categoria por ID deve retornar http 403 quando token não é enviado")
    void alterarSubCategoriaPorId_TokenNaoEnviado_DeveRetornarForbidden() throws Exception {
        Long idCategoria = 1L;
        
        var response = mvc.perform(put("/shop/admin/categoria/{idCategoria}", idCategoria))
				                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

}
