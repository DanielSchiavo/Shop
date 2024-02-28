package br.com.danielschiavo.shop.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.danielschiavo.shop.models.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.produto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.AtualizarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.MostrarArquivosProdutoDTO;
import br.com.danielschiavo.shop.models.produto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.ProdutoDTO;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.repositories.ItemCarrinhoRepository;
import br.com.danielschiavo.shop.repositories.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FilesStorageService fileService;
	
	@Autowired
	private SubCategoriaService subCategoriaService;
	
	private ItemCarrinhoRepository itemCarrinhoRepository;
	
	private final int MAX_FILES = 10;

	public void salvar(Produto product) {
		produtoRepository.save(product);
	}

	// public Page<ShowProductsDTO> findAllByActiveTrue(Pageable pageable) {
//		return repository.findAllByActiveTrue(pageable).map(ShowProductsDTO::new);
//	}
	
	public byte[] pegarPrimeiraImagemProduto(List<ArquivosProduto> listaArquivosProduto) {
	    for (ArquivosProduto arquivosProduto : listaArquivosProduto) {
	        if (arquivosProduto.getPosicao() == 0) {
				return fileService.pegarBytesArquivoProduto(arquivosProduto.getNome());
	        }
	    }
		return null;
	}

	public void deletarProdutoPorId(Long id) {
		produtoRepository.deleteById(id);
		List<Optional<ItemCarrinho>> optionalItemCarrinho = itemCarrinhoRepository.findAllByProdutoId(id);
		
		if (!optionalItemCarrinho.isEmpty()) {
			optionalItemCarrinho.forEach(item -> {
				itemCarrinhoRepository.delete(item.get());
			});
		}
	}

	public MostrarProdutosDTO criarNovoProduto(Produto product, MultipartFile[] files, int[] position) throws IOException {
		Produto productFlush = produtoRepository.saveAndFlush(product);

		List<String> productFileName = fileService.pegarNomeArquivoProduto(files, position, productFlush.getId());
		fileService.salvarNoDiscoArquivosProduto(files, productFileName);

		productFlush.setArquivosProduto(productFileName, position);

		produtoRepository.save(productFlush);

		byte[] productFirstImage = fileService.pegarBytesArquivoProduto(productFileName.get(0)); // ESTÁ ERRADO!
		return new MostrarProdutosDTO(product, productFirstImage);
	}

	public void atualizarArquivos(MultipartFile[] arquivos, int[] posicoes, Produto product) {
		List<ArquivosProduto> productFiles = product.getArquivosProduto();
		Iterator<ArquivosProduto> iterator = productFiles.iterator();
		while (iterator.hasNext()) {
		    ArquivosProduto arquivoProduto = iterator.next();
		    fileService.deletarArquivoProdutoNoDisco(arquivoProduto.getNome());
		    iterator.remove();
		}
		
		List<String> nomeArquivoProduto = fileService.pegarNomeArquivoProduto(arquivos, posicoes, product.getId());
		for (int i=0; i<posicoes.length; i++) {
			if (!arquivos[i].getContentType().startsWith("image") && posicoes[i] == 0) {
				throw new RuntimeException("O primeiro arquivo tem que ser uma imagem para exibição do produto! ");
			}
			int a = i;
			Optional<ArquivosProduto> optionalArquivoProduto = product.getArquivosProduto().stream()
					.filter(arquivoProduto -> arquivoProduto.getPosicao() == posicoes[a]).findFirst();
			if (optionalArquivoProduto.isPresent()) {
				fileService.deletarArquivoProdutoNoDisco(optionalArquivoProduto.get().getNome());
				fileService.salvarNoDiscoArquivoProduto(arquivos[i], nomeArquivoProduto.get(i));
				optionalArquivoProduto.get().setNome(nomeArquivoProduto.get(i));
				continue;
			}
			fileService.salvarNoDiscoArquivoProduto(arquivos[i], nomeArquivoProduto.get(i));
			product.adicionarArquivosProduto(nomeArquivoProduto.get(i), posicoes[i]);
		}
		produtoRepository.save(product);
	}

	public List<MostrarArquivosProdutoDTO> carregarArquivosProduto(List<ArquivosProduto> arquivosProduto) throws IOException {
		List<MostrarArquivosProdutoDTO> mostrarArquivosProdutoDTO = new ArrayList<>();
		
		arquivosProduto.forEach(arquivo -> {			
				String nome = arquivo.getNome();
				byte[] arquivoProduto = null;
				arquivoProduto = fileService.pegarBytesArquivoProduto(nome);
				String tipo = null;
				if (nome.endsWith("jpg") || nome.endsWith("jpeg")) {
					tipo = "image";
				}
				if (nome.endsWith("mp4") || nome.endsWith("avi")) {
					tipo = "video";
				}
				mostrarArquivosProdutoDTO.add(new MostrarArquivosProdutoDTO(tipo, arquivoProduto.length, arquivo.getPosicao(), arquivoProduto));
			
		});
		
		return mostrarArquivosProdutoDTO;
	}

	public Page<MostrarProdutosDTO> listarProdutos(Pageable pageable) {
		Page<Produto> pageProdutos = produtoRepository.findAll(pageable);
		List<MostrarProdutosDTO> list = new ArrayList<>();
		
		for (Produto produto : pageProdutos) {
			byte[] imagem = pegarPrimeiraImagemProduto(produto.getArquivosProduto());
			MostrarProdutosDTO mostrarProdutos = new MostrarProdutosDTO(produto, imagem);
			list.add(mostrarProdutos);
		    }
		return new PageImpl<>(list, pageProdutos.getPageable(), pageProdutos.getTotalElements());
	}
	
	public List<MostrarProdutosDTO> adicionarPrimeiraImagemEmMostrarProdutosDTO(List<Produto> produtos) throws IOException {
		List<MostrarProdutosDTO> listaMostrarProdutosDTO = new ArrayList<>();
		for (Produto produto : produtos) {
			for (ArquivosProduto arquivo : produto.getArquivosProduto()) {
				if (arquivo.getPosicao() == 0) {
					byte[] bytesPrimeiraImagem = fileService.pegarBytesArquivoProduto(arquivo.getNome());
					MostrarProdutosDTO mostrarProdutosDTO = new MostrarProdutosDTO(produto, bytesPrimeiraImagem);
					listaMostrarProdutosDTO.add(mostrarProdutosDTO);
				}
			}
		}
		return listaMostrarProdutosDTO;
	}

	public ProdutoDTO transformarStringJsonEmProdutoDTO(String produto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ProdutoDTO produtoDTO = objectMapper.readValue(produto, ProdutoDTO.class);
			return produtoDTO;
		} catch (IOException err) {
			System.out.println("Error " + err.toString());
		}
		return null;

	}
	public AtualizarProdutoDTO transformarStringJsonParaAtualizarProdutoDTO(String produto) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			AtualizarProdutoDTO atualizarProdutoDTO = objectMapper.readValue(produto, AtualizarProdutoDTO.class);
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

	public Produto verificarId(Long id) {
		Optional<Produto> productOptional = produtoRepository.findById(id);
		if (productOptional.isPresent()) {
			Produto product = productOptional.get();
			return product;
		} else {
			throw new RuntimeException("ID do produto inexistente! ");
		}
	}

	public void atualizarDadosProduto(Produto produto, String produtoJson, MultipartFile[] arquivos, String posicoesString) {
		if (produtoJson != null) {
			AtualizarProdutoDTO atualizarProdutoDTO = transformarStringJsonParaAtualizarProdutoDTO(produtoJson);
			
			if (atualizarProdutoDTO.subCategoriaId() != null) {
				SubCategoria subCategory = subCategoriaService.verificarId(atualizarProdutoDTO.subCategoriaId());
				produto.setSub_categoria(subCategory);
			}
			
			produto.atualizarAtributos(atualizarProdutoDTO);
		}
		
		if (arquivos != null) {
			int[] position = transformarStringPosicaoEmArrayInt(posicoesString);
			atualizarArquivos(arquivos, position, produto);
		}
	}

	public MostrarProdutosDTO cadastrarProduto(String jsonProduto, MultipartFile[] multipartArquivos, String stringPosicoes) {
		ProdutoDTO produtoDTO = transformarStringJsonEmProdutoDTO(jsonProduto);
		int[] posicoes = transformarStringPosicaoEmArrayInt(stringPosicoes);
		
		if (multipartArquivos.length != posicoes.length) {
			ResponseEntity.badRequest().body("Cada FILE deve corresponder a uma POSITION!");
		}
		
		if (posicoes[0] != 0) {
			throw new RuntimeException("O produto precisa ter uma imagem principal! que corresponde a POSITION: 0");
		}
		
		if (multipartArquivos.length > MAX_FILES && posicoes.length > MAX_FILES) {
			throw new RuntimeException("O número máximo de imagens e vídeos é " + MAX_FILES + "!");
		}
		
		SubCategoria subCategoria = subCategoriaService.verificarId(produtoDTO.subCategoriaId());
		
		Produto produto = new Produto(produtoDTO, subCategoria);
		MostrarProdutosDTO mostrarProdutosDTO = null;
		try {
			mostrarProdutosDTO = criarNovoProduto(produto, multipartArquivos, posicoes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mostrarProdutosDTO;
	}

	public Produto alterarProdutoPorId(Long id, String jsonProduto, MultipartFile[] multipartArquivos, String stringPosicoes) {
		Produto produto = produtoRepository.getReferenceById(id);
		int[] posicoes = transformarStringPosicaoEmArrayInt(stringPosicoes);

		if (jsonProduto == null && multipartArquivos == null && posicoes == null) {
			ResponseEntity.badRequest().body("Nenhum dado foi enviado com a requisição, portanto nada foi alterado.");
		}
		
		if (multipartArquivos.length != posicoes.length) {
			ResponseEntity.badRequest().body("Cada ARQUIVO deve corresponder a uma POSICAO, para saber a ordem de exibicao do arquivo.");
		}
		
		atualizarDadosProduto(produto, jsonProduto, multipartArquivos, stringPosicoes);
		
		return produto;
	}

	public DetalharProdutoDTO detalharProdutoPorId(Long id) {
		Produto produto = produtoRepository.getReferenceById(id);
		List<MostrarArquivosProdutoDTO> mostrarArquivosProdutoDTO;
		DetalharProdutoDTO detalharProdutoDTO = null;
		try {
			mostrarArquivosProdutoDTO = carregarArquivosProduto(produto.getArquivosProduto());
			detalharProdutoDTO = new DetalharProdutoDTO(produto, mostrarArquivosProdutoDTO);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return detalharProdutoDTO;
	}
}
