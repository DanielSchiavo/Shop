package br.com.danielschiavo.shop.models.cliente.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.danielschiavo.shop.models.cliente.cartao.MostrarCartaoDTO;
import br.com.danielschiavo.shop.models.cliente.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
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
public class MostrarClienteDTO {
	
	private Long id;
	private String cpf;
	private String nome;
	private String sobrenome;
	private LocalDate dataNascimento;
	private LocalDate dataCriacaoConta;
	private String email;
	private String celular;
	private ArquivoInfoDTO fotoPerfil;
	private List<MostrarEnderecoDTO> enderecos;
	private List<MostrarCartaoDTO> cartoes;
}
