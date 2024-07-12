package br.com.danielschiavo.cliente.service.endereco;


import br.com.danielschiavo.cliente.model.entity.Endereco;
import br.com.danielschiavo.cliente.repository.EnderecoRepository;
import br.com.danielschiavo.cliente.mapper.EnderecoMapper;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;

import java.util.List;

@Service
@Setter
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository repository;
	
	@Autowired
	private EnderecoMapper mapper;
	
	@Transactional
	public void deletarEnderecoPorId(Long enderecoId, Long clienteId) {
		repository.deleteByIdAndClienteId(enderecoId, clienteId);
	}
	
	public List<Endereco> pegarTodosEnderecosCliente(Long clienteId) {
		return repository.findAllByClienteId(clienteId)
				.orElseThrow(() -> new ValidacaoException("Cliente não possui nenhum endereço cadastrado"));
	}
	
	public Endereco pegarEnderecoPorId(Long enderecoId, Long clienteId) {
		return repository.findByIdAndClienteId(enderecoId, clienteId).orElseThrow(() -> new ValidacaoException("Cliente não tem um endereço com o ID " + enderecoId));
	}
	
	@Transactional
	public Endereco cadastrarEndereco(Long clienteId, Endereco endereco) {
		endereco.setClienteId(clienteId);

		List<Endereco> enderecos = pegarTodosEnderecosCliente(clienteId);

		if (endereco.getEnderecoPadrao()) {
			enderecos.stream().filter(end -> end.getEnderecoPadrao().equals(true)).forEach(end -> end.setEnderecoPadrao(false));
		}

		enderecos.add(endereco);
		repository.saveAll(enderecos);
		return endereco;
	}
	
	@Transactional
	public Endereco alterarEnderecoPorIdToken(Long enderecoId, Long clienteId, Endereco enderecoAtualizado) {
		List<Endereco> todosEnderecos = pegarTodosEnderecosCliente(clienteId);
		
		Endereco endereco = todosEnderecos.stream().filter(end -> end.getId().equals(enderecoId))
				.findFirst().orElseThrow(() -> new ValidacaoException("Não existe endereço de id número " + enderecoId + " para o cliente de id número " + clienteId));

		mapper.alterarEndereco(enderecoAtualizado, endereco);

		if (enderecoAtualizado.getEnderecoPadrao()) {
			todosEnderecos.stream().filter(end -> end.getEnderecoPadrao().equals(true) && !end.getId().equals(enderecoId))
					.forEach(end -> end.setEnderecoPadrao(false));
		}

		repository.saveAll(todosEnderecos);
		return endereco;
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
