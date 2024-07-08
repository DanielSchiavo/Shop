package br.com.danielschiavo.cliente.service;


import br.com.danielschiavo.cliente.dto.request.endereco.AlterarEnderecoRequest;
import br.com.danielschiavo.cliente.dto.request.endereco.CadastrarEnderecoRequest;
import br.com.danielschiavo.cliente.model.entity.Endereco;
import br.com.danielschiavo.cliente.dto.response.endereco.MostrarEnderecoResponse;
import br.com.danielschiavo.cliente.repository.EnderecoRepository;
import br.com.danielschiavo.cliente.mapper.EnderecoMapper;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shared.infra.security.SecurityService;
import lombok.Setter;

import java.util.List;

@Service
@Setter
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private EnderecoMapper enderecoMapper;
	
	@Transactional
	public void deletarEnderecoPorId(Long enderecoId, Long clienteId) {
		enderecoRepository.deleteByIdAndClienteId(enderecoId, clienteId);
	}
	
	public List<Endereco> pegarTodosEnderecosCliente(Long clienteId) {
		return enderecoRepository.findAllByClienteId(clienteId)
				.orElseThrow(() -> new ValidacaoException("Cliente não possui nenhum endereço cadastrado"));
	}
	
	public Endereco pegarEnderecoPorId(Long enderecoId, Long clienteId) {
		return enderecoRepository.findByIdAndClienteId(enderecoId, clienteId).orElseThrow(() -> new ValidacaoException("Cliente não tem um endereço com o ID " + enderecoId));
	}
	
	@Transactional
	public Endereco cadastrarEndereco(CadastrarEnderecoRequest request, Long clienteId) {
		Endereco endereco = enderecoMapper.toEntity(request);
		endereco.setClienteId(clienteId);

		List<Endereco> enderecos = pegarTodosEnderecosCliente(clienteId);

		if (request.enderecoPadrao()) {
			enderecos.stream().filter(end -> end.getEnderecoPadrao().equals(true)).forEach(end -> end.setEnderecoPadrao(false));
		}

		enderecos.add(endereco);
		enderecoRepository.saveAll(enderecos);
		return endereco;
	}
	
	@Transactional
	public Endereco alterarEnderecoPorIdToken(AlterarEnderecoRequest request, Long enderecoId, Long clienteId) {
		List<Endereco> todosEnderecos = pegarTodosEnderecosCliente(clienteId);
		
		Endereco endereco = todosEnderecos.stream().filter(end -> end.getId().equals(enderecoId))
				.findFirst().orElseThrow(() -> new ValidacaoException("Não existe endereço de id número " + enderecoId + " para o cliente de id número " + clienteId));

		enderecoMapper.alterarEnderecoDtoParaEndereco(request, endereco);
		if (request.enderecoPadrao()) {
			todosEnderecos.stream().filter(end -> end.getEnderecoPadrao().equals(true) && !end.getId().equals(enderecoId))
					.forEach(end -> end.setEnderecoPadrao(false));
		}

		enderecoRepository.saveAll(todosEnderecos);
		return endereco;
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
