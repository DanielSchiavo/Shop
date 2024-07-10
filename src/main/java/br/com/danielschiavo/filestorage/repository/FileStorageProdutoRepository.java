package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.model.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageProdutoRepository extends FileStorageRepository {

    @Autowired
    @Qualifier("fileStorageRepository")
    private FileStorageRepository repository;

    public static final Path raizProduto = Paths.get("imagens/produto");

    public List<File> pegarImagensPorListaDeNomes(List<String> nomeImagens) {
        List<File> files = new ArrayList<>();
        nomeImagens.forEach(nome -> files.add(pegarImagemPorNome(nome)));
        return files;
    }

    public File pegarImagemPorNome(String nomeImagem) {
        byte[] content = repository.pegar(raizProduto, nomeImagem);
        return new File(nomeImagem, content);
    }

    public void deletarTodasImagensPorNome(List<String> nomeImagens) {
        nomeImagens.forEach(this::deletarImagemPorNome);
    }

    public void deletarImagemPorNome(String nomeImagem) {
        repository.deletar(raizProduto, nomeImagem);
    }

    public List<File> salvarTodos(List<File> arquivos) {
        List<File> files = new ArrayList<>();
        arquivos.forEach(arq -> files.add(salvar(arq)));
        return files;
    }

    public File salvar(File file) {
        repository.salvar(raizProduto, file.getFileName(), file.getContent());
        return file;
    }

    public boolean verificarSeImagensExistem(List<String> nomesImagens) {
        return repository.verificarSeImagensExistem(raizProduto, nomesImagens);
    }

    public boolean verificarSeImagemExiste(String nomeImagem) {
        return repository.verificarSeImagemExiste(raizProduto, nomeImagem);
    }
}
