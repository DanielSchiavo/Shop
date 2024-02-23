package br.com.danielschiavo.shop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.cliente.AtualizarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.ClienteDTO;
import br.com.danielschiavo.shop.models.cliente.ClientePaginaInicialDTO;
import br.com.danielschiavo.shop.models.cliente.DetalharClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MensagemEFotoPerfilDTO;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.services.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ClienteRepository clientRepository;

	@PostMapping("/registrar/cliente")
	@Transactional
	public ResponseEntity<DetalharClienteDTO> cadastrarCliente(@RequestBody @Valid ClienteDTO clientDTO,
			UriComponentsBuilder uriBuilder) {
		Cliente cliente = clienteService.cadastrarCliente(clientDTO);
		
		var uri = uriBuilder.path("/shop/cliente/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).body(new DetalharClienteDTO(cliente));
	}
	
	@PutMapping("/cliente/{id}")
	@Transactional
	public ResponseEntity<DetalharClienteDTO> atualizarClientePorId(@PathVariable Long id,
			@RequestBody AtualizarClienteDTO updateClientDTO) {
		Cliente cliente = clienteService.atualizarClientePorId(id, updateClientDTO);

		return ResponseEntity.ok(new DetalharClienteDTO(cliente));
	}
	
	@GetMapping("/cliente")
	public ResponseEntity<?> detalharCliente() {
		DetalharClienteDTO detalharClienteDTO = clienteService.detalharClientePorId();
		
		return ResponseEntity.ok(detalharClienteDTO);
	}

	@GetMapping("/admin/cliente/{id}")
	public ResponseEntity<?> detalharClientePorIdMaisTodosOsPedidos(@PathVariable Long id) {
		DetalharClienteDTO detalharClientePorIdMaisTodosOsPedidos = clienteService.detalharClientePorIdMaisTodosOsPedidos(id);
		
		return ResponseEntity.ok(detalharClientePorIdMaisTodosOsPedidos);
	}


	@PutMapping("/cliente/{id}/foto-perfil")
	@Transactional
	public ResponseEntity<MensagemEFotoPerfilDTO> atualizarFotoPerfil(@PathVariable Long id,
			@RequestParam("foto_perfil") MultipartFile novaImagem) {
		MensagemEFotoPerfilDTO mensagemEFotoPerfilDTO = clienteService.atualizarFotoPerfil(id, novaImagem);
		
		return ResponseEntity.ok(mensagemEFotoPerfilDTO);
	}

	@DeleteMapping("/cliente/{id}/imagem-perfil")
	public ResponseEntity<?> deletarFotoPerfil(@PathVariable Long id) {
		clienteService.deletarFotoPerfil(id);

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/cliente/{id}/pagina-inicial")
	public ResponseEntity<ClientePaginaInicialDTO> pegarDadosParaExibirNaPaginaInicial(@PathVariable Long id) {
		ClientePaginaInicialDTO clientePaginaInicialDTO = clienteService.pegarDadosParaExibirNaPaginaInicial(id);
		
		return ResponseEntity.ok(clientePaginaInicialDTO);
	}
	
	@DeleteMapping("/admin/cliente/{id}")
	public ResponseEntity<?> deletarClientePorId(@PathVariable Long id) {
		clientRepository.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/admin/cliente")
	public ResponseEntity<Page<DetalharClienteDTO>> detalharTodosClientes(Pageable pageable) {
		var client = clienteService.pegarTodosClientes(pageable);
		return ResponseEntity.ok(client);
	}

}
