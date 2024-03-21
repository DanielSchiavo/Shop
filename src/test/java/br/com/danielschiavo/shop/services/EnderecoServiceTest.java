package br.com.danielschiavo.shop.services;

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
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.AlterarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.CadastrarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.repositories.EnderecoRepository;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {
	
	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@InjectMocks
	private EnderecoService enderecoService;
	
	@Mock
	private Cliente cliente;
	
	@Mock
	private EnderecoRepository enderecoRepository;
	
	@Captor
	private ArgumentCaptor<Endereco> enderecoCaptor;

	@Test
	@DisplayName("Deletar enedereco por id token deve funcionar normalmente quando id endereco fornecido existe")
	void deletarEnderecoPorIdToken_idEnderecoExiste_NaoDeveLancarExcecao() {
		//ARRANGE
		Endereco endereco = new Endereco(1L, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		Endereco endereco2 = new Endereco(2L, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", false, cliente);
		List<Endereco> listaEndereco = new ArrayList<>();
		listaEndereco.addAll(List.of(endereco, endereco2));
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", listaEndereco, null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		Long idEndereco = 2L;
		
		//ACT
		enderecoService.deletarEnderecoPorIdToken(idEndereco);
		
		//ASSERT
		BDDMockito.then(enderecoRepository).should().delete(enderecoCaptor.capture());
	}

	@Test
	@DisplayName("Deletar enedereco por id token deve lançar exceção quando id endereco fornecido não existe")
	void deletarEnderecoPorIdToken_idEnderecoNaoExiste_DeveLancarExcecao() {
		//ARRANGE
		Endereco endereco = new Endereco(1L, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		Endereco endereco2 = new Endereco(2L, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", false, cliente);
		List<Endereco> listaEndereco = new ArrayList<>();
		listaEndereco.addAll(List.of(endereco, endereco2));
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", listaEndereco, null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		Long idEndereco = 3L;
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> enderecoService.deletarEnderecoPorIdToken(idEndereco));
	}
	
	@Test
	@DisplayName("Pegar enderecos cliente por id token deve funcionar normalmente quando cliente tem enderecos cadastrados")
	void pegarEnderecosClientePorIdToken_ClienteTemEnderecosCadastrados_NaoDeveLancarExcecao() {
		//ARRANGE
		Endereco endereco = new Endereco(1L, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		Endereco endereco2 = new Endereco(2L, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", false, cliente);
		List<Endereco> listaEndereco = new ArrayList<>();
		listaEndereco.addAll(List.of(endereco, endereco2));
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", listaEndereco, null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		
		//ACT
		List<MostrarEnderecoDTO> listaMostrarEnderecoDTO = enderecoService.pegarEnderecosClientePorIdToken();
		
		//ASSERT
		Assertions.assertEquals(listaEndereco.size(), listaMostrarEnderecoDTO.size());
		for (int i = 0; i < listaMostrarEnderecoDTO.size(); i++) {
		    MostrarEnderecoDTO enderecoResultado = listaMostrarEnderecoDTO.get(i);

		    Assertions.assertEquals(listaEndereco.get(i).getCep(), enderecoResultado.cep(), "O CEP do endereço deve ser igual");
		    Assertions.assertEquals(listaEndereco.get(i).getRua(), enderecoResultado.rua(), "A rua do endereço deve ser igual");
		    Assertions.assertEquals(listaEndereco.get(i).getNumero(), enderecoResultado.numero(), "O número do endereço deve ser igual");
		    Assertions.assertEquals(listaEndereco.get(i).getComplemento(), enderecoResultado.complemento(), "O complemento do endereço deve ser igual");
		    Assertions.assertEquals(listaEndereco.get(i).getBairro(), enderecoResultado.bairro(), "O bairro do endereço deve ser igual");
		    Assertions.assertEquals(listaEndereco.get(i).getCidade(), enderecoResultado.cidade(), "A cidade do endereço deve ser igual");
		    Assertions.assertEquals(listaEndereco.get(i).getEstado(), enderecoResultado.estado(), "O estado do endereço deve ser igual");
		    Assertions.assertEquals(listaEndereco.get(i).getEnderecoPadrao(), enderecoResultado.enderecoPadrao(), "A indicação de endereço padrão deve ser igual");
		}
	}
	
	@Test
	@DisplayName("Pegar enderecos cliente por id token deve lançar exceção quando cliente não tem enderecos cadastrados")
	void pegarEnderecosClientePorIdToken_ClienteNaoTemEnderecosCadastrados_DeveLancarExcecao() {
		//ARRANGE
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", new ArrayList<Endereco>(), null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> enderecoService.pegarEnderecosClientePorIdToken());
	}
	
	@Test
	@DisplayName("Cadastrar novo endereço por id token deve executar normalmente quando enviado CadastrarEnderecoDTO correto")
	void cadastrarNovoEnderecoPorIdToken() {
		//ARRANGE
		Endereco endereco = new Endereco(1L, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		Endereco endereco2 = new Endereco(2L, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", false, cliente);
		List<Endereco> listaEndereco = new ArrayList<>();
		listaEndereco.addAll(List.of(endereco, endereco2));
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", listaEndereco, null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		CadastrarEnderecoDTO cadastrarEnderecoDTO = new CadastrarEnderecoDTO("21452872", "Dummy", "21", "Sem complemento", "Campo grande", "Cariacica", "ES", true);
		
		//ACT
		MostrarEnderecoDTO mostrarEnderecoDTO = enderecoService.cadastrarNovoEnderecoPorIdToken(cadastrarEnderecoDTO);
		
		//ASSERT
		Assertions.assertEquals(listaEndereco.get(2).getCep(), mostrarEnderecoDTO.cep(), "O CEP do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(2).getRua(), mostrarEnderecoDTO.rua(), "A rua do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(2).getNumero(), mostrarEnderecoDTO.numero(), "O número do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(2).getComplemento(), mostrarEnderecoDTO.complemento(), "O complemento do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(2).getBairro(), mostrarEnderecoDTO.bairro(), "O bairro do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(2).getCidade(), mostrarEnderecoDTO.cidade(), "A cidade do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(2).getEstado(), mostrarEnderecoDTO.estado(), "O estado do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(2).getEnderecoPadrao(), mostrarEnderecoDTO.enderecoPadrao(), "A indicação de endereço padrão deve ser igual");
	}
	
	@Test
	@DisplayName("Alterar endereco por id token deve executar normalmente quando id fornecido existe, é do cliente e AlterarEnderecoDTO é válido")
	void alterarEnderecoPorIdToken() {
		//ARRANGE
		Endereco endereco = new Endereco(1L, "29142298", "Divinopolis", "15", "Sem complemento", "Bela vista", "Cariacica", "ES", true, cliente);
		Endereco endereco2 = new Endereco(2L, "29152291", "Avenida luciano das neves", "3233", "Apartamento 302", "Praia de itaparica", "Vila velha", "ES", false, cliente);
		List<Endereco> listaEndereco = new ArrayList<>();
		listaEndereco.addAll(List.of(endereco, endereco2));
		Cliente cliente = new Cliente(null, "12345678994", "Silvana", "Pereira da silva", LocalDate.of(2000, 3, 3), LocalDate.now(), "silvana.dasilva@gmail.com", "{noop}123456", "27999833653", "outrafoto.jpeg", listaEndereco, null, null);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		AlterarEnderecoDTO alterarEnderecoDTO = new AlterarEnderecoDTO("12345678", "Itapua", "35", "com complemento", "aquele bairro", "serra", "ES", true);
		
		//ACT
		MostrarEnderecoDTO mostrarEnderecoDTO = enderecoService.alterarEnderecoPorIdToken(alterarEnderecoDTO, 1L);
		
		//ASSERT
		Assertions.assertEquals(listaEndereco.get(0).getCep(), mostrarEnderecoDTO.cep(), "O CEP do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(0).getRua(), mostrarEnderecoDTO.rua(), "A rua do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(0).getNumero(), mostrarEnderecoDTO.numero(), "O número do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(0).getComplemento(), mostrarEnderecoDTO.complemento(), "O complemento do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(0).getBairro(), mostrarEnderecoDTO.bairro(), "O bairro do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(0).getCidade(), mostrarEnderecoDTO.cidade(), "A cidade do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(0).getEstado(), mostrarEnderecoDTO.estado(), "O estado do endereço deve ser igual");
		Assertions.assertEquals(listaEndereco.get(0).getEnderecoPadrao(), mostrarEnderecoDTO.enderecoPadrao(), "A indicação de endereço padrão deve ser igual");
	}
}
