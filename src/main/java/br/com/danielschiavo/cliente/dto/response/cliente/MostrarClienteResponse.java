package br.com.danielschiavo.cliente.dto.response.cliente;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class MostrarClienteResponse {
	
	private Long id;
	private String cpf;
	private String nome;
	private String sobrenome;
	private LocalDate dataNascimento;
	private LocalDate dataCriacaoConta;
	private String email;
	private String celular;
	private String fotoPerfil;
}
