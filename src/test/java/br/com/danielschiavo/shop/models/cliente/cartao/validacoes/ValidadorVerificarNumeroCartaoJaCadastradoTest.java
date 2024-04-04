package br.com.danielschiavo.shop.models.cliente.cartao.validacoes;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.cartao.CadastrarCartaoDTO;
import br.com.danielschiavo.shop.models.cliente.cartao.Cartao;
import br.com.danielschiavo.shop.repositories.cliente.CartaoRepository;

@ExtendWith(MockitoExtension.class)
class ValidadorVerificarNumeroCartaoJaCadastradoTest {

	@InjectMocks
	private ValidadorVerificarNumeroCartaoJaCadastrado validador;
	
	@Mock
	private CadastrarCartaoDTO cadastrarCartaoDTO;
	
	@Mock
	private CartaoRepository cartaoRepository;
	
	@Mock
	private Cliente cliente;
	
	@Mock
	private Cartao cartao;
	
	@Test
	@DisplayName("Validador de número de cartão não deve lançar exceção para cartão novo com número e tipo únicos")
	void ValidadorVerificarNumeroCartaoJaCadastrado_ClienteNaoTemCartaoIgualCadastrado_NaoDeveLancarExcecao() {
		BDDMockito.given(cartaoRepository.findByNumeroCartaoAndTipoCartaoAndCliente(cadastrarCartaoDTO.numeroCartao(), cadastrarCartaoDTO.tipoCartao(), cliente)).willReturn(Optional.empty());
		
		Assertions.assertDoesNotThrow(() -> validador.validar(cadastrarCartaoDTO, cliente));
	}
	
	@Test
	@DisplayName("Validador de número de cartão deve lançar exceção para cartão novo com numeroCartao e tipoCartao já cadastrado")
	void ValidadorVerificarNumeroCartaoJaCadastrado_ClienteTemCartaoIgualCadastrado_DeveLancarExcecao() {
		BDDMockito.given(cartaoRepository.findByNumeroCartaoAndTipoCartaoAndCliente(cadastrarCartaoDTO.numeroCartao(), cadastrarCartaoDTO.tipoCartao(), cliente)).willReturn(Optional.of(cartao));
		
		Assertions.assertThrows(ValidacaoException.class, () -> validador.validar(cadastrarCartaoDTO, cliente));
	}

}
