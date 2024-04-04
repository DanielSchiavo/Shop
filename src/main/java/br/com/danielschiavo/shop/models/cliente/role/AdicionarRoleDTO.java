package br.com.danielschiavo.shop.models.cliente.role;

import jakarta.validation.constraints.NotNull;

public record AdicionarRoleDTO(
		@NotNull
		Long idCliente,
		@NotNull
		String role
		) {

}
