package br.com.danielschiavo.shop.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.Cartao;
import br.com.danielschiavo.shop.models.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.models.cartao.TipoCartao;
import br.com.danielschiavo.shop.models.cartao.validacoes.ValidadorCadastrarNovoCartao;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.CartaoRepository;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@InjectMocks
	private CartaoService cartaoService;
	
	@Mock
	private Cliente cliente;
	
	@Mock
	private CartaoRepository cartaoRepository;
	
	@Captor
	private ArgumentCaptor<Cartao> cartaoCaptor;
	
	@Spy
	private List<ValidadorCadastrarNovoCartao> validadores = new ArrayList<>();
	
	@Mock
	private ValidadorCadastrarNovoCartao validador1;
	
	@Mock
	private ValidadorCadastrarNovoCartao validador2;
	
	@Test
	@DisplayName("Deletar cartao por id token deve excluir o cartão dos cartões do cliente se cartão existe")
	void deletarCartaoPorIdToken_CartaoExiste_DeveExcluirOCartao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		List<Cartao> listaCartao = new ArrayList<>();
		Cartao cartao = new Cartao(1L, "Santander", "1234567812345678", "Daniel schiavo rosseto", "03/28", true, TipoCartao.CREDITO, cliente);
		Cartao cartao2 = new Cartao(2L, "Santander", "8765432187654321", "Fernando henrique cardoso", "03/28", true, TipoCartao.CREDITO, cliente);
		listaCartao.addAll(List.of(cartao, cartao2));
		BDDMockito.when(cliente.getCartoes()).thenReturn(listaCartao);
		Long idCartao = 1L;
		
		//ACT
		cartaoService.deletarCartaoPorIdToken(idCartao);
		
		//ASSERT
		BDDMockito.then(cartaoRepository).should().delete(cartao);
		Assertions.assertEquals(true, cliente.getCartoes().stream().filter(c -> c.getId() == idCartao).findFirst().isEmpty());
	}
	
	@Test
	@DisplayName("Deletar cartao por id token deve lançar exceção quando id do cartão não existir para o cliente")
	void deletarCartaoPorIdToken_CartaoNaoExiste_DeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		List<Cartao> listaCartao = new ArrayList<>();
		Cartao cartao = new Cartao(1L, "Santander", "1234567812345678", "Daniel schiavo rosseto", "03/28", true, TipoCartao.CREDITO, cliente);
		Cartao cartao2 = new Cartao(2L, "Santander", "8765432187654321", "Fernando henrique cardoso", "03/28", true, TipoCartao.CREDITO, cliente);
		listaCartao.addAll(List.of(cartao, cartao2));
		BDDMockito.when(cliente.getCartoes()).thenReturn(listaCartao);
		Long idCartao = 3L;
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> cartaoService.deletarCartaoPorIdToken(idCartao));
	}
	
	@Test
	@DisplayName("Pegar cartões cliente por id token deve retornar todos os cartões do cliente se ele tiver pelo menos 1 cartão")
	void pegarCartoesClientePorIdToken_CartoesExistem_DeveRetornarOsCartoes() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		List<Cartao> listaCartao = new ArrayList<>();
		Cartao cartao = new Cartao(1L, "Santander", "1234567812345678", "Daniel schiavo rosseto", "03/28", true, TipoCartao.CREDITO, cliente);
		Cartao cartao2 = new Cartao(2L, "Santander", "8765432187654321", "Fernando henrique cardoso", "03/28", true, TipoCartao.CREDITO, cliente);
		listaCartao.addAll(List.of(cartao, cartao2));
		BDDMockito.when(cliente.getCartoes()).thenReturn(listaCartao);
		
		//ACT
		List<MostrarCartaoDTO> listaMostrarCartaoDTO = cartaoService.pegarCartoesClientePorIdToken();
		
		//ASSERT
		Assertions.assertEquals(listaCartao.size(), listaMostrarCartaoDTO.size());
		Assertions.assertEquals(listaCartao.get(0).getNumeroCartao(), listaMostrarCartaoDTO.get(0).numeroCartao());
		Assertions.assertEquals(listaCartao.get(0).getId(), listaMostrarCartaoDTO.get(0).id());
		Assertions.assertEquals(listaCartao.get(1).getNumeroCartao(), listaMostrarCartaoDTO.get(1).numeroCartao());
		Assertions.assertEquals(listaCartao.get(1).getId(), listaMostrarCartaoDTO.get(1).id());
	}
	
	@Test
	@DisplayName("Pegar cartões cliente por id token deve lançar exceção quando cliente não tem nenhum cartão")
	void pegarCartoesClientePorIdToken_ClienteNaoTemCartao_DeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCartoes()).thenReturn(new ArrayList<Cartao>());
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> cartaoService.pegarCartoesClientePorIdToken());
	}
	
	@Test
	@DisplayName("Cadastrar novo cartão por id token deve executar normalmente quando dto enviado é valido e tem outro cartão como cartaoPadrao true")
	void cadastrarNovoCartaoPorIdToken_DtoEnviadoValidoTemOutroCartaoPadraoTrue_NaoDeveLancarExcecao() {
		//ARRANGE
		CadastrarCartaoDTO cadastrarCartaoDTO = new CadastrarCartaoDTO("03/28", "1111222233334444", "Jucelino kubchecker", true, TipoCartao.CREDITO);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		List<Cartao> listaCartao = new ArrayList<>();
		Cartao cartao1 = new Cartao(1L, "Santander", "1234567812345678", "Daniel schiavo rosseto", "03/28", true, TipoCartao.CREDITO, cliente);
		Cartao cartao2 = new Cartao(2L, "Santander", "8765432187654321", "Fernando henrique cardoso", "03/28", true, TipoCartao.CREDITO, cliente);
		listaCartao.addAll(List.of(cartao1, cartao2));
		BDDMockito.when(cliente.getCartoes()).thenReturn(listaCartao);
		validadores.addAll(List.of(validador1, validador2));
		
		//ACT
		cartaoService.cadastrarNovoCartaoPorIdToken(cadastrarCartaoDTO);
		
		//ASSERT
		BDDMockito.then(validador1).should().validar(cadastrarCartaoDTO, cliente);
		BDDMockito.then(validador2).should().validar(cadastrarCartaoDTO, cliente);
		verify(cartaoRepository, times(3)).save(cartaoCaptor.capture());
		List<Cartao> allSavedCards = cartaoCaptor.getAllValues();
		Cartao novoCartaoCapturado = allSavedCards.get(allSavedCards.size() - 1); // Pega o último cartão salvo
		Assertions.assertEquals("Falta implementar API banco", novoCartaoCapturado.getNomeBanco());
	}
	
	@Test
	@DisplayName("Cadastrar novo cartão por id token deve executar normalmente quando dto enviado contém cartaoPadrao true e é valido porém cliente não possui outro cartão")
	void cadastrarNovoCartaoPorIdToken_DtoEnviadoValidoComCartaoPadraoTrueEListaCartoesClienteEstaVazia_NaoDeveLancarExcecao() {
		//ARRANGE
		CadastrarCartaoDTO cadastrarCartaoDTO = new CadastrarCartaoDTO("03/28", "1111222233334444", "Jucelino kubchecker", true, TipoCartao.CREDITO);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCartoes()).thenReturn(new ArrayList<Cartao>());
		validadores.addAll(List.of(validador1, validador2));
		
		//ACT
		cartaoService.cadastrarNovoCartaoPorIdToken(cadastrarCartaoDTO);
		
		//ASSERT
		BDDMockito.then(validador1).should().validar(cadastrarCartaoDTO, cliente);
		BDDMockito.then(validador2).should().validar(cadastrarCartaoDTO, cliente);
		verify(cartaoRepository).save(cartaoCaptor.capture());
		Cartao capturado = cartaoCaptor.getValue();
		Assertions.assertEquals("Falta implementar API banco", capturado.getNomeBanco());
	}

	@Test
	@DisplayName("Alterar cartao padrao por id token quando id do cartão fornecido for true deve mudar para false")
	void alterarCartaoPadraoPorIdToken_EraCartaoPadraoTrueEVirouCartaoPadraoFalse_NaoDeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		List<Cartao> listaCartao = new ArrayList<>();
		Cartao cartao1 = new Cartao(1L, "Santander", "1234567812345678", "Daniel schiavo rosseto", "03/28", true, TipoCartao.CREDITO, cliente);
		Cartao cartao2 = new Cartao(2L, "Santander", "8765432187654321", "Fernando henrique cardoso", "03/28", false, TipoCartao.CREDITO, cliente);
		listaCartao.addAll(List.of(cartao1, cartao2));
		BDDMockito.when(cliente.getCartoes()).thenReturn(listaCartao);
		Long idCartao = 1L;
		
		//ACT
		cartaoService.alterarCartaoPadraoPorIdToken(idCartao);
		
		//ASSERT
		verify(cartaoRepository, times(1)).save(cartaoCaptor.capture());
		Assertions.assertEquals(false, cartao1.getCartaoPadrao());// cartão1 era true, virou false
		Assertions.assertEquals(false, cartao2.getCartaoPadrao()); // cartão2 continua igual, não é alterado
	}
	
	@Test
	@DisplayName("Alterar cartao padrao por id token quando id do cartão fornecido for false deve mudar para true e todos os outros cartões do cliente que forem cartaoPadrao true para false")
	void alterarCartaoPadraoPorIdToken_EraCartaoPadraoFalseEVirouCartaoPadraoTrue_NaoDeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		List<Cartao> listaCartao = new ArrayList<>();
		Cartao cartao1 = new Cartao(1L, "Santander", "1234567812345678", "Daniel schiavo rosseto", "03/28", false, TipoCartao.CREDITO, cliente);
		Cartao cartao2 = new Cartao(2L, "Santander", "8765432187654321", "Fernando henrique cardoso", "03/28", true, TipoCartao.CREDITO, cliente);
		Cartao cartao3 = new Cartao(3L, "Santander", "1234567887654321", "Jucelino kubchecker", "03/28", true, TipoCartao.CREDITO, cliente);
		listaCartao.addAll(List.of(cartao1, cartao2, cartao3));
		BDDMockito.when(cliente.getCartoes()).thenReturn(listaCartao);
		Long idCartao = 1L;
		
		//ACT
		cartaoService.alterarCartaoPadraoPorIdToken(idCartao);
		
		//ASSERT
		verify(cartaoRepository, times(3)).save(cartaoCaptor.capture());
		Assertions.assertEquals(true, cartao1.getCartaoPadrao()); //cartao1 era false agora virou true
		Assertions.assertEquals(false, cartao2.getCartaoPadrao()); //cartao2 era true agora virou false
		Assertions.assertEquals(false, cartao3.getCartaoPadrao()); //cartao3 era true agora virou false
	}
}
