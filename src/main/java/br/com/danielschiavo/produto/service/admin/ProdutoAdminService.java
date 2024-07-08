package br.com.danielschiavo.produto.service.admin;

import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.filestorage.service.FileStorageProdutoService;
import br.com.danielschiavo.produto.model.AlterarProdutoRequest;
import br.com.danielschiavo.produto.model.CadastrarProdutoRequest;
import br.com.danielschiavo.produto.model.Produto;
import br.com.danielschiavo.produto.repository.admin.ProdutoRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shared.infra.security.SecurityService;
import br.com.danielschiavo.produto.service.admin.validacoes.ValidadorCadastrarNovoProduto;
import lombok.Setter;

@Service
public class ProdutoAdminService {
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private FileStorageProdutoService fileStorageProdutoService;

	@Autowired
	private List<ValidadorCadastrarNovoProduto> validador;
	
	@Setter
	@Autowired
	private ProdutoMapper produtoMapper;
	
	@Transactional
	public void deletarProdutoPorId(Long id) {
		Produto produto = pegarProdutoPorId(id);
		List<String> nomeTodosArquivos = pegarNomeTodosArquivos(produto);
		
		fileStorageProdutoService.deletarImagens(nomeTodosArquivos);

		produtoRepository.delete(produto);
	}
	
	@Transactional
	public Produto cadastrarProduto(CadastrarProdutoRequest cadastrarProdutoDTO) {
		validador.forEach(v -> v.validar(cadastrarProdutoDTO));
		
		Produto produto = produtoMapper.cadastrarProdutoDtoParaProduto(cadastrarProdutoDTO);
		produtoRepository.save(produto);
		
		return produto;
	}

	@Transactional
	public Produto alterarProdutoPorId(Long id, AlterarProdutoRequest request) {
		Produto produto = pegarProdutoPorId(id);
		produtoMapper.alterarProdutoDtoParaProduto(request, produto);
		produtoRepository.save(produto);

		return produto;
	}

	public Produto pegarProdutoPorId(Long id) {
		return produtoRepository.findById(id).orElseThrow(() -> new ValidacaoException("Não existe produto com o id " + id));
	}

	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	public List<String> pegarNomeTodosArquivos(Produto produto) {
		return produto.getArquivosProduto().stream().map(ap -> ap.getNome()).collect(Collectors.toList());
	}
	
}
