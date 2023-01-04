package br.com.danielschiavo.shop.models.client;

import java.time.LocalDate;

import br.com.danielschiavo.shop.models.address.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "clients")
@Entity(name = "Client")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Client {


	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String cpf;
	private String name;
	
	@Column(name = "last_name")
	private String lastName;
	
	private LocalDate birthDate;
	private String email;
	private String password;
	private String telephone;
	
	@Embedded
	private Address address;
	
	public Client(@Valid ClientDTO clientDTO) {
		this.cpf = clientDTO.cpf();
		this.name = clientDTO.name();
		this.lastName = clientDTO.lastName();
		this.birthDate = clientDTO.birthDate();
		this.email = clientDTO.email();
		this.password = clientDTO.password();
		this.telephone = clientDTO.telephone();
		this.address = clientDTO.address();
	}

	public void updateAttributes(UpdateClientDTO updateClientDTO) {
		if (updateClientDTO.cpf() != null) {
			this.cpf = updateClientDTO.cpf();
		}
		if (updateClientDTO.name() != null) {
			this.name = updateClientDTO.name();
		}
		if (updateClientDTO.lastName() != null) {
			this.lastName = updateClientDTO.lastName();
		}
		if (updateClientDTO.birthDate() != null) {
			this.birthDate = updateClientDTO.birthDate();
		}
		if (updateClientDTO.email() != null) {
			this.email = updateClientDTO.email();
		}
		if (updateClientDTO.password() != null) {
			this.password = updateClientDTO.password();
		}
		if (updateClientDTO.telephone() != null) {
			this.telephone = updateClientDTO.telephone();
		}
		if (updateClientDTO.address() != null) {
			this.address = updateClientDTO.address();
		}
	}
}
