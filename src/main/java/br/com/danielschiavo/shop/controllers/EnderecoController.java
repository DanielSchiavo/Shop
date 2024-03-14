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

import br.com.danielschiavo.shop.models.endereco.CadastrarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.services.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Endereço", description = "Todos endpoints relacionados com os endereços do usuário")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;
	
	@GetMapping("/cliente/endereco")
	@Operation(summary = "Pegar todos endereços do cliente")
	public ResponseEntity<List<MostrarEnderecoDTO>> pegarEnderecosCliente() {
		List<MostrarEnderecoDTO> mostrarEnderecoDTO = enderecoService.pegarEnderecosCliente();
		
		return ResponseEntity.status(HttpStatus.OK).body(mostrarEnderecoDTO);
	}
	
	@PutMapping("/cliente/endereco/{idEndereco}")
	@Operation(summary = "Alterar um endereço por id")
	public ResponseEntity<MostrarEnderecoDTO> alterarEndereco(@PathVariable Long idEndereco, @RequestBody @Valid CadastrarEnderecoDTO novoEnderecoDTO) {
		MostrarEnderecoDTO enderecoDTO = enderecoService.alterarEndereco(novoEnderecoDTO, idEndereco);
		
		return ResponseEntity.ok().body(enderecoDTO);
	}

	@PostMapping("/cliente/endereco")
	@Operation(summary = "Cadastrar novo endereço para o cliente")
	public ResponseEntity<MostrarEnderecoDTO> cadastrarNovoEndereco(@RequestBody @Valid CadastrarEnderecoDTO novoEnderecoDTO) {
		MostrarEnderecoDTO mostrarEnderecoDTO = enderecoService.cadastrarNovoEndereco(novoEnderecoDTO);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(mostrarEnderecoDTO);
	}
	
	@DeleteMapping("/cliente/endereco/{idEndereco}")
	@Operation(summary = "Deletar um endereço por id")
	public ResponseEntity<?> deletarEndereco(@PathVariable Long idEndereco) {
		enderecoService.deletarEndereco(idEndereco);
		
		return ResponseEntity.noContent().build();
	}
	

}
