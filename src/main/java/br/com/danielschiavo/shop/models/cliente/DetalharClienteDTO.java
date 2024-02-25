package br.com.danielschiavo.shop.models.cliente;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import br.com.danielschiavo.shop.models.endereco.EnderecoDTO;

public record DetalharClienteDTO(
		Long id,
		String cpf,
		String nome,
		String sobrenome,
		LocalDate dataNascimento,
		String email,
		String senha,
		String celular,
		List<EnderecoDTO> enderecos
		) {

	public DetalharClienteDTO(Cliente cliente) {
		this(
			cliente.getId(),
			cliente.getCpf(),
			cliente.getNome(),
			cliente.getSobrenome(),
			cliente.getDataNascimento(),
			cliente.getEmail(),
			cliente.getSenha(),
			cliente.getCelular(),
			cliente.getEnderecos().stream()
	        .map(EnderecoDTO::converterParaEnderecoDTO)
	        .collect(Collectors.toList())
				);
	}

}
