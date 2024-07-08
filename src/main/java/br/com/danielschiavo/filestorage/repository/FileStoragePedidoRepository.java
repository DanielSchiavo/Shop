package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileStoragePedidoRepository extends FileStorageRepository {

    @Autowired
    @Qualifier("fileStorageRepository")
    private FileStorageRepository repository;

    private final Path raizPedido = Paths.get("imagens/pedido");

    public ArquivoInfoDTO pegarImagemPorNome(String nomeArquivo) {
        return (ArquivoInfoDTO) repository.pegar(raizPedido, nomeArquivo);
    }

    public Optional<String> verificarSeExisteImagemPedidoNoDisco(String nomeImagem) {
        return repository.verificarSeExisteNoDisco(raizPedido, nomeImagem, "*", "");
    }

    public void salvar(String nomeImagem, byte[] bytesImagem) {
        repository.salvar(raizPedido, nomeImagem, bytesImagem);
    }
}



