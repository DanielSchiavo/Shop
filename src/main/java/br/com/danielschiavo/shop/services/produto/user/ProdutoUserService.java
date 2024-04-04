package br.com.danielschiavo.shop.services.produto.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.mapper.produto.ProdutoMapper;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.dto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.dto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.repositories.produto.ProdutoRepository;
import br.com.danielschiavo.shop.services.filestorage.FileStorageProdutoService;
import br.com.danielschiavo.shop.services.produto.CategoriaUtilidadeService;
import br.com.danielschiavo.shop.services.produto.ProdutoUtilidadeService;
import lombok.Setter;

@Service
@Setter
public class ProdutoUserService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FileStorageProdutoService fileStorageProdutoService;

	@Autowired
	private ProdutoMapper produtoMapper;
	
	@Autowired
	private ProdutoUtilidadeService produtoUtilidadeService;
	
	@Autowired
	private CategoriaUtilidadeService categoriaUtilidadeService;
	
	public Page<MostrarProdutosDTO> listarProdutos(Pageable pageable) {
		Page<Produto> pageProdutos = produtoRepository.findAll(pageable);
		return produtoMapper.pageProdutosParaPageMostrarProdutosDTO(pageProdutos, fileStorageProdutoService, produtoUtilidadeService, produtoMapper, categoriaUtilidadeService);
	}
	
	public DetalharProdutoDTO detalharProdutoPorId(Long id) {
		Produto produto = produtoUtilidadeService.verificarSeProdutoExistePorId(id);
		return produtoMapper.produtoParaDetalharProdutoDTO(produto, fileStorageProdutoService, produtoUtilidadeService, categoriaUtilidadeService);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------
	
}
