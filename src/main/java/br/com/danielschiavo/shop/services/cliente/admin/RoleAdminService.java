package br.com.danielschiavo.shop.services.cliente.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.mapper.cliente.RoleMapper;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.role.AdicionarRoleDTO;
import br.com.danielschiavo.shop.models.cliente.role.Role;
import br.com.danielschiavo.shop.repositories.cliente.ClienteRepository;
import br.com.danielschiavo.shop.services.cliente.ClienteUtilidadeService;

@Service
public class RoleAdminService {
	
	@Autowired
	private ClienteUtilidadeService clienteUtilidadeService;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private RoleMapper roleMapper;

	public void adicionarRole(AdicionarRoleDTO adicionarRoleDTO) {
		String nomeRole = adicionarRoleDTO.role();
		if (!nomeRole.equals("ADMIN")) {
			throw new ValidacaoException("Envie uma role válida! não existe a role " + nomeRole);
		}
		
		Cliente cliente = clienteUtilidadeService.verificarSeClienteExistePorId(adicionarRoleDTO.idCliente());
		Role role = roleMapper.stringRoleParaRoleEntity(nomeRole, cliente);
		cliente.adicionarRole(role);
		clienteRepository.save(cliente);
	}

	public void removerRole(Long idCliente, String nomeRole) {
		if (!nomeRole.equals("ADMIN")) {
			throw new ValidacaoException("Envie uma role válida! não existe a role " + nomeRole);
		}
		Cliente cliente = clienteUtilidadeService.verificarSeClienteExistePorId(idCliente);
		Role role = cliente.getRoles().stream()
						.filter(r -> r.getRole().toString().endsWith(nomeRole))
						.findFirst().orElseThrow(() -> new ValidacaoException("O cliente não possui a role de nome " + nomeRole));
		cliente.removerRole(role);
		clienteRepository.save(cliente);
	}
	
}
