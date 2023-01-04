package br.com.danielschiavo.shop.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.danielschiavo.shop.models.client.Client;
import br.com.danielschiavo.shop.models.client.ClientDTO;
import br.com.danielschiavo.shop.models.client.DetailingClientDTO;
import br.com.danielschiavo.shop.models.client.UpdateClientDTO;
import br.com.danielschiavo.shop.services.ClientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("clients")
public class ClientController {

	@Autowired
	private ClientService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DetailingClientDTO> registerClient(@RequestBody @Valid ClientDTO clientDTO, UriComponentsBuilder uriBuilder){
		var client = new Client(clientDTO);
		service.save(client);
		
		var uri = uriBuilder.path("/clients/{id}").buildAndExpand(client.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DetailingClientDTO(client));
	}
	
	@GetMapping
	public ResponseEntity<List<Client>> datailingAllClients(){
		var client = service.getAllClients();
		return ResponseEntity.ok(client);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> datailingById(@PathVariable Long id){
		var client = service.getReferenceById(id);
		return ResponseEntity.ok(new DetailingClientDTO(client));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<DetailingClientDTO> updateById(@PathVariable Long id, @RequestBody UpdateClientDTO updateClientDTO){
		var client = service.getReferenceById(id);
		client.updateAttributes(updateClientDTO);
		
		return ResponseEntity.ok(new DetailingClientDTO(client));
	}
}
