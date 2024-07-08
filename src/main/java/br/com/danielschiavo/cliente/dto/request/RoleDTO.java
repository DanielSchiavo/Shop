package br.com.danielschiavo.cliente.dto.request;

import br.com.danielschiavo.cliente.model.enums.NomeRole;
import jakarta.validation.constraints.NotNull;

public record RoleDTO(
		@NotNull
		Long idCliente,
		@NotNull
		NomeRole role
		) {

}
