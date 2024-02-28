package br.com.danielschiavo.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.endereco.EnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.repositories.ClienteRepository;
import br.com.danielschiavo.shop.repositories.EnderecoRepository;

@Service
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private TokenJWTService tokenJWTService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public void save(Endereco address) {
		enderecoRepository.save(address);
	}
	
	public Endereco verificarID(Long clienteId, Long enderecoId) {
		Optional<Endereco> optionalEndereco = enderecoRepository.findByClienteIdAndEnderecoId(clienteId, enderecoId);
		if (optionalEndereco.isPresent()) {
			Endereco endereco = optionalEndereco.get();
			return endereco;
		} else {	
			throw new RuntimeException("ID do Endereco ou Cliente inexistente! ");
		}
	}

	@Transactional
	public MostrarEnderecoDTO cadastrarNovoEndereco(EnderecoDTO novoEnderecoDTO) {
		var novoEndereco = new Endereco(novoEnderecoDTO);
		
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		novoEndereco.setCliente(cliente);
		
		if (novoEnderecoDTO.enderecoPadrao() == true) {
			
			var optionalEndereco = enderecoRepository.findByClienteAndEnderecoPadraoTrue(cliente);
			if (optionalEndereco.isPresent()) {
				var endereco = optionalEndereco.get();
				endereco.setEnderecoPadrao(false);
				enderecoRepository.save(endereco);
			}
			
			novoEndereco.setEnderecoPadrao(true);
		}
		
		enderecoRepository.save(novoEndereco);
		
		return new MostrarEnderecoDTO(novoEndereco);
	}

	@Transactional
	public MostrarEnderecoDTO alterarEndereco(EnderecoDTO enderecoDTO, Long idEndereco) {
		var endereco = enderecoRepository.findById(idEndereco).get();
		
		endereco.alterarEndereco(enderecoDTO);
		
		enderecoRepository.save(endereco);
		
		return new MostrarEnderecoDTO(endereco);
	}

	@Transactional
	public void deletarEndereco(Long id) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		var endereco = enderecoRepository.findByCliente(cliente);
		
		enderecoRepository.delete(endereco);
	}

	public List<MostrarEnderecoDTO> pegarEnderecosCliente() {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		
		var pageEndereco = enderecoRepository.findAllByCliente(cliente);
		return pageEndereco.stream().map(MostrarEnderecoDTO::converterParaEnderecoDTO).toList();
	}
	
}
