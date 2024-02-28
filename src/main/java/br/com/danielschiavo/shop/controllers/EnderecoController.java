package br.com.danielschiavo.shop.controllers;


import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.models.endereco.EnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.services.EnderecoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;

	@PostMapping("/cliente/endereco")
	public ResponseEntity<MostrarEnderecoDTO> cadastrarNovoEndereco(@RequestBody @Valid EnderecoDTO novoEnderecoDTO) {
		MostrarEnderecoDTO detalharEnderecoDTO = enderecoService.cadastrarNovoEndereco(novoEnderecoDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(detalharEnderecoDTO);
	}
	
	@GetMapping("/cliente/endereco")
	public ResponseEntity<List<MostrarEnderecoDTO>> pegarEnderecosCliente() {
		List<MostrarEnderecoDTO> detalharEnderecoDTO = enderecoService.pegarEnderecosCliente();
		
		return ResponseEntity.status(HttpStatus.OK).body(detalharEnderecoDTO);
	}
	
	@PutMapping("/cliente/endereco/{id}")
	public ResponseEntity<MostrarEnderecoDTO> alterarEndereco(@PathVariable Long id, @RequestBody @Valid EnderecoDTO novoEnderecoDTO) {
		MostrarEnderecoDTO enderecoDTO = enderecoService.alterarEndereco(novoEnderecoDTO, id);
		
		return ResponseEntity.ok().body(enderecoDTO);
	}
	
	@DeleteMapping("/cliente/endereco/{id}")
	public ResponseEntity<?> deletarEndereco(@PathVariable Long id) {
		enderecoService.deletarEndereco(id);
		
		return ResponseEntity.noContent().build();
	}
	

}
