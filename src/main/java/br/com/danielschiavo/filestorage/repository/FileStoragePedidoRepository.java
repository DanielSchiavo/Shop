package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.model.File;
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

    private final Path raizPedido = Paths.get("imagens/order");

    public File pegarImagemPorNome(String nomeArquivo) {
        byte[] content = repository.pegar(raizPedido, nomeArquivo);
        return new File(nomeArquivo, content);
    }

    public Optional<byte[]> verificarSeExisteImagemPedidoNoDisco(String nomeImagem) {
        return repository.verificarSeExisteNoDisco(raizPedido, nomeImagem, "*", "");
    }

    public File salvar(String nomeImagem, byte[] bytesImagem) {
        repository.salvar(raizPedido, nomeImagem, bytesImagem);
        return new File(nomeImagem, bytesImagem);
    }
}



