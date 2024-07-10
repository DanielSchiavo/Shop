package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStoragePerfilRepository extends FileStorageRepository{

    @Autowired
    @Qualifier("fileStorageRepository")
    private FileStorageRepository repository;

    private final Path raizPerfil = Paths.get("imagens/perfil");

    public void deletarPorNome(String nomeImagem) {
        repository.deletar(raizPerfil, nomeImagem);
    }

    public File pegarFotoPerfilPorNome(String nomeImagem) {
        byte[] content = repository.pegar(raizPerfil, nomeImagem);
        return new File(nomeImagem, content);
    }

    public File salvar(String nomeImagem, byte[] bytesImagem) {
        repository.salvar(raizPerfil, nomeImagem, bytesImagem);
        return new File(nomeImagem, bytesImagem);
    }
}
