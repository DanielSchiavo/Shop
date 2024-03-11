package br.com.danielschiavo.shop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.cliente.AlterarFotoPerfilDTO;
import br.com.danielschiavo.shop.models.cliente.AtualizarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.ClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente", description = "Todos endpoints relacionados com o usuário")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ClienteRepository clientRepository;

	@GetMapping("/cliente")
	@Operation(summary = "Mostra todos os dados do cliente")
	public ResponseEntity<?> detalharCliente() {
		MostrarClienteDTO detalharClienteDTO = clienteService.detalharClientePorId();
		
		return ResponseEntity.ok(detalharClienteDTO);
	}
	
	@PutMapping("/cliente")
	@Operation(summary = "Cliente altera seus próprios dados")
	public ResponseEntity<MostrarClienteDTO> alterarClientePorId(@RequestBody AtualizarClienteDTO updateClientDTO) {
		Cliente cliente = clienteService.atualizarClientePorId(updateClientDTO);

		return ResponseEntity.ok(new MostrarClienteDTO(cliente));
	}
	
	@PutMapping("/cliente/foto-perfil")
	@Operation(summary = "Alterar a foto do perfil do cliente")
	public ResponseEntity<?> atualizarFotoPerfil(@RequestBody AlterarFotoPerfilDTO alterarFotoPerfilDTO) {
		var mensagemEFotoPerfilDTO = clienteService.alterarFotoPerfil(alterarFotoPerfilDTO);
		
		return ResponseEntity.ok(mensagemEFotoPerfilDTO);
	}
	
	@PostMapping("/cadastrar/cliente")
	@Operation(summary = "Cadastro de cliente")
	public ResponseEntity<MostrarClienteDTO> cadastrarCliente(@RequestBody @Valid ClienteDTO clientDTO,
			UriComponentsBuilder uriBuilder) {
		System.out.println(" TESTE ");
		Cliente cliente = clienteService.cadastrarCliente(clientDTO);
		
		var uri = uriBuilder.path("/shop/cliente/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).body(new MostrarClienteDTO(cliente));
	}

	@DeleteMapping("/cliente/foto-perfil")
	@Operation(summary = "Deleta foto do perfil do cliente")
	public ResponseEntity<?> deletarFotoPerfil() {
		clienteService.deletarFotoPerfil();

		return ResponseEntity.noContent().build();
	}

//	@GetMapping("/cliente/{id}/pagina-inicial")
//	public ResponseEntity<MostrarClientePaginaInicialDTO> pegarDadosParaExibirNaPaginaInicial(@PathVariable Long id) {
//		MostrarClientePaginaInicialDTO clientePaginaInicialDTO = clienteService.pegarDadosParaExibirNaPaginaInicial(id);
//		
//		return ResponseEntity.ok(clientePaginaInicialDTO);
//	}
	
	@GetMapping("/admin/cliente")
	@Operation(summary = "Mostra todos os clientes cadastrados")
	public ResponseEntity<Page<MostrarClienteDTO>> detalharTodosClientes(Pageable pageable) {
		var client = clienteService.pegarTodosClientes(pageable);
		return ResponseEntity.ok(client);
	}
	
	@DeleteMapping("/admin/cliente/{idCliente}")
	@Operation(summary = "Deleta o cliente pelo id fornecido no parametro da requisição")
	public ResponseEntity<?> deletarClientePorId(@PathVariable Long idCliente) {
		clientRepository.deleteById(idCliente);
		return ResponseEntity.noContent().build();
	}
}
