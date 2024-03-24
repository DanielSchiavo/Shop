package br.com.danielschiavo.shop.services;


import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
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
import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.carrinho.MostrarCarrinhoClienteDTO;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivosProduto;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;

	@Mock
	private CarrinhoRepository carrinhoRepository;
	
	@InjectMocks
	private CarrinhoService carrinhoService;
	
	@Mock
	private Cliente cliente;
	
	@Mock
	private Carrinho carrinho;
	
	@Mock
	private Produto produto;
	
	@Mock
	private FileStorageService fileService;
	
	@Mock
	private ProdutoService produtoService;
	
	@Mock
	private ProdutoRepository produtoRepository;
	
	@Mock
	private ItemCarrinhoDTO itemCarrinhoDTO;
	
	@Captor
	private ArgumentCaptor<Carrinho> carrinhoCaptor;
	
	@Test
	@DisplayName("Deletar produto no carrinho por id token não deve lançar exceção quando carrinho existe e produto está no carrinho")
	void deletarProdutoNoCarrinhoPorIdToken_CarrinhoExisteEProdutoEstaNoCarrinho_NaoDeveLancarExcecao() {
		//ARRANGE
		Produto produto = new Produto(1L, null, null, null, null, null, null, null, null, null);
		Produto produto2 = new Produto(2L, null, null, null, null, null, null, null, null, null);
		List<ItemCarrinho> listaItemCarrinho = new ArrayList<>();
		ItemCarrinho itemCarrinho = new ItemCarrinho(1L, 1, produto, carrinho);
		ItemCarrinho itemCarrinho2 = new ItemCarrinho(2L, 3, produto2, carrinho);
		listaItemCarrinho.addAll(List.of(itemCarrinho, itemCarrinho2));
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(listaItemCarrinho);
		Long idProduto = 1L;
		
		//ACT
		carrinhoService.deletarProdutoNoCarrinhoPorIdToken(idProduto);
		
		//ASSERT
		BDDMockito.then(carrinhoRepository).should().save(carrinho);
		Assertions.assertEquals(listaItemCarrinho.size(), 1);
		Assertions.assertEquals(listaItemCarrinho.get(0).getId(), 2L);
	}
	
	@Test
	@DisplayName("Deletar produto no carrinho por id token deve lançar exceção quando carrinho não existe")
	void deletarProdutoNoCarrinhoPorIdToken_CarrinhoNaoExiste_DeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(null);
		Long idProduto = 1L;
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> carrinhoService.deletarProdutoNoCarrinhoPorIdToken(idProduto));
	}
	
	@Test
	@DisplayName("Deletar produto no carrinho por id token deve lançar exceção quando produto não está no carrinho")
	void deletarProdutoNoCarrinhoPorIdToken_ProdutoNaoEstaNoCarrinho_DeveLancarExcecao() {
		//ARRANGE
		Produto produto = new Produto(1L, null, null, null, null, null, null, null, null, null);
		Produto produto2 = new Produto(2L, null, null, null, null, null, null, null, null, null);
		List<ItemCarrinho> listaItemCarrinho = new ArrayList<>();
		ItemCarrinho itemCarrinho = new ItemCarrinho(1L, 1, produto, carrinho);
		ItemCarrinho itemCarrinho2 = new ItemCarrinho(2L, 3, produto2, carrinho);
		listaItemCarrinho.addAll(List.of(itemCarrinho, itemCarrinho2));
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(listaItemCarrinho);
		Long idProduto = 3L;
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> carrinhoService.deletarProdutoNoCarrinhoPorIdToken(idProduto));
	}
	
	@Test
	@DisplayName("Pegar carrinho cliente por id token não deve lançar exceção quando carrinho do cliente existe e tem produtos")
	void pegarCarrinhoClientePorIdToken_CarrinhoDoClienteExisteETemProdutos_NaoDeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		List<ArquivosProduto> listaArquivosProduto = new ArrayList<>();
		ArquivosProduto arquivosProduto = new ArquivosProduto(1L, "Padrao.jpeg", (byte) 0, null);
		listaArquivosProduto.add(arquivosProduto);
		List<Produto> listaProdutos = new ArrayList<>();
		Produto produto = new Produto(1L, null, null, BigDecimal.valueOf(200.00), null, null, null, listaArquivosProduto, null, null);
		Produto produto2 = new Produto(2L, null, null, BigDecimal.valueOf(100.00), null, null, null, listaArquivosProduto, null, null);
		listaProdutos.addAll(List.of(produto, produto2));
		List<ItemCarrinho> listaItemCarrinho = new ArrayList<>();
		ItemCarrinho itemCarrinho = new ItemCarrinho(1L, 1, produto, carrinho);
		ItemCarrinho itemCarrinho2 = new ItemCarrinho(2L, 3, produto2, carrinho);
		listaItemCarrinho.addAll(List.of(itemCarrinho, itemCarrinho2));
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(listaItemCarrinho);
		BDDMockito.when(produtoRepository.findAllById(any())).thenReturn(listaProdutos);
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO(null, null, null, null, null, String.valueOf("qualquer").getBytes());
		BDDMockito.when(fileService.pegarArquivoProdutoPorNome(any())).thenReturn(arquivoInfoDTO);
		
		//ACT
		MostrarCarrinhoClienteDTO mostrarCarrinhoClienteDTO = carrinhoService.pegarCarrinhoClientePorIdToken();
		
		//ASSERT
		Assertions.assertEquals(0, mostrarCarrinhoClienteDTO.id()); //O @Mock quando cria um mock, em atributos Long id ele cria como 0 e não como nulo
		Assertions.assertEquals(BigDecimal.valueOf(500.00), mostrarCarrinhoClienteDTO.valorTotal());
		Assertions.assertEquals(2, mostrarCarrinhoClienteDTO.itemCarrinho().size());
		Assertions.assertEquals(1L, mostrarCarrinhoClienteDTO.itemCarrinho().get(0).idProduto());
		Assertions.assertEquals(2L, mostrarCarrinhoClienteDTO.itemCarrinho().get(1).idProduto());
	}
	
	@Test
	@DisplayName("Pegar carrinho cliente por id token deve lançar exceção quando carrinho do cliente não existe")
	void pegarCarrinhoClientePorIdToken_CarrinhoDoClienteNaoExiste_DeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(null);
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> carrinhoService.pegarCarrinhoClientePorIdToken());
	}
	
	@Test
	@DisplayName("Pegar carrinho cliente por id token deve lançar exceção quando carrinho do cliente existe mas não tem produtos")
	void pegarCarrinhoClientePorIdToken_CarrinhoDoClienteExisteMasNaoTemProdutos_DeveLancarExcecao() {
		//ARRANGE
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(new ArrayList<ItemCarrinho>());
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> carrinhoService.pegarCarrinhoClientePorIdToken());
	}
	
	@Test
	@DisplayName("Adicionar produtos no carrinho por id token deve funcionar normalmente quando cliente tem carrinho, nao tem mesmo produto no carrinho e o produto é válido")
	void adicionarProdutosNoCarrinhoPorIdToken_ClienteTemCarrinhoENaoTemMesmoProdutoNoCarrinho_NaoDeveLancarExcecao() {
		//ARRANGE
		ItemCarrinhoDTO itemCarrinhoDTO = new ItemCarrinhoDTO(1L, 5);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(produtoService.verificarSeProdutoExistePorId(any())).thenReturn(produto);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(new ArrayList<ItemCarrinho>());
		
		//ACT
		carrinhoService.adicionarProdutosNoCarrinhoPorIdToken(itemCarrinhoDTO);
		
		//ASSERT
		Assertions.assertEquals(1, carrinho.getItemsCarrinho().size());
		Assertions.assertEquals(5, carrinho.getItemsCarrinho().get(0).getQuantidade());
		Assertions.assertEquals(produto, carrinho.getItemsCarrinho().get(0).getProduto());
		Assertions.assertEquals(carrinho, carrinho.getItemsCarrinho().get(0).getCarrinho());
		BDDMockito.then(carrinhoRepository).should().save(carrinho);
	}
	
	@Test
	@DisplayName("Adicionar produtos no carrinho por id token deve funcionar normalmente quando cliente tem carrinho, tem o mesmo produto no carrinho e produto é válido")
	void adicionarProdutosNoCarrinhoPorIdToken_ClienteTemCarrinhoETemMesmoProdutoNoCarrinho_NaoDeveLancarExcecao() {
		//ARRANGE
		ItemCarrinhoDTO itemCarrinhoDTO = new ItemCarrinhoDTO(1L, 5);
		List<ItemCarrinho> listaItemCarrinho = new ArrayList<>();
		ItemCarrinho itemCarrinho = new ItemCarrinho(1L, 2, produto, carrinho);
		listaItemCarrinho.add(itemCarrinho);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(produtoService.verificarSeProdutoExistePorId(any())).thenReturn(produto);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(listaItemCarrinho);
		BDDMockito.when(produto.getId()).thenReturn(1L);
		
		//ACT
		carrinhoService.adicionarProdutosNoCarrinhoPorIdToken(itemCarrinhoDTO);
		
		//ASSERT
		Assertions.assertEquals(1, carrinho.getItemsCarrinho().size());
		Assertions.assertEquals(7, carrinho.getItemsCarrinho().get(0).getQuantidade());
		Assertions.assertEquals(produto, carrinho.getItemsCarrinho().get(0).getProduto());
		BDDMockito.then(carrinhoRepository).should().save(carrinho);
	}
	
	@Test
	@DisplayName("Adicionar produtos no carrinho por id token deve funcionar normalmente quando cliente não tem carrinho e envia um produto válido, será criado um carrinho")
	void adicionarProdutosNoCarrinhoPorIdToken_ProdutoValidoEClienteNaoTemCarrinho_NaoDeveLancarExcecao() {
		//ARRANGE
		ItemCarrinhoDTO itemCarrinhoDTO = new ItemCarrinhoDTO(1L, 5);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(produtoService.verificarSeProdutoExistePorId(any())).thenReturn(produto);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(null);
		
		//ACT
		carrinhoService.adicionarProdutosNoCarrinhoPorIdToken(itemCarrinhoDTO);
		
		//ASSERT
		BDDMockito.then(carrinhoRepository).should().save(carrinhoCaptor.capture());
		Assertions.assertEquals(1, carrinhoCaptor.getValue().getItemsCarrinho().size());
		Assertions.assertEquals(5, carrinhoCaptor.getValue().getItemsCarrinho().get(0).getQuantidade());
		Assertions.assertEquals(produto, carrinhoCaptor.getValue().getItemsCarrinho().get(0).getProduto());
	}
	
	@Test
	@DisplayName("Setar quantidade produto no carrinho por id token deve funcionar normalmente quando cliente tem carrinho e envia dto valido")
	void setarQuantidadeProdutoNoCarrinhoPorIdToken_ClienteTemCarrinhoEDtoValido_NaoDeveLancarExecao() {
		//ARRANGE
		ItemCarrinhoDTO itemCarrinhoDTO = new ItemCarrinhoDTO(2L, 3);
		Produto produto = new Produto(1L, null, null, BigDecimal.valueOf(200.00), null, null, null, null, null, null);
		Produto produto2 = new Produto(2L, null, null, BigDecimal.valueOf(100.00), null, null, null, null, null, null);
		List<ItemCarrinho> listaItemCarrinho = new ArrayList<>();
		ItemCarrinho itemCarrinho = new ItemCarrinho(1L, 1, produto, carrinho);
		ItemCarrinho itemCarrinho2 = new ItemCarrinho(2L, 3, produto2, carrinho);
		listaItemCarrinho.addAll(List.of(itemCarrinho, itemCarrinho2));
		BDDMockito.when(produtoService.verificarSeProdutoExistePorId(any())).thenReturn(produto);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(listaItemCarrinho);
		
		//ACT
		carrinhoService.setarQuantidadeProdutoNoCarrinhoPorIdToken(itemCarrinhoDTO);
		
		//ASSERT
		BDDMockito.then(carrinhoRepository).should().save(carrinho);
		Assertions.assertEquals(listaItemCarrinho.get(1).getId(), 2L);
		Assertions.assertEquals(listaItemCarrinho.get(1).getQuantidade(), 3);
		Assertions.assertEquals(listaItemCarrinho.get(1).getProduto(), produto2);
		Assertions.assertEquals(listaItemCarrinho.get(1).getCarrinho(), carrinho);
	}
	
	@Test
	@DisplayName("Setar quantidade produto no carrinho por id token deve funcionar normalmente quando cliente tem carrinho e envia dto valido para remover")
	void setarQuantidadeProdutoNoCarrinhoPorIdToken_ClienteTemCarrinhoEDtoValidoParaRemover_NaoDeveLancarExecao() {
		//ARRANGE
		ItemCarrinhoDTO itemCarrinhoDTO = new ItemCarrinhoDTO(2L, 0);
		Produto produto = new Produto(1L, null, null, BigDecimal.valueOf(200.00), null, null, null, null, null, null);
		Produto produto2 = new Produto(2L, null, null, BigDecimal.valueOf(100.00), null, null, null, null, null, null);
		List<ItemCarrinho> listaItemCarrinho = new ArrayList<>();
		ItemCarrinho itemCarrinho = new ItemCarrinho(1L, 1, produto, carrinho);
		ItemCarrinho itemCarrinho2 = new ItemCarrinho(2L, 3, produto2, carrinho);
		listaItemCarrinho.addAll(List.of(itemCarrinho, itemCarrinho2));
		BDDMockito.when(produtoService.verificarSeProdutoExistePorId(any())).thenReturn(produto);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(carrinho);
		BDDMockito.when(carrinho.getItemsCarrinho()).thenReturn(listaItemCarrinho);
		
		//ACT
		carrinhoService.setarQuantidadeProdutoNoCarrinhoPorIdToken(itemCarrinhoDTO);
		
		//ASSERT
		BDDMockito.then(carrinhoRepository).should().save(carrinho);
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> listaItemCarrinho.get(1));
	}
	
	@Test
	@DisplayName("Setar quantidade produto no carrinho por id token deve lançar exceção quando cliente não tiver carrinho")
	void setarQuantidadeProdutoNoCarrinhoPorIdToken_ClienteNaoTemCarrinho_DeveLancarExecao() {
		//ARRANGE
		BDDMockito.when(produtoService.verificarSeProdutoExistePorId(any())).thenReturn(produto);
		BDDMockito.when(usuarioAutenticadoService.getCliente()).thenReturn(cliente);
		BDDMockito.when(cliente.getCarrinho()).thenReturn(null);
		
		//ASSERT + ACT
		Assertions.assertThrows(ValidacaoException.class, () -> carrinhoService.setarQuantidadeProdutoNoCarrinhoPorIdToken(itemCarrinhoDTO));
	}

}
