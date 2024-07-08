package br.com.danielschiavo.filestorage.repository;

import br.com.danielschiavo.filestorage.ArquivoInfoDTO;
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

    public void deletar(String nomeImagem) {
        ArquivoInfoDTO arquivo = (ArquivoInfoDTO) repository.deletar(raizPerfil, nomeImagem);

        if (arquivo.erro() != null) {
            System.out.println("Erro ao tentar excluir o arquivo nome: " + arquivo.nomeArquivo());
        }
    }

    public ArquivoInfoDTO pegarFotoPerfilPorNome(String nomeImagem) {
        return (ArquivoInfoDTO) repository.pegar(raizPerfil, nomeImagem);
    }

    public void salvar(String nomeImagem, byte[] bytesImagem) {
        repository.salvar(raizPerfil, nomeImagem, bytesImagem);
    }
}
