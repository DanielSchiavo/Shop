package br.com.danielschiavo.filestorage.service;

import java.util.Optional;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
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
	
	public ArquivoInfoDTO pegarImagemPedidoPorNome(String nomeArquivo) {
		return repository.pegarImagemPorNome(nomeArquivo);
	}
	
	public String persistirOuRecuperarImagemPedido(String nomeImagem, Long idProduto) {
		Optional<String> optionalString = repository.verificarSeExisteImagemPedidoNoDisco(nomeImagem);
		if (optionalString.isPresent()) {
			return nomeImagem;
		}
		else {
			String novoNome = gerarNomeImagemPedido(idProduto, nomeImagem);
			ArquivoInfoDTO arquivoInfoDTO = produtoRepository.pegarImagemPorNome(novoNome);
			repository.salvar(novoNome, arquivoInfoDTO.bytesArquivo());
			return novoNome;
		}
	}
	
//
// METODOS UTILITARIOS DE PEDIDO
//	

	private String gerarNomeImagemPedido(Long idProduto, String nomePrimeiraImagemProduto) {
        return "PRODID" + idProduto + "-" + nomePrimeiraImagemProduto;
	}

	

}
