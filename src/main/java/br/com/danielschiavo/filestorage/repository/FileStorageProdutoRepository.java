package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileStorageProdutoRepository extends FileStorageRepository {

    @Autowired
    @Qualifier("fileStorageRepository")
    private FileStorageRepository repository;

    public static final Path raizProduto = Paths.get("imagens/produto");

    public ArquivoInfoDTO pegarImagemPorNome(String nomeImagem) {
        return (ArquivoInfoDTO) repository.pegar(raizProduto, nomeImagem);
    }

    @SuppressWarnings("unchecked")
    public List<ArquivoInfoDTO> pegarImagensPorNome(List<String> nomeImagens) {
        return (List<ArquivoInfoDTO>) repository.pegar(raizProduto, nomeImagens.toArray(new String[0]));
    }

    @SuppressWarnings("unchecked")
    public void deletarImagensPorNome(List<String> nomeImagens) {
        List<ArquivoInfoDTO> lista = (List<ArquivoInfoDTO>) repository.deletar(raizProduto, nomeImagens.toArray(new String[0]));

        //Programar aqui alguma forma de avisar ao administrador
        lista.forEach(arquivo -> {
            if (arquivo.erro() != null) {
                System.out.println("Erro ao tentar excluir o arquivo nome: " + arquivo.nomeArquivo());
            }
        });
    }

    public void deletarImagemPorNome(String nomeImagem) {
        ArquivoInfoDTO arquivo = (ArquivoInfoDTO) repository.deletar(raizProduto, nomeImagem);

        if (arquivo.erro() != null) {
            System.out.println("Erro ao tentar excluir o arquivo nome: " + arquivo.nomeArquivo());
        }
    }

    public void salvarTodos(List<ArquivoInfoDTO> arquivos) {
        arquivos.forEach(arquivo -> {
            repository.salvar(raizProduto, arquivo.nomeArquivo(), arquivo.bytesArquivo());
        });
    }

    public void salvar(ArquivoInfoDTO arquivo) {
        repository.salvar(raizProduto, arquivo.nomeArquivo(), arquivo.bytesArquivo());
    }
}
