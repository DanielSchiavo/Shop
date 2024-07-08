package br.com.danielschiavo.cliente.controller.admin;

import br.com.danielschiavo.cliente.dto.request.RoleDTO;
import br.com.danielschiavo.cliente.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente - Admin Roles", description = "Todos endpoints relacionados com a role dos clientes para uso exclusivo dos administradores")
public class RoleAdminController {
	
	@Autowired
	private RoleService roleService;
	
	@PostMapping("/admin/role")
	@Operation(summary = "Adiciona role de um cliente cadastrado")
	public ResponseEntity<?> adicionarRole(@RequestBody @Valid RoleDTO adicionarRoleDTO) {
		try {
			String respostaAdicionarRole = roleService.adicionarRole(adicionarRoleDTO);
			return ResponseEntity.ok(respostaAdicionarRole);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@DeleteMapping("/admin/role")
	@Operation(summary = "Remove role de um cliente cadastrado")
	public ResponseEntity<?> removerRoleDoCliente(@RequestBody @Valid RoleDTO removerRoleDTO) {
		try {
			String respostaRemoverRole = roleService.removerRole(removerRoleDTO);
			return ResponseEntity.ok().body(respostaRemoverRole);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
