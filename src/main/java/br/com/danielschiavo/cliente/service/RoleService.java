package br.com.danielschiavo.cliente.service;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.mapper.RoleMapper;
import br.com.danielschiavo.cliente.repository.ClienteRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.cliente.dto.request.RoleDTO;
import br.com.danielschiavo.cliente.model.enums.NomeRole;
import br.com.danielschiavo.cliente.model.entity.Role;

@Service
public class RoleService {
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private RoleMapper roleMapper;

	public String adicionarRole(RoleDTO adicionarRoleDTO) {
		NomeRole nomeRole = adicionarRoleDTO.role();
		if (nomeRole != NomeRole.ADMIN) {
			throw new ValidacaoException("Envie uma role válida! não existe a role " + nomeRole);
		}
		
		Cliente cliente = clienteService.pegarClientePorId(adicionarRoleDTO.idCliente());
		Role role = roleMapper.stringRoleParaRoleEntity(adicionarRoleDTO, cliente);
		cliente.adicionarRole(role);
		clienteRepository.save(cliente);

		return "Usuario promovido a " + nomeRole + " com sucesso!";
	}

	public String removerRole(RoleDTO removerRoleDTO) {
		if (removerRoleDTO.role() != NomeRole.ADMIN) {
			throw new ValidacaoException("Envie uma role válida! não existe a role " + removerRoleDTO.role());
		}
		Cliente cliente = clienteService.pegarClientePorId(removerRoleDTO.idCliente());
		Role role = cliente.getRoles().stream()
						.filter(r -> r.getRole().equals(removerRoleDTO.role()))
						.findFirst().orElseThrow(() -> new ValidacaoException("O cliente não possui a role de nome " + removerRoleDTO.role()));
		cliente.removerRole(role);
		clienteRepository.save(cliente);
		
		return "Removido permissão de " + removerRoleDTO.role() + " do usuario com sucesso!";
	}
	
}
