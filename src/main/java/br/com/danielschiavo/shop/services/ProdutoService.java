package br.com.danielschiavo.shop.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.produto.AlterarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.validacoes.ValidadorCadastrarNovoProduto;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.repositories.CarrinhoRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FileStorageService fileService;

	@Autowired
	private SubCategoriaService subCategoriaService;

	@Autowired
	private CategoriaService categoriaService;

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	@Autowired
	private List<ValidadorCadastrarNovoProduto> validador;
	
	public Page<MostrarProdutosDTO> listarProdutos(Pageable pageable) {
		Page<Produto> pageProdutos = produtoRepository.findAll(pageable);
		List<MostrarProdutosDTO> list = new ArrayList<>();

		for (Produto produto : pageProdutos) {
			String nomePrimeiraImagem = produto.pegarNomePrimeiraImagem();
			ArquivoInfoDTO arquivoInfoDTO = fileService.pegarArquivoProdutoPorNome(nomePrimeiraImagem);
			MostrarProdutosDTO mostrarProdutos = new MostrarProdutosDTO(produto, arquivoInfoDTO.bytesArquivo());
			list.add(mostrarProdutos);
		}
		return new PageImpl<>(list, pageProdutos.getPageable(), pageProdutos.getTotalElements());
	}
	
	public DetalharProdutoDTO detalharProdutoPorId(Long id) {
		Produto produto = produtoRepository.getReferenceById(id);
		List<ArquivoInfoDTO> mostrarArquivosProdutoDTO = carregarArquivosProduto(produto.getArquivosProduto());
		DetalharProdutoDTO detalharProdutoDTO = new DetalharProdutoDTO(produto, mostrarArquivosProdutoDTO);
		return detalharProdutoDTO;
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------

	@Transactional
	public void deletarProdutoPorId(Long id) {
		produtoRepository.deleteById(id);

		Optional<List<Carrinho>> optionalCarrinho = carrinhoRepository.findCarrinhosByProdutoId(id);

		if (optionalCarrinho.isPresent()) {
			optionalCarrinho.get().forEach(carrinho -> {
				carrinho.getItemsCarrinho().removeIf(item -> item.getProduto().getId().equals(id));
				carrinhoRepository.save(carrinho);
			});
		}
	}
	
	@Transactional
	public MostrarProdutosDTO cadastrarProduto(CadastrarProdutoDTO cadastrarProdutoDTO) {
		validador.forEach(v -> v.validar(cadastrarProdutoDTO));
		
		SubCategoria subCategoria = subCategoriaService.verificarSeExisteSubCategoriaPorId(cadastrarProdutoDTO.idSubCategoria());
		Produto produto = new Produto(cadastrarProdutoDTO, subCategoria);

		String nomePrimeiraImagem = produto.pegarNomePrimeiraImagem();
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarArquivoProdutoPorNome(nomePrimeiraImagem);

		produtoRepository.save(produto);
		
		return new MostrarProdutosDTO(produto, arquivoInfoDTO.bytesArquivo());
	}
	
	@Transactional
	public DetalharProdutoDTO alterarProdutoPorId(Long id, AlterarProdutoDTO alterarProdutoDTO) {
		Produto produto = verificarSeProdutoExistePorId(id);
		
		alterarProdutoDTO.arquivos().forEach(arquivo -> {
			fileService.verificarSeExisteArquivoProdutoPorNome(arquivo.nome());
		});

		if (alterarProdutoDTO.idSubCategoria() != null) {
			SubCategoria subCategory = subCategoriaService
					.verificarSeExisteSubCategoriaPorId(alterarProdutoDTO.idSubCategoria());
			produto.setSubCategoria(subCategory);
		}
		if (alterarProdutoDTO.idCategoria() != null) {
			Categoria subCategory = categoriaService
					.verificarSeExisteCategoriaPorId(alterarProdutoDTO.idCategoria());
			produto.setCategoria(subCategory);
		}

		produto.alterarAtributos(alterarProdutoDTO, produto);
		
		produtoRepository.save(produto);

		return new DetalharProdutoDTO(produto, carregarArquivosProduto(produto.getArquivosProduto()));
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	private List<ArquivoInfoDTO> carregarArquivosProduto(List<ArquivosProduto> arquivosProduto) {
		List<String> listaDeNomes = arquivosProduto.stream().map(ap -> ap.getNome()).collect(Collectors.toList());
		List<ArquivoInfoDTO> listaArquivoInfoDTO = fileService.mostrarArquivoProdutoPorListaDeNomes(listaDeNomes);
		return listaArquivoInfoDTO;
	}

	public Produto verificarSeProdutoExistePorIdEAtivoTrue(Long id) {
		Optional<Produto> optionalProduto = produtoRepository.findByIdAndAtivoTrue(id);
		if (optionalProduto.isPresent()) {
			return optionalProduto.get();
		} else {
			throw new ValidacaoException("Não existe um produto ativo com o id " + id);
		}
	}
	
	public Produto verificarSeProdutoExistePorId(Long id) {
		Optional<Produto> optionalProduto = produtoRepository.findById(id);
		if (optionalProduto.isPresent()) {
			return optionalProduto.get();
		} else {
			throw new ValidacaoException("Não existe um produto com o id " + id);
		}
	}
}
