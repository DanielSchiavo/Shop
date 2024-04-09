package br.com.danielschiavo.shop.controllers.cliente.user;


import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.infra.exceptions.MensagemErroDTO;
import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cliente.dto.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.dto.AlterarFotoPerfilDTO;
import br.com.danielschiavo.shop.models.cliente.dto.CadastrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.dto.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.services.cliente.user.ClienteUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente - User", description = "Todos endpoints relacionados com o cliente, que o próprio poderá utilizar")
public class ClienteUserController {

	@Autowired
	private ClienteUserService clienteUserService;
	
	@DeleteMapping("/cliente/foto-perfil")
	@Operation(summary = "Deleta foto do perfil do cliente")
	public ResponseEntity<?> deletarFotoPerfilClientePorIdToken() {
		try {
			clienteUserService.deletarFotoPerfilPorIdToken();
			return ResponseEntity.noContent().build();

		} catch (ValidacaoException e) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(status).body(new MensagemErroDTO(status, e));
		} catch (IOException e) {
			HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(status).body(new MensagemErroDTO(status.toString(), "Falha interna no servidor ao tentar excluir o arquivo."));
		}
	}
	
	@GetMapping("/cliente/pagina-inicial")
	public ResponseEntity<MostrarClienteDTO> detalharClientePaginaInicialPorIdToken() {
		MostrarClienteDTO mostrarClienteDtoPaginaInicial = clienteUserService.detalharClientePorIdTokenPaginaInicial();
		
		return ResponseEntity.ok(mostrarClienteDtoPaginaInicial);
	}

	@GetMapping("/cliente")
	@Operation(summary = "Mostra todos os dados do cliente")
	public ResponseEntity<?> detalharClientePorIdToken() {
		MostrarClienteDTO detalharClienteDTO = clienteUserService.detalharClientePorIdToken();
		
		return ResponseEntity.ok(detalharClienteDTO);
	}
	
	@PostMapping("/publico/cadastrar/cliente")
	@Operation(summary = "Cadastro de cliente")
	public ResponseEntity<?> cadastrarCliente(@RequestBody @Valid CadastrarClienteDTO cadastrarClienteDTO,
			UriComponentsBuilder uriBuilder) {
		MostrarClienteDTO mostrarClienteDTO = clienteUserService.cadastrarCliente(cadastrarClienteDTO);
		
		var uri = uriBuilder.path("/shop/cliente/{id}").buildAndExpand(mostrarClienteDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(mostrarClienteDTO);
	}
	
	@PutMapping("/cliente")
	@Operation(summary = "Cliente altera seus próprios dados")
	public ResponseEntity<MostrarClienteDTO> alterarClientePorIdToken(@RequestBody @Valid AlterarClienteDTO alterarClienteDTO) {
		MostrarClienteDTO mostrarClienteDTO = clienteUserService.alterarClientePorIdToken(alterarClienteDTO);

		return ResponseEntity.ok(mostrarClienteDTO);
	}
	
	@PutMapping("/cliente/foto-perfil")
	@Operation(summary = "Alterar a foto do perfil do cliente")
	public ResponseEntity<?> alterarFotoPerfilPorIdToken(@RequestBody @Valid AlterarFotoPerfilDTO alterarFotoPerfilDTO) {
		try {
			ArquivoInfoDTO arquivoInfoDTO = clienteUserService.alterarFotoPerfilPorIdToken(alterarFotoPerfilDTO);
			return ResponseEntity.ok(arquivoInfoDTO);
			
		} catch (ValidacaoException e) {
			HttpStatus status = HttpStatus.NOT_FOUND;
			return ResponseEntity.status(status).body(new MensagemErroDTO(status, e));
		}
	}
}
