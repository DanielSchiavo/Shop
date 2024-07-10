package br.com.danielschiavo.filestorage.service;

import java.util.Optional;

import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.repository.FileStoragePedidoRepository;
import br.com.danielschiavo.filestorage.repository.FileStorageProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FileStoragePedidoService {
	
	@Autowired
	private FileStoragePedidoRepository repository;

	@Autowired
	private FileStorageProdutoRepository produtoRepository;
	
	public File pegarImagemPedidoPorNome(String nomeArquivo) {
		return repository.pegarImagemPorNome(nomeArquivo);
	}
	
	public File handleImagemPedido(String nomeImagem, Long idProduto) {
		Optional<byte[]> optional = repository.verificarSeExisteImagemPedidoNoDisco(nomeImagem);
		if (optional.isPresent()) {
			return new File(nomeImagem, optional.get());
		}
		else {
			String novoNome = gerarNomeImagemPedido(idProduto, nomeImagem);
			File file = produtoRepository.pegarImagemPorNome(nomeImagem);
			return repository.salvar(novoNome, file.getContent());
		}
	}
	
//
// METODOS UTILITARIOS DE PEDIDO
//	

	private String gerarNomeImagemPedido(Long idProduto, String nomePrimeiraImagemProduto) {
        return "PRODID" + idProduto + "-" + nomePrimeiraImagemProduto;
	}

	

}
