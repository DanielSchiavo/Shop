package br.com.danielschiavo.shop.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import br.com.danielschiavo.shop.models.cliente.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.AlterarFotoPerfilDTO;
import br.com.danielschiavo.shop.models.cliente.CadastrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClientePaginaInicialDTO;
import br.com.danielschiavo.shop.models.endereco.CadastrarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.repositories.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@InjectMocks
	private ClienteService clienteService;
	
	@Mock
	private Cliente cliente;
	
	@Captor
	private ArgumentCaptor<Cliente> clienteCaptor;
	
	@Mock
	private FileStorageService fileService;
	
	@Mock
	private ClienteRepository clienteRepository;
	
	@Test
	@DisplayName("Deletar foto perfil por id token deve lançar exceção quando tentar excluir foto Padrão")
	void deletarFotoPerfilPorIdToken_ClienteTemFotoPadrao_DeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getFotoPerfil()).thenReturn("Padrao.jpeg");
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> clienteService.deletarFotoPerfilPorIdToken());
	}
	
	@Test
	@DisplayName("Deletar foto perfil por id token deve remover a foto perfil do cliente e definir a foto do cliente como Padrao.jpeg")
	void deletarFotoPerfilPorIdToken_ClienteNaoTemFotoPadrao_NaoDeveLancarExcecao() throws IOException {
		//ARRANGE
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", null, null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		
		//ACT
		clienteService.deletarFotoPerfilPorIdToken();
		
		//ASSERT
		verify(clienteRepository).save(any(Cliente.class));
		Assertions.assertEquals("Padrao.jpeg", cliente.getFotoPerfil());
	}
	
	@Test
	@DisplayName("Detalhar cliente por id token pagina inicial deve retornar dados normalmente quando usuario logado no sistema")
	void detalharClientePorIdTokenPaginaInicial() {
		//ARRANGE
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", null, null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		byte[] bytes = "Bytesimagemdummy".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("outrafoto.jpeg", bytes);
		BDDMockito.when(fileService.pegarFotoPerfilPorNome(any())).thenReturn(arquivoInfoDTO);
		
		//ACT
		MostrarClientePaginaInicialDTO mostrarClientePaginaInicialDTO = clienteService.detalharClientePorIdTokenPaginaInicial();
		
		//ASSERT
		Assertions.assertEquals(cliente.getNome(), mostrarClientePaginaInicialDTO.nome());
		Assertions.assertEquals(bytes, mostrarClientePaginaInicialDTO.fotoPerfil().bytesArquivo());
		Assertions.assertEquals(arquivoInfoDTO.nomeArquivo(), mostrarClientePaginaInicialDTO.fotoPerfil().nomeArquivo());
	}
	
	@Test
	@DisplayName("Detalhar cliente por id token deve retornar dados normalmente quando usuario logado no sistema")
	void detalharClientePorIdToken() {
		//ARRANGE
		Endereco endereco = new Endereco(null, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		Endereco endereco2 = new Endereco(null, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", false, cliente);
		List<Endereco> listaEndereco = new ArrayList<>();
		listaEndereco.addAll(List.of(endereco, endereco2));
		List<Cartao> listaCartoes = new ArrayList<>();
		Cartao cartao = new Cartao(null, "Santander", "1123444255591132", "Daniel schiavo rosseto", "03/25", true, TipoCartao.CREDITO, cliente);
		listaCartoes.add(cartao);
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", listaEndereco, listaCartoes, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		byte[] bytes = "Bytesimagemdummy".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("outrafoto.jpeg", bytes);
		BDDMockito.when(fileService.pegarFotoPerfilPorNome(any())).thenReturn(arquivoInfoDTO);
		
		//ACT
		MostrarClienteDTO mostrarClienteDTO = clienteService.detalharClientePorIdToken();
		
		//ASSERT
		Assertions.assertNotNull(mostrarClienteDTO);
		Assertions.assertEquals(cliente.getId(), mostrarClienteDTO.id());
		Assertions.assertEquals(cliente.getCpf(), mostrarClienteDTO.cpf());
		Assertions.assertEquals(cliente.getNome(), mostrarClienteDTO.nome());
		Assertions.assertEquals(cliente.getSobrenome(), mostrarClienteDTO.sobrenome());
		Assertions.assertEquals(cliente.getDataNascimento(), mostrarClienteDTO.dataNascimento());
		Assertions.assertEquals(cliente.getDataCriacaoConta(), mostrarClienteDTO.dataCriacaoConta());
		Assertions.assertEquals(cliente.getEmail(), mostrarClienteDTO.email());
		Assertions.assertEquals(cliente.getCelular(), mostrarClienteDTO.celular());
		//ArquivoInfoDTO
		Assertions.assertEquals(arquivoInfoDTO.nomeArquivo(), mostrarClienteDTO.fotoPerfil().nomeArquivo());
		Assertions.assertEquals(arquivoInfoDTO.bytesArquivo(), mostrarClienteDTO.fotoPerfil().bytesArquivo());
		//Endereco
		Assertions.assertNotNull(mostrarClienteDTO.enderecos());
		Assertions.assertEquals(cliente.getEnderecos().size(), mostrarClienteDTO.enderecos().size());
		for (int i = 0; i < mostrarClienteDTO.enderecos().size(); i++) {
		    Endereco enderecoEsperado = listaEndereco.get(i);
		    MostrarEnderecoDTO enderecoResultado = mostrarClienteDTO.enderecos().get(i);

		    Assertions.assertEquals(enderecoEsperado.getId(), enderecoResultado.id(), "O ID do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getCep(), enderecoResultado.cep(), "O CEP do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getRua(), enderecoResultado.rua(), "A rua do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getNumero(), enderecoResultado.numero(), "O número do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getComplemento(), enderecoResultado.complemento(), "O complemento do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getBairro(), enderecoResultado.bairro(), "O bairro do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getCidade(), enderecoResultado.cidade(), "A cidade do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getEstado(), enderecoResultado.estado(), "O estado do endereço deve ser igual");
		    Assertions.assertEquals(enderecoEsperado.getEnderecoPadrao(), enderecoResultado.enderecoPadrao(), "A indicação de endereço padrão deve ser igual");
		}
		//Cartoes
		Assertions.assertNotNull(mostrarClienteDTO.cartoes());
		Assertions.assertEquals(cliente.getCartoes().size(), mostrarClienteDTO.cartoes().size());
		for (int i = 0; i < mostrarClienteDTO.cartoes().size(); i++) {
		    Cartao cartaoEsperado = listaCartoes.get(i);
		    MostrarCartaoDTO cartaoResultado = mostrarClienteDTO.cartoes().get(i);

		    Assertions.assertEquals(cartaoEsperado.getId(), cartaoResultado.id(), "O ID do cartão deve ser igual");
		    Assertions.assertEquals(cartaoEsperado.getNomeBanco(), cartaoResultado.nomeBanco(), "O nome do banco do cartão deve ser igual");
		    Assertions.assertEquals(cartaoEsperado.getNumeroCartao(), cartaoResultado.numeroCartao(), "O número do cartão deve ser igual");
		    Assertions.assertEquals(cartaoEsperado.getTipoCartao(), cartaoResultado.tipoCartao(), "O tipo do cartão deve ser igual");
		    Assertions.assertEquals(cartaoEsperado.getCartaoPadrao(), cartaoResultado.cartaoPadrao(), "A indicação de cartão padrão deve ser igual");
		}
	}
	
	@Test
	@DisplayName("Cadastrar cliente deve funcionar normalmente quando CadastrarClienteDTO valido é enviado com endereço")
	void cadastrarCliente_DtoEnviadoValidoEComEndereco() {
		//ARRANGE
		CadastrarEnderecoDTO cadastrarEnderecoDTO = new CadastrarEnderecoDTO("29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true);
		CadastrarClienteDTO cadastrarClienteDTO = new CadastrarClienteDTO("12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", cadastrarEnderecoDTO);
		
		//ACT
		MostrarClienteDTO mostrarClienteDTO = clienteService.cadastrarCliente(cadastrarClienteDTO);
		
		//ASSERT
		BDDMockito.then(clienteRepository).should().save(clienteCaptor.capture());
		Cliente clienteSalvo = clienteCaptor.getValue();
		Assertions.assertNotNull(mostrarClienteDTO);
		Assertions.assertEquals(clienteSalvo.getCpf(), mostrarClienteDTO.cpf());
		Assertions.assertEquals(clienteSalvo.getNome(), mostrarClienteDTO.nome());
		Assertions.assertEquals(clienteSalvo.getSobrenome(), mostrarClienteDTO.sobrenome());
		Assertions.assertEquals(clienteSalvo.getDataNascimento(), mostrarClienteDTO.dataNascimento());
		Assertions.assertEquals(clienteSalvo.getDataCriacaoConta(), mostrarClienteDTO.dataCriacaoConta());
		Assertions.assertEquals(clienteSalvo.getEmail(), mostrarClienteDTO.email());
		Assertions.assertEquals(clienteSalvo.getCelular(), mostrarClienteDTO.celular());
		//Endereco
		Assertions.assertNotNull(mostrarClienteDTO.enderecos());
		Assertions.assertEquals(clienteSalvo.getEnderecos().size(), mostrarClienteDTO.enderecos().size());
		for (int i = 0; i < mostrarClienteDTO.enderecos().size(); i++) {
		    MostrarEnderecoDTO enderecoResultado = mostrarClienteDTO.enderecos().get(i);

		    Assertions.assertEquals(cadastrarEnderecoDTO.cep(), enderecoResultado.cep(), "O CEP do endereço deve ser igual");
		    Assertions.assertEquals(cadastrarEnderecoDTO.rua(), enderecoResultado.rua(), "A rua do endereço deve ser igual");
		    Assertions.assertEquals(cadastrarEnderecoDTO.numero(), enderecoResultado.numero(), "O número do endereço deve ser igual");
		    Assertions.assertEquals(cadastrarEnderecoDTO.complemento(), enderecoResultado.complemento(), "O complemento do endereço deve ser igual");
		    Assertions.assertEquals(cadastrarEnderecoDTO.bairro(), enderecoResultado.bairro(), "O bairro do endereço deve ser igual");
		    Assertions.assertEquals(cadastrarEnderecoDTO.cidade(), enderecoResultado.cidade(), "A cidade do endereço deve ser igual");
		    Assertions.assertEquals(cadastrarEnderecoDTO.estado(), enderecoResultado.estado(), "O estado do endereço deve ser igual");
		    Assertions.assertEquals(cadastrarEnderecoDTO.enderecoPadrao(), enderecoResultado.enderecoPadrao(), "A indicação de endereço padrão deve ser igual");
		}
	}
	
	@Test
	@DisplayName("Cadastrar cliente deve funcionar normalmente quando CadastrarClienteDTO valido é enviado sem endereço")
	void cadastrarCliente_DtoEnviadoValidoESemEndereco() {
		//ARRANGE
		CadastrarClienteDTO cadastrarClienteDTO = new CadastrarClienteDTO("12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", null);
		
		//ACT
		MostrarClienteDTO mostrarClienteDTO = clienteService.cadastrarCliente(cadastrarClienteDTO);
		
		//ASSERT
		BDDMockito.then(clienteRepository).should().save(clienteCaptor.capture());
		Cliente clienteSalvo = clienteCaptor.getValue();
		Assertions.assertNotNull(mostrarClienteDTO);
		Assertions.assertEquals(clienteSalvo.getCpf(), mostrarClienteDTO.cpf());
		Assertions.assertEquals(clienteSalvo.getNome(), mostrarClienteDTO.nome());
		Assertions.assertEquals(clienteSalvo.getSobrenome(), mostrarClienteDTO.sobrenome());
		Assertions.assertEquals(clienteSalvo.getDataNascimento(), mostrarClienteDTO.dataNascimento());
		Assertions.assertEquals(clienteSalvo.getDataCriacaoConta(), mostrarClienteDTO.dataCriacaoConta());
		Assertions.assertEquals(clienteSalvo.getEmail(), mostrarClienteDTO.email());
		Assertions.assertEquals(clienteSalvo.getCelular(), mostrarClienteDTO.celular());
	}
	
	@Test
	@DisplayName("Alterar cliente por id token deve funcionar normalmente se dto enviado está correto")
	void alterarClientePorIdToken() {
		//ARRANGE
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", new ArrayList<Endereco>(), new ArrayList<Cartao>(), null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		AlterarClienteDTO alterarClienteDTO = new AlterarClienteDTO("12345612321", "Silvana", "Silva Santana", LocalDate.of(1999, 3, 3), "silvanasantana@gmail.com", "654321", "27998321332");
		byte[] bytes = "Bytesimagemdummy".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("outrafoto.jpeg", bytes);
		BDDMockito.when(fileService.pegarFotoPerfilPorNome(any())).thenReturn(arquivoInfoDTO);
		
		//ACT
		MostrarClienteDTO mostrarClienteDTO = clienteService.alterarClientePorIdToken(alterarClienteDTO);
		
		//ASSERT
		Assertions.assertNotNull(mostrarClienteDTO);
		Assertions.assertEquals(cliente.getId(), mostrarClienteDTO.id());
		Assertions.assertEquals(cliente.getCpf(), mostrarClienteDTO.cpf());
		Assertions.assertEquals(cliente.getNome(), mostrarClienteDTO.nome());
		Assertions.assertEquals(cliente.getSobrenome(), mostrarClienteDTO.sobrenome());
		Assertions.assertEquals(cliente.getDataNascimento(), mostrarClienteDTO.dataNascimento());
		Assertions.assertEquals(cliente.getDataCriacaoConta(), mostrarClienteDTO.dataCriacaoConta());
		Assertions.assertEquals(cliente.getEmail(), mostrarClienteDTO.email());
		Assertions.assertEquals(cliente.getCelular(), mostrarClienteDTO.celular());
		//ArquivoInfoDTO
		Assertions.assertEquals(arquivoInfoDTO.nomeArquivo(), mostrarClienteDTO.fotoPerfil().nomeArquivo());
		Assertions.assertEquals(arquivoInfoDTO.bytesArquivo(), mostrarClienteDTO.fotoPerfil().bytesArquivo());
	}
	
	@Test
	@DisplayName("Alterar foto perfil por id token deve executar normalmente quando dto enviado é valido")
	void alterarFotoPerfilPorIdToken() {
		//ARRANGE
		AlterarFotoPerfilDTO alterarFotoPerfilDTO = new AlterarFotoPerfilDTO("Novaimagem.jpeg");
		byte[] bytes = "Bytesimagemdummy".getBytes();
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO("Novaimagem.jpeg", bytes);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(fileService.pegarFotoPerfilPorNome(any())).thenReturn(arquivoInfoDTO);
		
		//ACT
		ArquivoInfoDTO retornoMetodoArquivoInfoDTO = clienteService.alterarFotoPerfilPorIdToken(alterarFotoPerfilDTO);
		
		//ASSERT
		BDDMockito.then(cliente).should().setFotoPerfil(alterarFotoPerfilDTO.nomeNovaFotoPerfil());
		BDDMockito.then(clienteRepository).should().save(cliente);
		Assertions.assertEquals(arquivoInfoDTO.nomeArquivo(), retornoMetodoArquivoInfoDTO.nomeArquivo());
		Assertions.assertEquals(arquivoInfoDTO.bytesArquivo(), retornoMetodoArquivoInfoDTO.bytesArquivo());
	}

}
