package br.com.danielschiavo.shop.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.infra.exceptions.FileStorageException;
import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@Spy
	@InjectMocks
	private FileStorageService fileService;
	
	@Mock
	private FileUrlResource fileUrlResource;
	
    @Mock
    private MultipartFile arquivo1;
    
    @Mock
    private MultipartFile arquivo2;
	
	
//	------------------------------
//	------------------------------
//			PRODUTO
//	------------------------------
//	------------------------------
	
    @Test
    @DisplayName("Deletar arquivo produto no disco deve executar normalmente quando arquivo existe")
    void deletarArquivoProdutoNoDisco_ArquivoExiste_NaoDeveLancarExcecao() throws IOException {
    	//ARRANGE
        Path pathEsperado = Paths.get(System.getProperty("user.home"), ".shop", "imagens", "produto", "teste.txt");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
        	
        	//ACT
        	fileService.deletarArquivoProdutoNoDisco("teste.txt");

        	//ASSERT
            mockedFiles.verify(() -> Files.delete(pathEsperado), Mockito.times(1));
        }
    }
    
    @Test
    @DisplayName("Deletar arquivo produto no disco deve lançar exceção quando arquivo não existe")
    void deletarArquivoProdutoNoDisco_ArquivoNaoExiste_DeveLancarExcecao() throws IOException {
    	//ARRANGE
        Path pathEsperado = Paths.get(System.getProperty("user.home"), ".shop", "imagens", "produto", "teste.txt");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
        	mockedFiles.when(() -> Files.delete(pathEsperado)).thenThrow(IOException.class);

        	//ASSERT + ACT
        	Assertions.assertThrows(FileStorageException.class, () -> fileService.deletarArquivoProdutoNoDisco("teste.txt"));
        }
    }
    
    @Test
    @DisplayName("Mostrar arquivo produto por lista de nomes deve devolver um ArquivoInfoDTO por cada nome contendo os bytes do arquivo")
    void mostrarArquivoProdutoPorListaDeNomes_ConseguiuRecuperarBytesArquivosComSucesso_DeveCriarArquivoInfoDtoNormalmente() throws IOException {
    	//ARRANGE
        List<String> nomesArquivos = Arrays.asList("arquivo1.txt", "arquivo2.txt");
        byte[] bytes = {1, 2, 3};
        Mockito.doReturn(bytes).when(fileService).recuperarBytesArquivoProdutoDoDisco(any());
        
        //ACT
        List<ArquivoInfoDTO> resultado = fileService.mostrarArquivoProdutoPorListaDeNomes(nomesArquivos);

        //ASSERT
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertEquals(nomesArquivos.get(0), resultado.get(0).nomeArquivo());
        Assertions.assertEquals(nomesArquivos.get(1), resultado.get(1).nomeArquivo());
        Assertions.assertArrayEquals(bytes, resultado.get(0).bytesArquivo());
        Assertions.assertArrayEquals(bytes, resultado.get(1).bytesArquivo());
    }
    
    @Test
    @DisplayName("Mostrar arquivo produto por lista de nomes quando tiver problema ao pegar algum arquivo deve criar um arquivo info dto com mensagem de erro e bytes arquivo null")
    void mostrarArquivoProdutoPorListaDeNomes_ProblemaAoPegarUmArquivo_DeveCriarUmArquivoInfoDtoComMensagemDeErro() throws IOException {
    	//ARRANGE
        List<String> nomesArquivos = Arrays.asList("arquivo1.jpeg", "arquivo2.jpeg", "arquivo3.jpeg");
        byte[] bytes = {1, 2, 3};
        Mockito.doReturn(bytes).when(fileService).recuperarBytesArquivoProdutoDoDisco("arquivo1.jpeg");
        Mockito.doThrow(new FileStorageException("Falha ao acessar arquivo")).when(fileService).recuperarBytesArquivoProdutoDoDisco("arquivo2.jpeg");
        Mockito.doReturn(bytes).when(fileService).recuperarBytesArquivoProdutoDoDisco("arquivo3.jpeg");
        
        //ACT
        List<ArquivoInfoDTO> resultado = fileService.mostrarArquivoProdutoPorListaDeNomes(nomesArquivos);

        //ASSERT
        Assertions.assertEquals(3, resultado.size());
        Assertions.assertEquals(nomesArquivos.get(0), resultado.get(0).nomeArquivo());
        Assertions.assertEquals(nomesArquivos.get(1), resultado.get(1).nomeArquivo());
        Assertions.assertEquals(nomesArquivos.get(2), resultado.get(2).nomeArquivo());
        Assertions.assertEquals(null, resultado.get(0).erro());
        Assertions.assertEquals("Falha ao acessar arquivo", resultado.get(1).erro());
        Assertions.assertEquals(null, resultado.get(2).erro());
        Assertions.assertArrayEquals(bytes, resultado.get(0).bytesArquivo());
        Assertions.assertArrayEquals(null, resultado.get(1).bytesArquivo());
        Assertions.assertArrayEquals(bytes, resultado.get(2).bytesArquivo());
    }
    
    @Test
    @DisplayName("Pegar arquivo produto por nome deve executar normalmente quando enviado um nome válido")
    void pegarArquivoProdutoPorNome_ArquivoExiste_NaoDeveLancarExcecao() {
    	//ARRANGE
    	String nomeArquivo = "arquivo1.jpeg";
    	byte[] bytes = {1, 2, 3};
    	Mockito.doReturn(bytes).when(fileService).recuperarBytesArquivoProdutoDoDisco("arquivo1.jpeg");
    	
    	//ACT
    	ArquivoInfoDTO arquivoInfoDTO = fileService.pegarArquivoProdutoPorNome(nomeArquivo);
    	
    	//ASSERT
    	Assertions.assertEquals(nomeArquivo, arquivoInfoDTO.nomeArquivo());
    	Assertions.assertEquals(bytes, arquivoInfoDTO.bytesArquivo());
    }
    
    @Test
    @DisplayName("Pegar arquivo produto por nome deve lançar exceção quando nome do arquivo não existir no diretorio de arquivos")
    void pegarArquivoProdutoPorNome_ArquivoNaoExiste_DeveLancarExcecao() {
    	//ARRANGE
    	String nomeArquivo = "arquivo1.jpeg";
        Mockito.doThrow(new FileStorageException("Falha ao acessar arquivo")).when(fileService).recuperarBytesArquivoProdutoDoDisco("arquivo1.jpeg");
    	
    	//ASSERT + ACT
    	Assertions.assertThrows(FileStorageException.class, () -> fileService.pegarArquivoProdutoPorNome(nomeArquivo));
    }
    
    @Test
    @DisplayName("Persistir array arquivo produto deve funcionar normalmente quando extensão do arquivo for válida (PNG, JPEG, JPG, AVI, MP4)")
    void persistirArrayArquivoProduto_ExtensaoDoArquivoValida_NaoDeveLancarExcecao() throws IOException {
    	//ARRAGE
    	byte[] bytes1 = "conteúdo do arquivo 1".getBytes();
    	byte[] bytes2 = "conteúdo do arquivo 2".getBytes();
    	ByteArrayInputStream inputStream1 = new ByteArrayInputStream(bytes1);
    	ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bytes2);
    	when(arquivo1.getInputStream()).thenReturn(inputStream1);
    	when(arquivo2.getInputStream()).thenReturn(inputStream2);
    	when(arquivo1.getContentType()).thenReturn("image/jpeg");
    	when(arquivo2.getContentType()).thenReturn("image/jpeg");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
            .thenAnswer(invocation -> null);
        	
        	//ACT
        	MultipartFile[] arquivos = {arquivo1, arquivo2};
        	UriComponentsBuilder uriBuilderBase = UriComponentsBuilder.fromUriString("http://localhost:8080");
        	List<ArquivoInfoDTO> resultado = fileService.persistirArrayArquivoProduto(arquivos, uriBuilderBase);
        	
        	//ASSERT
        	Assertions.assertNotNull(resultado);
        	Assertions.assertEquals(2, resultado.size());
        	ArquivoInfoDTO arquivoInfo1 = resultado.get(0);
        	Assertions.assertEquals(true, arquivoInfo1.nomeArquivo().endsWith(".jpeg"));
        	Assertions.assertTrue(arquivoInfo1.uri().contains(".jpeg"));
        	Assertions.assertArrayEquals(bytes1, arquivoInfo1.bytesArquivo());
        	ArquivoInfoDTO arquivoInfo2 = resultado.get(1);
        	Assertions.assertEquals(true, arquivoInfo2.nomeArquivo().endsWith(".jpeg"));
        	Assertions.assertTrue(arquivoInfo2.uri().contains(".jpeg"));
        	Assertions.assertArrayEquals(bytes2, arquivoInfo2.bytesArquivo());
        }
    }
    
    @Test
    @DisplayName("Persistir array arquivo produto deve lançar exceção quando extensão do arquivo for diferente de PNG, JPEG, JPG, AVI, MP4")
    void persistirArrayArquivoProduto_ExtensaoDoArquivoInvalida_DeveLancarExcecao() throws IOException {
    	//ARRAGE
        when(arquivo1.getContentType()).thenReturn("application/pdf");
        when(arquivo2.getContentType()).thenReturn("application/pdf");
        	
        //ACT
        MultipartFile[] arquivos = {arquivo1, arquivo2};
        UriComponentsBuilder uriBuilderBase = UriComponentsBuilder.fromUriString("http://localhost:8080");
        List<ArquivoInfoDTO> listaArquivoInfoDTO = fileService.persistirArrayArquivoProduto(arquivos, uriBuilderBase);
        	
        //ASSERT
        Assertions.assertNotNull(listaArquivoInfoDTO.get(0).erro());
        Assertions.assertNotNull(listaArquivoInfoDTO.get(1).erro());
    }
    
    @Test
    @DisplayName("Alterar arquivo produto deve executar normalmente quando arquivo e nome valido são enviados")
    void alterarArquivoProduto_ArquivoENomeValidoEnviado_DeveExecutarNormalmente() throws IOException {
    	//ARRANGE
    	byte[] bytes1 = "conteúdo do arquivo 1".getBytes();
    	byte[] bytes2 = "conteúdo do arquivo 2".getBytes();
    	ByteArrayInputStream inputStream1 = new ByteArrayInputStream(bytes1);
    	ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bytes2);
    	when(arquivo1.getInputStream()).thenReturn(inputStream1);
    	when(arquivo2.getInputStream()).thenReturn(inputStream2);
    	when(arquivo1.getContentType()).thenReturn("image/jpeg");
    	when(arquivo2.getContentType()).thenReturn("image/jpeg");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
            .thenAnswer(invocation -> null);
            mockedFiles.when(() -> Files.delete(any(Path.class))).thenAnswer(invocation -> null);
            
        	//ACT
        	MultipartFile[] arquivos = {arquivo1, arquivo2};
        	String nomesArquivosASeremExcluidos = "arquivoexcluido1.jpg, arquivoexcluido2.jpg";
        	UriComponentsBuilder uriBuilderBase = UriComponentsBuilder.fromUriString("http://localhost:8080");
        	List<ArquivoInfoDTO> listaArquivoInfoDTO = fileService.alterarArrayArquivoProduto(arquivos, nomesArquivosASeremExcluidos, uriBuilderBase);
        	
        	//ASSERT
        	Assertions.assertEquals(2, listaArquivoInfoDTO.size());
        	Assertions.assertEquals(true, listaArquivoInfoDTO.get(0).nomeArquivo().endsWith(".jpeg"));
        	Assertions.assertEquals(true, listaArquivoInfoDTO.get(1).nomeArquivo().endsWith(".jpeg"));
        	Assertions.assertArrayEquals(bytes1, listaArquivoInfoDTO.get(0).bytesArquivo());
        	Assertions.assertArrayEquals(bytes2, listaArquivoInfoDTO.get(1).bytesArquivo());
        }
    }
    
    @Test
    @DisplayName("Alterar arquivo produto deve retornar ArquivoInfoDTO com erro quando nome invalido é enviado")
    void alterarArquivoProduto_NomeInvalidoEnviado_DeveRetornarArquivoInfoDtoComErro() throws IOException {
    	//ARRANGE
    	byte[] bytes1 = "conteúdo do arquivo 1".getBytes();
    	byte[] bytes2 = "conteúdo do arquivo 2".getBytes();
    	ByteArrayInputStream inputStream1 = new ByteArrayInputStream(bytes1);
    	ByteArrayInputStream inputStream2 = new ByteArrayInputStream(bytes2);
    	when(arquivo1.getInputStream()).thenReturn(inputStream1);
    	when(arquivo2.getInputStream()).thenReturn(inputStream2);
    	when(arquivo1.getContentType()).thenReturn("image/jpeg");
    	when(arquivo2.getContentType()).thenReturn("image/jpeg");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
            .thenAnswer(invocation -> null);
            mockedFiles.when(() -> Files.delete(any(Path.class))).thenThrow(IOException.class);
            
        	//ACT
        	MultipartFile[] arquivos = {arquivo1, arquivo2};
        	String nomesArquivosASeremExcluidos = "arquivoexcluido1.jpg, arquivoexcluido2.jpg";
        	UriComponentsBuilder uriBuilderBase = UriComponentsBuilder.fromUriString("http://localhost:8080");
        	List<ArquivoInfoDTO> listaArquivoInfoDTO = fileService.alterarArrayArquivoProduto(arquivos, nomesArquivosASeremExcluidos, uriBuilderBase);
        	
        	//ASSERT
        	Assertions.assertEquals(4, listaArquivoInfoDTO.size());
        	Assertions.assertNotNull(listaArquivoInfoDTO.get(0).erro());
        	Assertions.assertNotNull(listaArquivoInfoDTO.get(1).erro());
        	Assertions.assertEquals(true, listaArquivoInfoDTO.get(2).nomeArquivo().endsWith(".jpeg"));
        	Assertions.assertEquals(true, listaArquivoInfoDTO.get(3).nomeArquivo().endsWith(".jpeg"));
        	Assertions.assertArrayEquals(bytes1, listaArquivoInfoDTO.get(2).bytesArquivo());
        	Assertions.assertArrayEquals(bytes2, listaArquivoInfoDTO.get(3).bytesArquivo());
        }
    }
    
    @Test
    @DisplayName("Alterar arquivo produto deve retornar ArquivoInfoDTO com erro quando arquivo invalido é enviado")
    void alterarArquivoProduto_ArquivoInvalidoEnviado_DeveRetornarArquivoInfoDtoComErro() throws IOException {
    	//ARRANGE
    	when(arquivo1.getContentType()).thenReturn("application/pdf");
    	when(arquivo2.getContentType()).thenReturn("application/pdf");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
            .thenAnswer(invocation -> null);
            mockedFiles.when(() -> Files.delete(any(Path.class))).thenAnswer(invocation -> null);
            
        	//ACT
        	MultipartFile[] arquivos = {arquivo1, arquivo2};
        	String nomesArquivosASeremExcluidos = "arquivoexcluido1.jpg, arquivoexcluido2.jpg";
        	UriComponentsBuilder uriBuilderBase = UriComponentsBuilder.fromUriString("http://localhost:8080");
        	List<ArquivoInfoDTO> listaArquivoInfoDTO = fileService.alterarArrayArquivoProduto(arquivos, nomesArquivosASeremExcluidos, uriBuilderBase);
        	
        	//ASSERT
        	Assertions.assertEquals(2, listaArquivoInfoDTO.size());
        	Assertions.assertNotNull(listaArquivoInfoDTO.get(0).erro());
        	Assertions.assertNotNull(listaArquivoInfoDTO.get(1).erro());
        }
    }
    
    
//	------------------------------
//	------------------------------
//			PERFIL
//	------------------------------
//	------------------------------
    
    @Test
    @DisplayName("Deletar foto perfil no disco deve executar normalmente quando foto existe")
    void deletarFotoPerfilNoDisco_FotoPerfilExiste_NaoDeveLancarExcecao() throws IOException {
    	//ARRANGE
        Path pathEsperado = Paths.get(System.getProperty("user.home"), ".shop", "imagens", "perfil", "teste.jpeg");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
        	
        	//ACT
        	fileService.deletarFotoPerfilNoDisco("teste.jpeg");

        	//ASSERT
            mockedFiles.verify(() -> Files.delete(pathEsperado));
        }
    }
    
    @Test
    @DisplayName("Deletar foto perfil no disco deve lançar exceção quando foto perfil não existe")
    void deletarFotoPerfilNoDisco_FotoPerfilNaoExiste_DeveLancarExcecao() throws IOException {
    	//ARRANGE
    	Path pathEsperado = Paths.get(System.getProperty("user.home"), ".shop", "imagens", "perfil", "teste.jpeg");
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
        	mockedFiles.when(() -> Files.delete(pathEsperado)).thenThrow(IOException.class);

        	//ASSERT + ACT
        	Assertions.assertThrows(IOException.class, () -> fileService.deletarFotoPerfilNoDisco("teste.jpeg"));
        }
    }
    
    @Test
    @DisplayName("Deletar foto perfil no disco deve lançar exceção quando tentar excluir foto de perfil padrão")
    void deletarFotoPerfilNoDisco_TentandoExcluirFotoPerfilPadrao_DeveLancarExcecao() throws IOException {
    	//ARRANGE
    	String fotoPadrao = "Padrao.jpeg";

        //ASSERT + ACT
        Assertions.assertThrows(ValidacaoException.class, () -> fileService.deletarFotoPerfilNoDisco(fotoPadrao));
    }
    
    @Test
    @DisplayName("Pegar foto perfil por nome deve executar normalmente quando enviado um nome válido")
    void pegarFotoPerfilPorNome_FotoExiste_NaoDeveLancarExcecao() {
    	//ARRANGE
    	String nomeArquivo = "arquivo1.jpeg";
    	byte[] bytes = {1, 2, 3};
    	Mockito.doReturn(bytes).when(fileService).recuperarBytesFotoPerfilDoDisco("arquivo1.jpeg");
    	
    	//ACT
    	ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(nomeArquivo);
    	
    	//ASSERT
    	Assertions.assertEquals(nomeArquivo, arquivoInfoDTO.nomeArquivo());
    	Assertions.assertEquals(bytes, arquivoInfoDTO.bytesArquivo());
    }
    
    @Test
    @DisplayName("Pegar foto perfil por nome deve lançar exceção quando nome do arquivo não existir no diretorio de arquivos")
    void pegarFotoPerfilPorNome_FotoNaoExiste_DeveLancarExcecao() {
    	//ARRANGE
    	String nomeArquivo = "arquivo1.jpeg";
        Mockito.doThrow(new FileStorageException("Falha ao acessar arquivo")).when(fileService).recuperarBytesFotoPerfilDoDisco("arquivo1.jpeg");
    	
    	//ASSERT + ACT
    	Assertions.assertThrows(FileStorageException.class, () -> fileService.pegarFotoPerfilPorNome(nomeArquivo));
    }
    
  @Test
  @DisplayName("Persistir foto perfil deve funcionar normalmente quando extensão do arquivo for válida (PNG, JPEG, JPG)")
  void persistirFotoPerfil_ExtensaoDoArquivoValida_NaoDeveLancarExcecao() throws IOException {
  	//ARRAGE
	  byte[] bytes1 = "conteúdo do arquivo 1".getBytes();
	  ByteArrayInputStream inputStream1 = new ByteArrayInputStream(bytes1);
	  when(arquivo1.getInputStream()).thenReturn(inputStream1);
	  when(arquivo1.getContentType()).thenReturn("image/jpeg");
      try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
          mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
          .thenAnswer(invocation -> null);
      	
      	  //ACT
      	  MultipartFile arquivo = arquivo1;
      	  UriComponentsBuilder uriBuilderBase = UriComponentsBuilder.fromUriString("http://localhost:8080");
      	  ArquivoInfoDTO resultado = fileService.persistirFotoPerfil(arquivo, uriBuilderBase);
      	
      	  //ASSERT
      	  Assertions.assertNotNull(resultado);
      	  Assertions.assertEquals(true, resultado.nomeArquivo().endsWith(".jpeg"));
      	  Assertions.assertTrue(resultado.uri().contains(".jpeg"));
      	  Assertions.assertArrayEquals(bytes1, resultado.bytesArquivo());
      }
  }
  
  @Test
  @DisplayName("Persistir foto perfil deve lançar exceção quando extensão do arquivo for diferente de PNG, JPEG, JPG")
  void persistirFotoPerfil_ExtensaoDoArquivoInvalida_DeveLancarExcecao() throws IOException {
  	//ARRAGE
      try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
      	when(arquivo1.getContentType()).thenReturn("application/pdf");
      	String nomeArquivo = "arquivo1.jpeg";
      	when(arquivo1.getOriginalFilename()).thenReturn(nomeArquivo);
      	
      	//ACT
      	MultipartFile arquivo = arquivo1;
      	UriComponentsBuilder uriBuilderBase = UriComponentsBuilder.fromUriString("http://localhost:8080");
      	ArquivoInfoDTO resultado = fileService.persistirFotoPerfil(arquivo, uriBuilderBase);
      	
      	//ASSERT
      	Assertions.assertNotNull(resultado);
      	Assertions.assertEquals(nomeArquivo, resultado.nomeArquivo());
      	Assertions.assertNotNull(resultado.erro());
      }
  }
  
  @Test
  @DisplayName("Alterar arquivo produto deve executar normalmente quando arquivo e nome valido são enviados")
  void alterarFotoPerfil_ArquivoENomeValidoEnviado_DeveExecutarNormalmente() throws IOException {
	  //ARRANGE
	  byte[] bytes1 = "conteúdo do arquivo 1".getBytes();
	  ByteArrayInputStream inputStream1 = new ByteArrayInputStream(bytes1);
	  when(arquivo1.getInputStream()).thenReturn(inputStream1);
	  when(arquivo1.getContentType()).thenReturn("image/jpeg");
	  String nomeArquivoASerSubstituido = "arquivosubstituido.jpeg";
	  Mockito.doNothing().when(fileService).verificarSeExisteFotoPerfilPorNome(nomeArquivoASerSubstituido);
      try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
          mockedFiles.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
          .thenAnswer(invocation -> null);
          
      	//ACT
      	MultipartFile arquivo = arquivo1;
      	ArquivoInfoDTO arquivoInfoDTO = fileService.alterarFotoPerfil(arquivo, nomeArquivoASerSubstituido);
      	
      	//ASSERT
      	Assertions.assertNotNull(arquivoInfoDTO);
      	Assertions.assertEquals(true, arquivoInfoDTO.nomeArquivo().endsWith(".jpeg"));
      	Assertions.assertArrayEquals(bytes1, arquivoInfoDTO.bytesArquivo());
      }
  }
  
  @Test
  @DisplayName("Alterar arquivo produto deve lançar exceção quando nome invalido é enviado")
  void alterarFotoPerfil_NomeInvalidoEnviado_DeveLancarExcecao() throws IOException {
	  // ARRANGE
	  when(arquivo1.getContentType()).thenReturn("image/jpeg");
	  String nomeArquivoASerSubstituido = "arquivosubstituido.jpeg";

	  // ASSERT + ACT
	  MultipartFile arquivo = arquivo1;
	  Assertions.assertThrows(ValidacaoException.class, () -> fileService.alterarFotoPerfil(arquivo, nomeArquivoASerSubstituido));
  }
  
  @Test
  @DisplayName("Alterar arquivo produto deve lançar exceção quando arquivo invalido é enviado")
  void alterarFotoPerfil_ArquivoInvalidoEnviado_DeveLancarExcecao() throws IOException {
	  // ARRANGE
	  when(arquivo1.getContentType()).thenReturn("application/pdf");
	  String nomeArquivoASerSubstituido = "arquivosubstituido.jpeg";
	  MultipartFile arquivo = arquivo1;

	  // ASSERT + ACT
	  Assertions.assertThrows(FileStorageException.class, () -> fileService.alterarFotoPerfil(arquivo, nomeArquivoASerSubstituido));
  }
  
  
//	------------------------------
//	------------------------------
//			PEDIDO
//	------------------------------
//	------------------------------
  
  @Test
  @DisplayName("Pegar arquivo produto por nome deve executar normalmente quando enviado um nome válido")
  void pegarImagemPedidoPorNome_ArquivoExiste_NaoDeveLancarExcecao() {
  	//ARRANGE
  	String nomeArquivo = "arquivo1.jpeg";
  	byte[] bytes = {1, 2, 3};
  	Mockito.doReturn(bytes).when(fileService).recuperarBytesImagemPedidoDoDisco("arquivo1.jpeg");
  	
  	//ACT
  	ArquivoInfoDTO arquivoInfoDTO = fileService.pegarImagemPedidoPorNome(nomeArquivo);
  	
  	//ASSERT
  	Assertions.assertEquals(nomeArquivo, arquivoInfoDTO.nomeArquivo());
  	Assertions.assertEquals(bytes, arquivoInfoDTO.bytesArquivo());
  }
  
  @Test
  @DisplayName("Pegar arquivo produto por nome deve lançar exceção quando nome do arquivo não existir no diretorio de arquivos")
  void pegarImagemPedidoPorNome_ArquivoNaoExiste_DeveLancarExcecao() {
  	//ARRANGE
  	String nomeArquivo = "arquivo1.jpeg";
      Mockito.doThrow(new FileStorageException("Falha ao acessar arquivo")).when(fileService).recuperarBytesImagemPedidoDoDisco("arquivo1.jpeg");
  	
  	//ASSERT + ACT
  	Assertions.assertThrows(FileStorageException.class, () -> fileService.pegarImagemPedidoPorNome(nomeArquivo));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  @DisplayName("Persistir foto perfil deve cadastrar nova imagem pedido no disco quando imagem pedido não já existir no disco")
  void persistirOuRecuperarImagemPedido_NaoExisteImagemPedidoNoDisco_DeveCadastrarNovaImagemPedido() throws IOException {
  	  //ARRAGE
	  String nomePrimeiraImagemProduto = "Padrao.jpeg";
	  byte[] bytes = "conteúdo do arquivo primeira imagem".getBytes();
	  when(fileService.verificarSeExisteImagemPedidoNoDisco(nomePrimeiraImagemProduto)).thenReturn(null);
	  when(fileService.recuperarBytesArquivoProdutoDoDisco(nomePrimeiraImagemProduto)).thenReturn(bytes);
      try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
    	  DirectoryStream<Path> emptyDirectoryStream = Mockito.mock(DirectoryStream.class);
          mockedFiles.when(() -> Files.newDirectoryStream(any())).thenReturn(emptyDirectoryStream);
          mockedFiles.when(() -> Files.write(any(Path.class), any(byte[].class), any(StandardOpenOption.class)))
          .thenAnswer(invocation -> null);
          
      	  //ACT
          Long idProduto = 1L;
      	  String resultado = fileService.persistirOuRecuperarImagemPedido(nomePrimeiraImagemProduto, idProduto);
      	
      	  //ASSERT
      	  Assertions.assertNotNull(resultado);
      	  Assertions.assertEquals(true, resultado.endsWith(".jpeg"));
      	  mockedFiles.verify(() -> Files.write(any(Path.class), any(byte[].class), any(StandardOpenOption.class)), times(1));
      }
  }
  
  @Test
  @DisplayName("Persistir foto perfil deve retornar imagem pedido do disco quando já existir no disco")
  void persistirOuRecuperarImagemPedido_ExisteImagemPedidoNoDisco_DeveRetornarImagemPedidoDoDisco() throws IOException {
  	  //ARRAGE
	  String nomePrimeiraImagemProduto = "Padrao.jpeg";
	  Long idProduto = 1L;
          
	  //ACT
      String resultado = fileService.persistirOuRecuperarImagemPedido(nomePrimeiraImagemProduto, idProduto);
      	
      //ASSERT
      Assertions.assertEquals(true, resultado.equals(nomePrimeiraImagemProduto));
  }
}
