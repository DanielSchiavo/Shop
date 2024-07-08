package br.com.danielschiavo.produto.service.user;

import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.produto.model.DetalharProdutoResponse;
import br.com.danielschiavo.produto.model.MostrarProdutosResponse;
import br.com.danielschiavo.produto.model.Produto;
import br.com.danielschiavo.produto.repository.user.ProdutoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
@Setter
public class ProdutoUserService {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ProdutoComumMapper produtoMapper;

	@Autowired
	private FileStorageProdutoService fileStorageService;
	
	public Page<MostrarProdutosResponse> listarProdutos(Pageable pageable) {
		Page<Produto> pageProdutos = produtoRepository.findAll(pageable);
		
	    List<MostrarProdutosResponse> listaMostrarProdutosDTO = pageProdutos.getContent().stream()
	            .map(produto -> produtoMapper.produtoParaMostrarProdutosDTO(produto, pegarNomePrimeiraImagem(produto)))
	            .collect(Collectors.toList());
	    
	    return new PageImpl<>(listaMostrarProdutosDTO, pageable, pageProdutos.getTotalElements());
	}
	
	public DetalharProdutoResponse detalharProdutoPorId(Long id) {
		Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ValidacaoException("Não existe um produto com o id " + id));

		return produtoMapper.produtoParaDetalharProdutoDTO(produto, fileStorageService);
	}


//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	public String pegarNomePrimeiraImagem(Produto produto) {
		return produto.getArquivosProduto().stream().filter(ap -> ap.getPosicao() == (byte) 0).findFirst().get().getNome();
	}

	public String pegarNomePrimeiraImagem(List<ArquivoInfoDTO> arquivosProduto) {
		return arquivosProduto.stream().filter(ap -> ap.posicao() == (byte) 0).findFirst().get().nomeArquivo();
	}

}
