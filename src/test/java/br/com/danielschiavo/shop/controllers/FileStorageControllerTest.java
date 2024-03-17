package br.com.danielschiavo.shop.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.com.danielschiavo.shop.JwtUtilTest;
import br.com.danielschiavo.shop.models.cliente.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.filestorage.MostrarArquivoProdutoDTO;
import br.com.danielschiavo.shop.models.filestorage.PostImagemPedidoDTO;
import br.com.danielschiavo.shop.services.FileStorageService;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("dev")
class FileStorageControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	private String tokenUser = JwtUtilTest.generateTokenUser();
	
	private String tokenAdmin = JwtUtilTest.generateTokenAdmin();

	@MockBean
	private FileStorageService fileStorageService;
	
	@Autowired
	private JacksonTester<List<MostrarArquivoProdutoDTO>> listaMostrarArquivoProdutoDTOJson;
	
	@Autowired
	private JacksonTester<List<ArquivoInfoDTO>> listaArquivoInfoDTOJson;
	
	@Autowired
	private JacksonTester<MostrarArquivoProdutoDTO> mostrarArquivoProdutoDTOJson;
	
	@Autowired
	private JacksonTester<ArquivoInfoDTO> arquivoInfoDTOJson;
	
	@Autowired
	private JacksonTester<PostImagemPedidoDTO> postImagemPedidoDTOJson;

	@Test
	@DisplayName("Deletar arquivo produto deve retornar http 204 quando token e parametro válidos são enviados")
	void deletarArquivoProduto_TokenAdminEParametroValido_DeveRetornarOkNoContent() throws IOException, Exception {
		doNothing().when(fileStorageService).deletarArquivoProdutoNoDisco(any());
		
		String nomeArquivo = "Nomequalquer.jpeg";
		
		var response = mvc.perform(delete("/shop/admin/filestorage/arquivo-produto/{nomeArquivo}", nomeArquivo)
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar arquivo produto deve retornar http 403 quando token de usuario comum é enviado")
	void deletarArquivoProduto_TokenUserEnviado_DeveRetornarForbidden() throws IOException, Exception {
		String nomeArquivo = "Nomequalquer.jpeg";
		
		var response = mvc.perform(delete("/shop/admin/filestorage/arquivo-produto/{nomeArquivo}", nomeArquivo)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Deletar arquivo produto deve retornar http 401 quando token não é enviado")
	void deletarArquivoProduto_NenhumTokenEnviado_DeveRetornarUnauthorizred() throws IOException, Exception {
		String nomeArquivo = "Nomequalquer.jpeg";
		
		var response = mvc.perform(delete("/shop/admin/filestorage/arquivo-produto/{nomeArquivo}", nomeArquivo))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Mostrar arquivo produto por lista de nomes deve retornar codigo http 200 quando token e dto é valido")
	void mostrarArquivoProdutoPorListaDeNomes_TokenEDtoValido_DeveRetornarOk() throws IOException, Exception {
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Imagemum.jpeg", bytesImagem);
		ArquivoInfoDTO arquivoInfoDTO2 = new ArquivoInfoDTO("Imagemdois.jpeg", bytesImagem);
		List<ArquivoInfoDTO> listaArquivoInfoDTO = new ArrayList<>();
		listaArquivoInfoDTO.add(arquivoInfoDTO);
		listaArquivoInfoDTO.add(arquivoInfoDTO2);
		
		when(fileStorageService.mostrarArquivoProdutoPorListaDeNomes(any())).thenReturn(listaArquivoInfoDTO);
		
		MostrarArquivoProdutoDTO mostrarArquivoProdutoDTO = new MostrarArquivoProdutoDTO("Imagemum.jpeg");
		MostrarArquivoProdutoDTO mostrarArquivoProdutoDTO2 = new MostrarArquivoProdutoDTO("Imagemdois.jpeg");
		List<MostrarArquivoProdutoDTO> listaMostrarArquivoProdutoDTO = new ArrayList<>();
		listaMostrarArquivoProdutoDTO.add(mostrarArquivoProdutoDTO);
		listaMostrarArquivoProdutoDTO.add(mostrarArquivoProdutoDTO2);
		
		var response = mvc.perform(get("/shop/admin/filestorage/arquivo-produto")
				  				  .header("Authorization", "Bearer " + tokenAdmin)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(listaMostrarArquivoProdutoDTOJson.write(listaMostrarArquivoProdutoDTO)
								  .getJson()))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = listaArquivoInfoDTOJson.write(listaArquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Mostrar arquivo produto por lista de nomes deve retornar codigo http 403 quando usuario comum tenta acessar o endpoint")
	void mostrarArquivoProdutoPorListaDeNomes_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(get("/shop/admin/filestorage/arquivo-produto")
				  				  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Mostrar arquivo produto por lista de nomes deve retornar codigo http 401 quando tenta usar endpoint sem enviar token")
	void mostrarArquivoProdutoPorListaDeNomes_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(get("/shop/admin/filestorage/arquivo-produto"))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Mostrar arquivo produto por nome deve retornar codigo http 200 quando token e dto é valido")
	void mostrarArquivoProdutoPorNome_TokenEDtoValido_DeveRetornarOk() throws IOException, Exception {
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Imagemum.jpeg", bytesImagem);
		
		when(fileStorageService.pegarArquivoProdutoPorNome(any())).thenReturn(arquivoInfoDTO);
		
		String nomeArquivo = "Imagemum.jpeg";
		var response = mvc.perform(get("/shop/admin/filestorage/arquivo-produto/{nomeArquivo}", nomeArquivo)
				  				  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = arquivoInfoDTOJson.write(arquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	
	@Test
	@DisplayName("Mostrar arquivo produto por lista de nomes deve retornar codigo http 403 quando usuario comum tenta acessar o endpoint")
	void mostrarArquivoProdutoPorNome_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		String nomeArquivo = "Imagemum.jpeg";
		var response = mvc.perform(get("/shop/admin/filestorage/arquivo-produto/{nomeArquivo}", nomeArquivo)
				  				  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Mostrar arquivo produto por lista de nomes deve retornar codigo http 401 quando tenta usar endpoint sem enviar token")
	void mostrarArquivoProdutoPorNome_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		String nomeArquivo = "Imagemum.jpeg";
		var response = mvc.perform(get("/shop/admin/filestorage/arquivo-produto/{nomeArquivo}", nomeArquivo))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Cadastrar array arquivo produto deve retornar http 201 quando token e multipart são enviados")
	void cadastrarArrayArquivoProduto_TokenAdminEArrayMultipart_DeveRetornarCreated() throws IOException, Exception {
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Imagemum.jpeg", bytesImagem);
		ArquivoInfoDTO arquivoInfoDTO2 = new ArquivoInfoDTO("Imagemdois.jpeg", bytesImagem);
		List<ArquivoInfoDTO> listaArquivoInfoDTO = new ArrayList<>();
		listaArquivoInfoDTO.add(arquivoInfoDTO);
		listaArquivoInfoDTO.add(arquivoInfoDTO2);
		when(fileStorageService.persistirArrayArquivoProduto(any(), any())).thenReturn(listaArquivoInfoDTO);
		
        MockMultipartFile file1 = new MockMultipartFile("arquivos", "Imagemum.jpeg",
                MediaType.TEXT_PLAIN_VALUE, bytesImagem);
        MockMultipartFile file2 = new MockMultipartFile("arquivos", "Imagemdois.jpeg",
                MediaType.TEXT_PLAIN_VALUE, bytesImagem);
		
		var response = mvc.perform(multipart("/shop/admin/filestorage/arquivo-produto/array")
								  .file(file1)
								  .file(file2)
								  .header("Authorization", "Bearer " + tokenAdmin)
								  .contentType(MediaType.MULTIPART_FORM_DATA))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = listaArquivoInfoDTOJson.write(listaArquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Cadastrar array arquivo produto deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void cadastrarArrayArquivoProduto_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/arquivo-produto/array")
								  .header("Authorization", "Bearer " + tokenUser))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Cadastrar array arquivo produto deve retornar http 401 quando token não é enviado")
	void cadastrarArrayArquivoProduto_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/arquivo-produto/array"))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Cadastrar um arquivo produto deve retornar http 201 quando token e multipart são enviados")
	void cadastrarUmArquivoProduto_TokenAdminEArrayMultipart_DeveRetornarCreated() throws IOException, Exception {
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Imagemum.jpeg", bytesImagem);
		when(fileStorageService.persistirUmArquivoProduto(any())).thenReturn(arquivoInfoDTO);
		
        MockMultipartFile file1 = new MockMultipartFile("arquivo", "Imagemum.jpeg",
                MediaType.TEXT_PLAIN_VALUE, bytesImagem);
		
		var response = mvc.perform(multipart("/shop/admin/filestorage/arquivo-produto")
								  .file(file1)
								  .header("Authorization", "Bearer " + tokenAdmin)
								  .contentType(MediaType.MULTIPART_FORM_DATA))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = arquivoInfoDTOJson.write(arquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Cadastrar um arquivo produto deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void cadastrarUmArquivoProduto_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/arquivo-produto")
								  .header("Authorization", "Bearer " + tokenUser))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Cadastrar um arquivo produto deve retornar http 401 quando token não é enviado")
	void cadastrarUmArquivoProduto_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/arquivo-produto"))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Alterar arquivo produto deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void alterarArquivoProduto_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(put("/shop/admin/filestorage/arquivo-produto")
				.header("Authorization", "Bearer " + tokenUser))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Alterar um arquivo produto deve retornar http 401 quando token não é enviado")
	void alterarArquivoProduto_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(put("/shop/admin/filestorage/arquivo-produto"))
								.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	

//PERFIL
	@Test
	@DisplayName("Deletar foto perfil deve retornar http 204 quando token e parametro válidos são enviados")
	void deletarFotoPerfil_TokenAdminEParametroValido_DeveRetornarOkNoContent() throws IOException, Exception {
		doNothing().when(fileStorageService).deletarArquivoProdutoNoDisco(any());
		
		String nomeFotoPerfilAntiga = "Nomequalquer.jpeg";
		
		var response = mvc.perform(delete("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfilAntiga}", nomeFotoPerfilAntiga)
								  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
	
	@Test
	@DisplayName("Deletar foto perfil deve retornar http 403 quando token de usuario comum é enviado")
	void deletarFotoPerfil_TokenUserEnviado_DeveRetornarForbidden() throws IOException, Exception {
		String nomeFotoPerfilAntiga = "Nomequalquer.jpeg";
		
		var response = mvc.perform(delete("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfilAntiga}", nomeFotoPerfilAntiga)
								  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Deletar foto perfil deve retornar http 401 quando token não é enviado")
	void deletarFotoPerfil_NenhumTokenEnviado_DeveRetornarUnauthorizred() throws IOException, Exception {
		String nomeFotoPerfilAntiga = "Nomequalquer.jpeg";
		
		var response = mvc.perform(delete("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfilAntiga}", nomeFotoPerfilAntiga))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Pegar foto perfil por nome deve retornar codigo http 200 quando token e dto é valido")
	void pegarFotoPerfilPorNome_TokenEDtoValido_DeveRetornarOk() throws IOException, Exception {
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Imagemum.jpeg", bytesImagem);
		
		when(fileStorageService.pegarFotoPerfilPorNome(any())).thenReturn(arquivoInfoDTO);
		
		String nomeFotoPerfil = "Nomequalquer.jpeg";
		
		var response = mvc.perform(get("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfil}", nomeFotoPerfil)
				  				  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = arquivoInfoDTOJson.write(arquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Pegar foto perfil por nome deve retornar codigo http 403 quando usuario comum tenta acessar o endpoint")
	void pegarFotoPerfilPorNome_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		String nomeFotoPerfil = "Nomequalquer.jpeg";
		var response = mvc.perform(get("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfil}", nomeFotoPerfil)
				  				  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Pegar foto perfil por nome deve retornar codigo http 401 quando tenta usar endpoint sem enviar token")
	void pegarFotoPerfilPorNome_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		String nomeFotoPerfil = "Nomequalquer.jpeg";
		var response = mvc.perform(get("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfil}", nomeFotoPerfil))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Cadastrar foto perfil deve retornar http 201 quando token e multipart são enviados")
	void cadastrarFotoPerfil_TokenAdminEArrayMultipart_DeveRetornarCreated() throws IOException, Exception {
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Imagemum.jpeg", bytesImagem);
		when(fileStorageService.persistirFotoPerfil(any())).thenReturn(arquivoInfoDTO);
		
        MockMultipartFile file1 = new MockMultipartFile("foto", "Imagemum.jpeg",
                MediaType.TEXT_PLAIN_VALUE, bytesImagem);
		
		var response = mvc.perform(multipart("/shop/admin/filestorage/foto-perfil")
								  .file(file1)
								  .contentType(MediaType.MULTIPART_FORM_DATA)
								  .header("Authorization", "Bearer " + tokenAdmin))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
        var jsonEsperado = arquivoInfoDTOJson.write(arquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Cadastrar foto perfil deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void cadastrarFotoPerfil_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/foto-perfil")
								  .header("Authorization", "Bearer " + tokenUser))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Cadastrar foto perfil deve retornar http 401 quando token não é enviado")
	void cadastrarFotoPerfil_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/foto-perfil"))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Alterar foto perfil deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void alterarFotoPerfil_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		String nomeFotoPerfilAntiga = "Nomequalquer.jpeg";
		var response = mvc.perform(put("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfilAntiga}", nomeFotoPerfilAntiga)
				.header("Authorization", "Bearer " + tokenUser))
				.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Alterar foto perfil deve retornar http 401 quando token não é enviado")
	void alterarFotoPerfil_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		String nomeFotoPerfilAntiga = "Nomequalquer.jpeg";
		var response = mvc.perform(put("/shop/admin/filestorage/foto-perfil/{nomeFotoPerfilAntiga}", nomeFotoPerfilAntiga))
								.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	

//PEDIDO
	
	@Test
	@DisplayName("Pegar imagem pedido por nome deve retornar codigo http 200 quando token e dto é valido")
	void pegarImagemPedidoPorNome_TokenEDtoValido_DeveRetornarOk() throws IOException, Exception {
		byte[] bytesImagem = "Hello world".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Imagemum.jpeg", bytesImagem);
		
		when(fileStorageService.pegarImagemPedidoPorNome(any())).thenReturn(arquivoInfoDTO);
		
		String nomeFotoPerfil = "Nomequalquer.jpeg";
		
		var response = mvc.perform(get("/shop/admin/filestorage/pedido/{nomeFotoPerfil}", nomeFotoPerfil)
				  				  .header("Authorization", "Bearer " + tokenAdmin))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
        var jsonEsperado = arquivoInfoDTOJson.write(arquivoInfoDTO).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
	}
	
	@Test
	@DisplayName("Pegar imagem pedido por nome deve retornar codigo http 403 quando usuario comum tenta acessar o endpoint")
	void pegarImagemPedidoPorNome_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		String nomeFotoPerfil = "Nomequalquer.jpeg";
		var response = mvc.perform(get("/shop/admin/filestorage/pedido/{nomeFotoPerfil}", nomeFotoPerfil)
				  				  .header("Authorization", "Bearer " + tokenUser))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Pegar imagem pedido por nome deve retornar codigo http 401 quando tenta usar endpoint sem enviar token")
	void pegarImagemPedidoPorNome_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		String nomeFotoPerfil = "Nomequalquer.jpeg";
		var response = mvc.perform(get("/shop/admin/filestorage/pedido/{nomeFotoPerfil}", nomeFotoPerfil))
								  .andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	@DisplayName("Persistir ou recuperar imagem pedido deve retornar http 201 quando token e multipart são enviados")
	void persistirOuRecuperarImagemPedido_TokenAdminEArrayMultipart_DeveRetornarCreated() throws IOException, Exception {
		String nomeQualquer = "Nomequalquer.jpeg";
		when(fileStorageService.persistirOuRecuperarImagemPedido(any(), any())).thenReturn(nomeQualquer);
		
		var response = mvc.perform(post("/shop/admin/filestorage/pedido")
								  .header("Authorization", "Bearer " + tokenAdmin)
								  .contentType(MediaType.APPLICATION_JSON)
								  .content(postImagemPedidoDTOJson.write(
										  new PostImagemPedidoDTO("NomeQualquer.jpeg" , 1L))
										  .getJson())
								  )
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}
	
	@Test
	@DisplayName("Persistir ou recuperar imagem pedido deve retornar http 403 quando usuario comum tenta acessar o endpoint")
	void persistirOuRecuperarImagemPedido_TokenUser_DeveRetornarForbidden() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/pedido")
								  .header("Authorization", "Bearer " + tokenUser))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
	}
	
	@Test
	@DisplayName("Persistir ou recuperar imagem pedido deve retornar http 401 quando token não é enviado")
	void persistirOuRecuperarImagemPedido_TokenNaoEnviado_DeveRetornarUnauthorized() throws IOException, Exception {
		var response = mvc.perform(post("/shop/admin/filestorage/pedido"))
						.andReturn().getResponse();
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
}
