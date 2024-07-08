package br.com.danielschiavo.cliente.mapper;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.danielschiavo.cliente.model.entity.Role;
import br.com.danielschiavo.cliente.dto.request.RoleDTO;

@Mapper(componentModel = "spring")
public abstract class RoleMapper {

	@Mapping(target = "dataEHoraAtribuicao", expression = "java(java.time.LocalDateTime.now())")
	@Mapping(target = "cliente", source = "cliente")
	public abstract Role stringRoleParaRoleEntity(RoleDTO adicionarRoleDTO, Cliente cliente);
}
