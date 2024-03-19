package br.com.danielschiavo.shop.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

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

	public List<ArquivoInfoDTO> carregarArquivosProduto(List<ArquivosProduto> arquivosProduto) {
		List<String> listaDeNomes = arquivosProduto.stream().map(ap -> ap.getNome()).collect(Collectors.toList());
		List<ArquivoInfoDTO> listaArquivoInfoDTO = fileService.mostrarArquivoProdutoPorListaDeNomes(listaDeNomes);
		return listaArquivoInfoDTO;
	}

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

	public List<MostrarProdutosDTO> adicionarPrimeiraImagemEmMostrarProdutosDTO(List<Produto> produtos)
			throws IOException {
		List<MostrarProdutosDTO> listaMostrarProdutosDTO = new ArrayList<>();
		for (Produto produto : produtos) {
			for (ArquivosProduto arquivo : produto.getArquivosProduto()) {
				if (arquivo.getPosicao() == 0) {
					ArquivoInfoDTO arquivoInfoDTO = fileService.pegarArquivoProdutoPorNome(arquivo.getNome());
					MostrarProdutosDTO mostrarProdutosDTO = new MostrarProdutosDTO(produto, arquivoInfoDTO.bytesArquivo());
					listaMostrarProdutosDTO.add(mostrarProdutosDTO);
				}
			}
		}
		return listaMostrarProdutosDTO;
	}

	public CadastrarProdutoDTO transformarStringJsonEmProdutoDTO(String produto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			CadastrarProdutoDTO produtoDTO = objectMapper.readValue(produto, CadastrarProdutoDTO.class);
			return produtoDTO;
		} catch (IOException err) {
			System.out.println("Error " + err.toString());
		}
		return null;

	}

	public AlterarProdutoDTO transformarStringJsonParaAtualizarProdutoDTO(String produto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			AlterarProdutoDTO atualizarProdutoDTO = objectMapper.readValue(produto, AlterarProdutoDTO.class);
			return atualizarProdutoDTO;
		} catch (IOException err) {
			System.out.println("Error " + err.toString());
		}
		return null;

	}

	public int[] transformarStringPosicaoEmArrayInt(String posicao) {
		String[] values = posicao.split(",");
		return Stream.of(values).mapToInt(Integer::valueOf).toArray();
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

	public DetalharProdutoDTO detalharProdutoPorId(Long id) {
		Produto produto = produtoRepository.getReferenceById(id);
		List<ArquivoInfoDTO> mostrarArquivosProdutoDTO = carregarArquivosProduto(produto.getArquivosProduto());
		DetalharProdutoDTO detalharProdutoDTO = new DetalharProdutoDTO(produto, mostrarArquivosProdutoDTO);
		return detalharProdutoDTO;
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
