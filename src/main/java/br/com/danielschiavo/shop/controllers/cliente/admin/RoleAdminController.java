package br.com.danielschiavo.shop.controllers.cliente.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielschiavo.shop.models.cliente.role.AdicionarRoleDTO;
import br.com.danielschiavo.shop.services.cliente.admin.RoleAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/shop")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Role", description = "Todos endpoints relacionados com a role do cliente")
public class RoleAdminController {
	
	@Autowired
	private RoleAdminService roleAdminService;
	
	@PostMapping("/admin/cliente")
	@Operation(summary = "Adiciona role a um cliente cadastrado")
	public ResponseEntity<?> adicionarRole(@RequestBody @Valid AdicionarRoleDTO adicionarRoleDTO) {
		roleAdminService.adicionarRole(adicionarRoleDTO);
		return ResponseEntity.ok("Promovido com sucesso");
	}
	
	@DeleteMapping("/admin/cliente/{idCliente}/role/{nomeRole}")
	public ResponseEntity<?> removerRoleDoCliente(@PathVariable Long idCliente, @PathVariable String nomeRole) {
	    roleAdminService.removerRole(idCliente, nomeRole);
		return ResponseEntity.ok().build();
	}

}
