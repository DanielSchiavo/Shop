package br.com.danielschiavo.shop.controllers.filestorage;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.infra.exceptions.FileStorageException;
import br.com.danielschiavo.shop.infra.exceptions.MensagemErroDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.filestorage.MostrarArquivoProdutoDTO;
import br.com.danielschiavo.shop.services.filestorage.FileStorageProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Serviço de Armazenamento de Arquivos (Imagens e Vídeos)", description = "Para fazer upload de imagens e videos antes de enviar para cadastro de produto, cadastro de pedido, foto de perfil do usuário.")
public class FileStorageProdutoController {

	@Autowired
	private FileStorageProdutoService fileStorageService;
	
	@DeleteMapping("/admin/filestorage/arquivo-produto/{nomeArquivo}")
	@Operation(summary = "Deleta o arquivo com o nome enviado no parametro da requisição")
	public ResponseEntity<?> deletarArquivoProduto(@PathVariable @NotNull String nomeArquivo) {
		try {
			fileStorageService.deletarArquivoProdutoNoDisco(nomeArquivo);
			return ResponseEntity.noContent().build();
			
		} catch (FileStorageException e) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(status).body(new MensagemErroDTO(status, e));
		}
	}
	
	@GetMapping("/admin/filestorage/arquivo-produto")
	@Operation(summary = "Recupera os bytes das imagens enviadas em um array no corpo da requisição")
	public ResponseEntity<?> mostrarArquivoProdutoPorListaDeNomes(@RequestBody List<MostrarArquivoProdutoDTO> listaMostrarArquivoProdutoDTO) {
		List<String> listaNomes = listaMostrarArquivoProdutoDTO.stream().map(lmap -> lmap.nome()).collect(Collectors.toList());
		List<ArquivoInfoDTO> listArquivos = fileStorageService.mostrarArquivoProdutoPorListaDeNomes(listaNomes);
		return ResponseEntity.ok(listArquivos);
	}
	
	@GetMapping("/admin/filestorage/arquivo-produto/{nomeArquivo}")
	@Operation(summary = "Recupera os bytes do nome do arquivo fornecido no parametro da requisição")
	public ResponseEntity<?> mostrarArquivoProdutoPorNome(@PathVariable String nomeArquivo) {
		try {
			ArquivoInfoDTO arquivo = fileStorageService.pegarArquivoProdutoPorNome(nomeArquivo);
			return ResponseEntity.ok(arquivo);
			
		} catch (FileStorageException e) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(status).body(new MensagemErroDTO(status, e));
		}
		
	}
	
	@PostMapping(path = "/admin/filestorage/arquivo-produto/array" , consumes = "multipart/form-data")
	@ResponseBody
	@Operation(summary = "Salva um array de arquivos enviados através de um formulário html e gera os seus respectivos nomes")
	public ResponseEntity<List<ArquivoInfoDTO>> cadastrarArrayArquivoProduto(
			@RequestPart(name = "arquivos", required = true) MultipartFile[] arquivos,
			UriComponentsBuilder uriBuilder
 			) {
		List<ArquivoInfoDTO> listArquivoInfoDTO = fileStorageService.persistirArrayArquivoProduto(arquivos, uriBuilder);
	    boolean erroEncontrado = listArquivoInfoDTO.stream()
	            .anyMatch(arquivo -> arquivo.erro() != null);
		if (erroEncontrado == false) {
			return ResponseEntity.created(uriBuilder.build().toUri()).body(listArquivoInfoDTO);
		}
		else {
			return ResponseEntity.badRequest().body(listArquivoInfoDTO);
		}
	}
	
	@PutMapping("/admin/filestorage/arquivo-produto")
	@Operation(summary = "Deleta o nomeAntigoDoArquivo e salva o arquivo enviado e gera um novo nome")
	public ResponseEntity<?> alterarArrayArquivoProduto(
			@RequestPart(name = "arquivo", required = true) MultipartFile[] arquivos,
			@RequestParam(name = "nomeAntigoDoArquivo", required = true) String nomesArquivosASeremExcluidos,
			UriComponentsBuilder uriBuilder
			) {
		List<ArquivoInfoDTO> arquivoInfoDTO = fileStorageService.alterarArrayArquivoProduto(arquivos, nomesArquivosASeremExcluidos, uriBuilder);

		return ResponseEntity.ok(arquivoInfoDTO);
	}
	
}
