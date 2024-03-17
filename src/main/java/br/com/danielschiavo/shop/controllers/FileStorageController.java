package br.com.danielschiavo.shop.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.filestorage.MostrarArquivoProdutoDTO;
import br.com.danielschiavo.shop.models.filestorage.PostImagemPedidoDTO;
import br.com.danielschiavo.shop.services.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Serviço de Armazenamento de Arquivos (Imagens e Vídeos)", description = "Para fazer upload de imagens e videos antes de enviar para cadastro de produto, cadastro de pedido, foto de perfil do usuário.")
public class FileStorageController {

	@Autowired
	private FileStorageService fileStorageService;
	
//PRODUTO
	@DeleteMapping("/admin/filestorage/arquivo-produto/{nomeArquivo}")
	@Operation(summary = "Deleta o arquivo com o nome enviado no parametro da requisição")
	public ResponseEntity<?> deletarArquivoProduto(@PathVariable @NotNull String nomeArquivo) {
		fileStorageService.deletarArquivoProdutoNoDisco(nomeArquivo);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/admin/filestorage/arquivo-produto")
	@Operation(summary = "Recupera os bytes das imagens enviadas em um array no corpo da requisição")
	public ResponseEntity<List<ArquivoInfoDTO>> mostrarArquivoProdutoPorListaDeNomes(@RequestBody List<MostrarArquivoProdutoDTO> listaMostrarArquivoProdutoDTO) {
		List<String> listaNomes = listaMostrarArquivoProdutoDTO.stream().map(lmap -> lmap.nome()).collect(Collectors.toList());
		List<ArquivoInfoDTO> listArquivos = fileStorageService.mostrarArquivoProdutoPorListaDeNomes(listaNomes);
		
		return ResponseEntity.ok(listArquivos);
	}
	
	@GetMapping("/admin/filestorage/arquivo-produto/{nomeArquivo}")
	@Operation(summary = "Recupera os bytes do nome do arquivo fornecido no parametro da requisição")
	public ResponseEntity<ArquivoInfoDTO> mostrarArquivoProdutoPorNome(@PathVariable String nomeArquivo) {
		ArquivoInfoDTO arquivo = fileStorageService.pegarArquivoProdutoPorNome(nomeArquivo);
		
		return ResponseEntity.ok(arquivo);
	}
	
	@PostMapping(path = "/admin/filestorage/arquivo-produto/array" , consumes = "multipart/form-data")
	@ResponseBody
	@Operation(summary = "Salva um array de arquivos enviados através de um formulário html e gera os seus respectivos nomes")
	public ResponseEntity<List<ArquivoInfoDTO>> cadastrarArrayArquivoProduto(
			@RequestPart(name = "arquivos", required = true) MultipartFile[] arquivos,
			UriComponentsBuilder uriBuilder
 			) {
		List<ArquivoInfoDTO> listArquivoInfoDTO = fileStorageService.persistirArrayArquivoProduto(arquivos, uriBuilder);
		return ResponseEntity.created(uriBuilder.build().toUri()).body(listArquivoInfoDTO);
	}

	@PostMapping(path = "/admin/filestorage/arquivo-produto" , consumes = "multipart/form-data")
	@ResponseBody
	@Operation(summary = "Salva um arquivo enviado através de um formulario html e gera um nome")
	public ResponseEntity<ArquivoInfoDTO> cadastrarUmArquivoProduto(
			@RequestPart(name = "arquivo", required = true) MultipartFile arquivo,
			UriComponentsBuilder uriBuilder
 			) {
		ArquivoInfoDTO arquivoInfo = fileStorageService.persistirUmArquivoProduto(arquivo);
		
		URI uri = uriBuilder.path("/shop/admin/filestorage/arquivo-produto" + arquivoInfo.nomeArquivo()).build().toUri();
		return ResponseEntity.created(uri).body(arquivoInfo);
	}
	
	@PutMapping("/admin/filestorage/arquivo-produto")
	@Operation(summary = "Deleta o nomeAntigoDoArquivo e salva o arquivo enviado e gera um novo nome")
	public ResponseEntity<?> alterarArquivoProduto(
			@RequestPart(name = "arquivo", required = true) MultipartFile arquivo,
			@RequestParam(name = "nomeAntigoDoArquivo", required = true) String nomeAntigoDoArquivo
			) {
		ArquivoInfoDTO arquivoInfoDTO = fileStorageService.alterarArquivoProduto(arquivo, nomeAntigoDoArquivo);

		return ResponseEntity.ok(arquivoInfoDTO);
	}
	
	
//PERFIL
	@DeleteMapping("/admin/filestorage/foto-perfil/{nomeFotoPerfilAntiga}")
	@Operation(summary = "Deleta a foto de perfil com o nome enviado no parametro da requisição")
	public ResponseEntity<?> deletarFotoPerfil(@PathVariable String nomeFotoPerfilAntiga) {
		fileStorageService.deletarFotoPerfilNoDisco(nomeFotoPerfilAntiga);

		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/admin/filestorage/foto-perfil/{nomeFotoPerfil}")
	@Operation(summary = "Pega uma foto de perfil dado o nome da foto no parametro da requisição")
	public ResponseEntity<ArquivoInfoDTO> pegarFotoPerfilPorNome(@PathVariable String nomeFotoPerfil) {
		ArquivoInfoDTO arquivo = fileStorageService.pegarFotoPerfilPorNome(nomeFotoPerfil);
		
		return ResponseEntity.ok(arquivo);
	}
	
	@PostMapping("/admin/filestorage/foto-perfil")
	@Operation(summary = "Cadastra uma foto de perfil enviada através de um formulario html e gera um nome")
	public ResponseEntity<?> cadastrarFotoPerfil(
			@RequestPart(name = "foto", required = true) MultipartFile foto,
			UriComponentsBuilder uriBuilder
			) {
		ArquivoInfoDTO arquivoInfoDTO = fileStorageService.persistirFotoPerfil(foto);
		
		URI uri = uriBuilder.path("/shop/admin/filestorage/foto-perfil/" + arquivoInfoDTO.nomeArquivo()).build().toUri();
		return ResponseEntity.created(uri).body(arquivoInfoDTO);
	}
	
	@PutMapping("/admin/filestorage/foto-perfil/{nomeFotoPerfilAntiga}")
	@Operation(summary = "Deleta o nomeAntigoDoArquivo e salva o arquivo enviado e gera um novo nome")
	public ResponseEntity<?> alterarFotoPerfil(
			@RequestPart(name = "foto", required = true) MultipartFile novaFoto,
			@PathVariable String nomeFotoPerfilAntiga,
			UriComponentsBuilder uriBuilder
			) {
		ArquivoInfoDTO arquivoInfoDTO = fileStorageService.alterarFotoPerfil(novaFoto, nomeFotoPerfilAntiga);
		
		return ResponseEntity.ok(arquivoInfoDTO);
	}
	

//PEDIDO	
	@GetMapping("/admin/filestorage/pedido/{nomeImagemPedido}")
	@Operation(summary = "Recupera os bytes da imagem do pedido dado o nome no parametro da requisição")
	public ResponseEntity<ArquivoInfoDTO> pegarImagemPedidoPorNome(@PathVariable String nomeImagemPedido) {
		ArquivoInfoDTO arquivo = fileStorageService.pegarImagemPedidoPorNome(nomeImagemPedido);
		
		return ResponseEntity.ok(arquivo);
	}
	
	@PostMapping("/admin/filestorage/pedido")
	@Operation(summary = "Cadastra uma nova imagem do pedido e devolve o nome e os bytes da imagem, ou, se já tiver uma imagem cadastrada devolve o nome e os bytes da imagem já cadastrada")
	public ResponseEntity<?> persistirOuRecuperarImagemPedido(
			@RequestBody PostImagemPedidoDTO postImagemPedidoDTO,
			UriComponentsBuilder uriBuilder
			) {
		String nomeArquivo = fileStorageService.persistirOuRecuperarImagemPedido(postImagemPedidoDTO.nomePrimeiraImagemProduto(), postImagemPedidoDTO.idProduto());
		ArquivoInfoDTO arquivoInfoDTO = new ArquivoInfoDTO(nomeArquivo, null);
		
		URI uri = uriBuilder.path("/filestorage/pedido/" + arquivoInfoDTO.nomeArquivo()).build().toUri();
		return ResponseEntity.created(uri).body(arquivoInfoDTO);
	}
	
}
