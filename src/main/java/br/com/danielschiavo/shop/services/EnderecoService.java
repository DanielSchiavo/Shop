package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.repositories.EnderecoRepository;

@Service
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public void save(Endereco address) {
		enderecoRepository.save(address);
	}
	
	public Endereco verificarID(Long clienteId, Long enderecoId) {
		Optional<Endereco> optionalEndereco = enderecoRepository.findByCliente_idAndEnderecoId(clienteId, enderecoId);
		if (optionalEndereco.isPresent()) {
			Endereco endereco = optionalEndereco.get();
			return endereco;
		} else {	
			throw new RuntimeException("ID do Endereco ou Cliente inexistente! ");
		}
	}

}
