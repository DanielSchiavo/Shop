package br.com.danielschiavo.shop.mapper.cliente;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.role.NomeRole;
import br.com.danielschiavo.shop.models.cliente.role.Role;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {

	public Role stringRoleParaRoleEntity(String nomeRole, Cliente cliente) {
		Role role = new Role();
		role.setDataAtribuicao(LocalDateTime.now());
		if (nomeRole.equals("ADMIN")) {
			role.setRole(NomeRole.ADMIN);
		}
		role.setCliente(cliente);
		return role;
	}
}
