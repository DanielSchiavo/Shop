package br.com.danielschiavo.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.categoria.CriarCategoriaDTO;
import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {
	
	@Mock
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@InjectMocks
	private CategoriaService categoriaService;
	
	@Mock
	private Cliente cliente;
	
	@Mock
	private Categoria categoria;
	
	@Mock
	private CategoriaRepository categoriaRepository;
	
	@Captor
	private ArgumentCaptor<Categoria> categoriaCaptor;
	
    @Test
    @DisplayName("Listar categorias deve retornar pagina de categorias normalmente")
    void listarCategorias_DeveRetornarPaginaDeCategorias() {
        // ARRANGE
        Pageable pageable = PageRequest.of(0, 10);
        List<Categoria> listaCategorias = List.of(new Categoria(1L, "Categoria 1", null), new Categoria(2L, "Categoria 2", null));
        Page<Categoria> pageCategoria = new PageImpl<>(listaCategorias, pageable, listaCategorias.size());
        BDDMockito.when(categoriaRepository.findAll(pageable)).thenReturn(pageCategoria);

        // ACT
        Page<Categoria> retornoPageCategoriaListarCategorias = categoriaService.listarCategorias(pageable);

        // ASSERT
        assertNotNull(retornoPageCategoriaListarCategorias);
        assertEquals(pageCategoria.getTotalElements(), retornoPageCategoriaListarCategorias.getTotalElements());
        assertEquals(pageCategoria.getContent(), retornoPageCategoriaListarCategorias.getContent());
    }
    
    
//	------------------------------
//	------------------------------
//	METODOS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------
    
    @Test
    @DisplayName("Deletar categoria por id com o id de categoria fornecido existente deve executar normalmente")
    void deletarCategoriaPorId_IdFornecidoCategoriaExiste_NaoDeveLancarExcecao() {
    	//ARRANGE
    	BDDMockito.when(categoriaRepository.findById(any())).thenReturn(Optional.of(categoria));
    	Long idCategoria = 1L;
    	
    	//ACT
    	categoriaService.deletarCategoriaPorId(idCategoria);
    	
    	//ASSERT
    	BDDMockito.then(categoriaRepository).should().delete(categoria);
    }
    
    @Test
    @DisplayName("Deletar categoria por id com o id de categoria fornecido não existente deve lançar exceção")
    void deletarCategoriaPorId_IdFornecidoCategoriaNaoExiste_DeveLancarExcecao() {
    	//ARRANGE
    	BDDMockito.when(categoriaRepository.findById(any())).thenReturn(Optional.empty());
    	
    	//ASSERT + ACT
    	Long idCategoria = 1L;
    	Assertions.assertThrows(ValidacaoException.class, () -> categoriaService.deletarCategoriaPorId(idCategoria));
    }
    
    @Test
    @DisplayName("Criar categoria deve funcionar normalmente quando nome de categoria ainda não foi cadastrado")
    void criarCategoria_NomeAindaNaoFoiCadastrado_NaoDeveLancarExcecao() {
    	//ARRANGE
    	BDDMockito.when(categoriaRepository.findByNome(any())).thenReturn(Optional.empty());
    	String nomeCategoriaASerCriada = "Computador";
    	
    	//ACT
    	MostrarCategoriaDTO mostrarCategoriaDTO = categoriaService.criarCategoria(nomeCategoriaASerCriada);
    	
    	//ASSERT
    	BDDMockito.then(categoriaRepository).should().save(categoriaCaptor.capture());
    	Assertions.assertEquals(nomeCategoriaASerCriada, mostrarCategoriaDTO.nome());
    }
    
    @Test
    @DisplayName("Criar categoria deve lançar exceção quando nome de categoria ja foi cadastrado")
    void criarCategoria_NomeJaFoiCadastrado_DeveLancarExcecao() {
    	//ARRANGE
    	BDDMockito.when(categoriaRepository.findByNome(any())).thenReturn(Optional.of(categoria));
    	String nomeCategoriaASerCriada = "Computador";
    	
    	//ASSERT + ACT
    	Assertions.assertThrows(ValidacaoException.class, () -> categoriaService.criarCategoria(nomeCategoriaASerCriada));
    }
    
    @Test
    @DisplayName("Alterar nome categoria por id deve executar normalmente quando id categoria a ser alterada existir e nome dto não já existir")
    void alterarNomeCategoriaPorId_IdCategoriaExisteENomeDtoOk_NaoDeveLancarExcecao() {
    	//ARRANGE
    	Categoria categoria = new Categoria (1L, "Ferramentas", null);
    	BDDMockito.when(categoriaRepository.findById(any())).thenReturn(Optional.of(categoria)); //verificarSeExisteCategoriaPorId
    	BDDMockito.when(categoriaRepository.findByNome(any())).thenReturn(Optional.empty());
    	CriarCategoriaDTO criarCategoriaDTO = new CriarCategoriaDTO("Computadores");
    	Long idCategoriaASerAlterada = 1L;
    	
    	//ACT
    	MostrarCategoriaDTO mostrarCategoriaDTO = categoriaService.alterarNomeCategoriaPorId(idCategoriaASerAlterada, criarCategoriaDTO);
    	
    	//ASSERT
    	Assertions.assertEquals(criarCategoriaDTO.nome(), mostrarCategoriaDTO.nome());
    }
    
    @Test
    @DisplayName("Alterar nome categoria por id deve lançar exceção quando id categoria a ser alterada for ok e nome dto (nome novo da categoria) já existir")
    void alterarNomeCategoriaPorId_IdCategoriaExisteENomeDtoJaExiste_DeveLancarExcecao() {
    	//ARRANGE
    	Categoria categoria = new Categoria (1L, "Ferramentas", null);
    	BDDMockito.when(categoriaRepository.findById(any())).thenReturn(Optional.of(categoria)); //verificarSeExisteCategoriaPorId
    	BDDMockito.when(categoriaRepository.findByNome(any())).thenReturn(Optional.of(this.categoria));
    	CriarCategoriaDTO criarCategoriaDTO = new CriarCategoriaDTO("Computadores");
    	Long idCategoriaASerAlterada = 1L;
    	
    	//ASSERT + ACT
    	Assertions.assertThrows(ValidacaoException.class, () -> categoriaService.alterarNomeCategoriaPorId(idCategoriaASerAlterada, criarCategoriaDTO));
    }
    
    @Test
    @DisplayName("Alterar nome categoria por id deve lançar exceção quando id categoria não existir e nome dto (nome novo da categoria) estiver ok")
    void alterarNomeCategoriaPorId_IdCategoriaNaoExisteENomeDtoOk_DeveLancarExcecao() {
    	//ARRANGE
    	BDDMockito.when(categoriaRepository.findById(any())).thenReturn(Optional.empty()); //verificarSeExisteCategoriaPorId
    	CriarCategoriaDTO criarCategoriaDTO = new CriarCategoriaDTO("Computadores");
    	Long idCategoriaASerAlterada = 1L;
    	
    	//ASSERT + ACT
    	Assertions.assertThrows(ValidacaoException.class, () -> categoriaService.alterarNomeCategoriaPorId(idCategoriaASerAlterada, criarCategoriaDTO));
    }
}
