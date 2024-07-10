package br.com.danielschiavo.cliente.controller.admin;


import br.com.danielschiavo.cliente.mapper.ClienteMapper;
import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.enums.NomeRole;
import br.com.danielschiavo.cliente.service.cliente.ClienteService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/clientes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Cliente - Admin", description = "Todos endpoints relacionados com o cliente para uso exclusivo dos administradores")
public class ClienteAdminController {

    @Autowired
    private ClienteService service;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private ClienteMapper mapper;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{id}")
    @Operation(summary = "Mostra todos os dados do cliente")
    public ResponseEntity<?> pegarClientePorId(@PathVariable Long id) {
        Cliente cliente = service.pegarClientePorId(id);

        return ResponseEntity.ok(Response.success("Sucesso ao recuperar dados do cliente", mapper.toDto(cliente)));
    }

	@GetMapping
	@Operation(summary = "Mostra todos os clientes cadastrados")
	public ResponseEntity<?> adminDetalharTodosClientes(Pageable pageable) {
		var clientes = service.pegarTodosClientes(pageable);
		return ResponseEntity.ok(Response.success("Sucesso ao recuperar todos clientes cadastrados", clientes));
	}

    @PostMapping("/{clienteId}/roles/{nomeRole}")
    @Operation(summary = "Adiciona role de um cliente cadastrado")
    public ResponseEntity<?> adicionarRole(@PathVariable Long clienteId, @PathVariable NomeRole nomeRole) {
        clienteService.adicionarRole(clienteId, nomeRole);
        return ResponseEntity.ok(Response.success("Permissão concedida ao usuário com sucesso", null));
    }

    @DeleteMapping("/{clienteId}/roles/{nomeRole}")
    @Operation(summary = "Remove role de um cliente cadastrado")
    public ResponseEntity<?> removerRoleDoCliente(@PathVariable Long clienteId, @PathVariable NomeRole nomeRole) {
        clienteService.removerRole(clienteId, nomeRole);
        return ResponseEntity.ok().body(Response.success("Permissão do usuário removida com sucesso", null));
    }

}
