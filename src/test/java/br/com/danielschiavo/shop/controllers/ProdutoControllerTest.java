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
import java.util.Set;

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
import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaComSubCategoriaDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.produto.AlterarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProdutoDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.services.ProdutoService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class ProdutoControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();
	
    @MockBean
    private ProdutoService produtoService;
    
	@Autowired
	private JacksonTester<Page<MostrarProdutosDTO>> pageMostrarProdutosDTOJson;
	
	@Autowired
	private JacksonTester<DetalharProdutoDTO> detalharProdutoDTOJson;
	
	@Autowired
	private JacksonTester<MostrarProdutosDTO> mostrarProdutosDTOJson;

	@Autowired
	private JacksonTester<CadastrarProdutoDTO> cadastrarProdutoDTOJson;
	
	@Autowired
	private JacksonTester<AlterarProdutoDTO> alterarProdutoDTOJson;
	
	@Test
	@DisplayName("Listar produtos deve retornar codigo http 200")
	void listarProdutos_DeveRetornarOk() throws IOException, Exception {
		MostrarSubCategoriaDTO mostrarSubCategoriaDTO = new MostrarSubCategoriaDTO(1L, "Mouses");
		MostrarCategoriaComSubCategoriaDTO mostrarCategoriaComSubCategoriaDTO = new MostrarCategoriaComSubCategoriaDTO(1L, "Computadores", mostrarSubCategoriaDTO);
		byte[] bytesImagem = "Hello world".getBytes();
		MostrarProdutosDTO mostrarProdutosDTO = new MostrarProdutosDTO(1L, "Produto1", BigDecimal.valueOf(200.00), 5, true, mostrarCategoriaComSubCategoriaDTO, bytesImagem);
		Page<MostrarProdutosDTO> pageProdutos = new PageImpl<>(List.of(mostrarProdutosDTO));
		when(produtoService.listarProdutos(any())).thenReturn(pageProdutos);
		
		var response = mvc.perform(get("/shop/publico/produto"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = pageMostrarProdutosDTOJson.write(pageProdutos).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}
	
	@Test
	@DisplayName("Listar produtos deve retornar codigo http 200")
	void listarProdutos_ParametroValido_DeveRetornarOk() throws IOException, Exception {
		MostrarSubCategoriaDTO mostrarSubCategoriaDTO = new MostrarSubCategoriaDTO(1L, "Mouses");
		MostrarCategoriaComSubCategoriaDTO mostrarCategoriaComSubCategoriaDTO = new MostrarCategoriaComSubCategoriaDTO(1L, "Computadores", mostrarSubCategoriaDTO);
		byte[] bytes = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("NomeArquivo.jpeg", bytes);
		ArquivoInfoDTO arquivoInfoDTO2 = new ArquivoInfoDTO("NomeVideo.avi", bytes);
		List<ArquivoInfoDTO> listaArquivoInfoDTO = new ArrayList<>();
		listaArquivoInfoDTO.add(arquivoInfoDTO);
		listaArquivoInfoDTO.add(arquivoInfoDTO2);
		
		DetalharProdutoDTO detalharProdutoDTO = new DetalharProdutoDTO(1L, "Nome produto", "descricao", BigDecimal.valueOf(200,00), 5, true, mostrarCategoriaComSubCategoriaDTO, listaArquivoInfoDTO);
		when(produtoService.detalharProdutoPorId(any())).thenReturn(detalharProdutoDTO);
		
		Long idProduto = 1L;
		
		var response = mvc.perform(get("/shop/publico/produto/{idProduto}", idProduto))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = detalharProdutoDTOJson.write(detalharProdutoDTO).getJson();

        JSONAssert.assertEquals(jsonEsperado, response.getContentAsString(), JSONCompareMode.LENIENT);
	}

	
//	------------------------------
//	------------------------------
//	ENDPOINTS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------
	
	@Test
	@DisplayName("Admin deletar produto por id deve retornar http 201 quando token valido e id produto é enviado")
	void deletarProdutoPorId_AdminValidoEIdProduto_DeveRetornarNoContent() throws IOException, Exception {
		doNothing().when(produtoService).deletarProdutoPorId(any());
		Long idProduto = 2L;
		var response = mvc.perform(delete("/shop/admin/produto/{idProduto}", idProduto)
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Admin deletar produto por id deve retornar http 403 quando token de usuario comum é enviado")
	void deletarProdutoPorId_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		Long idProduto = 2L;
		var response = mvc.perform(delete("/shop/admin/produto/{idProduto}", idProduto)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Admin deletar produto por id deve retornar http 403 quando nenhum token é enviado")
	void deletarProdutoPorId_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idProduto = 2L;
		var response = mvc.perform(delete("/shop/admin/produto/{idProduto}", idProduto))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Cadastrar produto deve retornar http 201 quando informacoes validas são enviadas")
	void cadastrarProduto_DtoETokenAdminValido_DeveRetornarCreated() throws IOException, Exception {
		MostrarSubCategoriaDTO mostrarSubCategoriaDTO = new MostrarSubCategoriaDTO(1L, "Mouses");
		MostrarCategoriaComSubCategoriaDTO mostrarCategoriaComSubCategoriaDTO = new MostrarCategoriaComSubCategoriaDTO(1L, "Computadores", mostrarSubCategoriaDTO);
		byte[] bytesImagem = "Hello world".getBytes();
		MostrarProdutosDTO mostrarProdutosDTO = new MostrarProdutosDTO(1L, "Produto1", BigDecimal.valueOf(200.00), 5, true, mostrarCategoriaComSubCategoriaDTO, bytesImagem);
		when(produtoService.cadastrarProduto(any())).thenReturn(mostrarProdutosDTO);
		
		Set<TipoEntrega> tipoEntrega = Set.of(TipoEntrega.CORREIOS,TipoEntrega.RETIRADA_NA_LOJA);
		ArquivoProdutoDTO arquivoProdutoDTO = new ArquivoProdutoDTO("NomeArquivo.jpeg", 0);
		ArquivoProdutoDTO arquivoProdutoDTO2 = new ArquivoProdutoDTO("NomeVideo.avi", 1);
		List<ArquivoProdutoDTO> listaArquivoProdutoDTO = new ArrayList<>();
		listaArquivoProdutoDTO.add(arquivoProdutoDTO);
		listaArquivoProdutoDTO.add(arquivoProdutoDTO2);
		CadastrarProdutoDTO cadastrarProdutoDTO = new CadastrarProdutoDTO("Produto1", "Descricao", BigDecimal.valueOf(200.00), 5, true, 1L, 1L, tipoEntrega, listaArquivoProdutoDTO);
		
		var response = mvc.perform(post("/shop/admin/produto")
								  .header("Authorization", "Bearer " + tokenAdmin)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(cadastrarProdutoDTOJson.write(cadastrarProdutoDTO)
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = mostrarProdutosDTOJson.write(mostrarProdutosDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Cadastrar produto deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void cadastrarProduto_DtoValidoETokenUser_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/produto")
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
			
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Cadastrar produto deve retornar http 403 quando token não é enviado")
	void cadastrarProduto_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/produto"))
								  .andReturn().getResponse();
			
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Alterar produto por id deve retornar http 200 quando informacoes estão válidas")
	void alterarProdutoPorId_DtoETokenAdminValido_DeveRetornarOk() throws IOException, Exception {
		MostrarSubCategoriaDTO mostrarSubCategoriaDTO = new MostrarSubCategoriaDTO(1L, "Mouses");
		MostrarCategoriaComSubCategoriaDTO mostrarCategoriaComSubCategoriaDTO = new MostrarCategoriaComSubCategoriaDTO(1L, "Computadores", mostrarSubCategoriaDTO);
		byte[] bytes = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("NomeArquivo.jpeg", bytes);
		ArquivoInfoDTO arquivoInfoDTO2 = new ArquivoInfoDTO("NomeVideo.avi", bytes);
		List<ArquivoInfoDTO> listaArquivoInfoDTO = new ArrayList<>();
		listaArquivoInfoDTO.add(arquivoInfoDTO);
		listaArquivoInfoDTO.add(arquivoInfoDTO2);
		DetalharProdutoDTO detalharProdutoDTO = new DetalharProdutoDTO(1L, "Nome produto", "descricao", BigDecimal.valueOf(200,00), 5, true, mostrarCategoriaComSubCategoriaDTO, listaArquivoInfoDTO);
		when(produtoService.alterarProdutoPorId(any(), any())).thenReturn(detalharProdutoDTO);
		
		Set<TipoEntrega> tipoEntrega = Set.of(TipoEntrega.CORREIOS,TipoEntrega.RETIRADA_NA_LOJA);
		ArquivoProdutoDTO arquivoProdutoDTO = new ArquivoProdutoDTO("NomeArquivo.jpeg", 0);
		ArquivoProdutoDTO arquivoProdutoDTO2 = new ArquivoProdutoDTO("NomeVideo.avi", 1);
		List<ArquivoProdutoDTO> listaArquivoProdutoDTO = new ArrayList<>();
		listaArquivoProdutoDTO.add(arquivoProdutoDTO2);
		listaArquivoProdutoDTO.add(arquivoProdutoDTO);
		
		Long idProduto = 1L;
		
		var response = mvc.perform(put("/shop/admin/produto/{idProduto}", idProduto)
								  .header("Authorization", "Bearer " + tokenAdmin)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(alterarProdutoDTOJson.write(
										  new AlterarProdutoDTO("Nome produto", "descricao", BigDecimal.valueOf(200.00), 5, true, 1L, 1L, tipoEntrega, listaArquivoProdutoDTO))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = detalharProdutoDTOJson.write(detalharProdutoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Alterar produto por id deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void alterarProdutoPorId_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		Long idProduto = 1L;
		
		var response = mvc.perform(put("/shop/admin/produto/{idProduto}", idProduto)
								  .header("Authorization", "Bearer " + tokenUser))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Alterar produto por id deve retornar http 401 quando tenta acessar o endpoint sem token")
	void alterarProdutoPorId_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		Long idProduto = 1L;
		
		var response = mvc.perform(put("/shop/admin/produto/{idProduto}", idProduto))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
