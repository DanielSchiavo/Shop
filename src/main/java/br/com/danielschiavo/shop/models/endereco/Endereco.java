package br.com.danielschiavo.shop.models.endereco;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.ClienteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "clientes_enderecos")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String cep;
	private String rua;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	
    @NotBlank(message = "O estado não pode estar vazio.")
    @Size(min = 2, max = 2, message = "O estado deve conter exatamente 2 letras.")
    @Pattern(regexp = "^[A-Za-z]{2}$", message = "O estado deve conter apenas letras.")
	private String estado;
	
	private Boolean enderecoPadrao;
	
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
	
	public Endereco(ClienteDTO clienteDTO, Cliente cliente) {
		this.cep = clienteDTO.endereco().cep();
		this.rua = clienteDTO.endereco().rua();
		this.numero = clienteDTO.endereco().numero();
		this.complemento = clienteDTO.endereco().complemento();
		this.bairro = clienteDTO.endereco().bairro();
		this.cidade = clienteDTO.endereco().cidade();
		this.estado = clienteDTO.endereco().estado();
		this.cliente = cliente;
	}

	public Endereco(EnderecoDTO novoEnderecoDTO) {
		this.cep = novoEnderecoDTO.cep();
		this.rua = novoEnderecoDTO.rua();
		this.numero = novoEnderecoDTO.numero();
		if (novoEnderecoDTO.complemento() != null) {
			this.complemento = novoEnderecoDTO.complemento();
		}
		this.bairro = novoEnderecoDTO.bairro();
		this.cidade = novoEnderecoDTO.cidade();
		this.estado = novoEnderecoDTO.estado();
		if (novoEnderecoDTO.enderecoPadrao() != null) {
			this.enderecoPadrao = novoEnderecoDTO.enderecoPadrao();
		}
	}

	public void alterarEndereco(EnderecoDTO enderecoDTO) {
		if (enderecoDTO.cep() != null) {
			this.cep = enderecoDTO.cep();
		}
		if (enderecoDTO.rua() != null) {
			this.rua = enderecoDTO.rua();
		}
		if (enderecoDTO.numero() != null) {
			this.numero = enderecoDTO.numero();
		}
		if (enderecoDTO.complemento() != null) {
			this.complemento = enderecoDTO.complemento();
		}
		if (enderecoDTO.bairro() != null) {
			this.bairro = enderecoDTO.bairro();
		}
		if (enderecoDTO.cidade() != null) {
			this.cidade = enderecoDTO.cidade();
		}
		if (enderecoDTO.estado() != null) {
			this.estado = enderecoDTO.estado();
		}
		if (enderecoDTO.enderecoPadrao() != null) {
			this.enderecoPadrao = enderecoDTO.enderecoPadrao();
		}
	}
}