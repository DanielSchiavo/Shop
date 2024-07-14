package br.com.danielschiavo.customer.controller.admin;


import br.com.danielschiavo.customer.mapper.CustomerMapper;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.model.enums.RoleName;
import br.com.danielschiavo.customer.service.cliente.CustomerService;
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
@Tag(name = "Customer - Admin", description = "All endpoints related to a Customer that only Administrators can use")
public class CustomerAdminController {

    @Autowired
    private CustomerService service;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private CustomerMapper mapper;

    @GetMapping("/{id}")
    @Operation(summary = "Show all the Customer's data")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Customer customer = service.getCustomerById(id);

        return ResponseEntity.ok(Response.success("Success in recovering customer's data", mapper.toDto(customer)));
    }

	@GetMapping
	@Operation(summary = "Show all registred customers")
	public ResponseEntity<?> getAllCustomers(Pageable pageable) {
		var clientes = service.getAllCustomers(pageable);
		return ResponseEntity.ok(Response.success("Success in recovering all registred customers", clientes));
	}

    @PostMapping("/{customerId}/roles/{roleName}")
    @Operation(summary = "Add role from a registred customer")
    public ResponseEntity<?> addRole(@PathVariable Long customerId, @PathVariable RoleName roleName) {
        service.addRole(customerId, roleName);
        return ResponseEntity.ok(Response.success("Role added successfully", null));
    }

    @DeleteMapping("/{customerId}/roles/{roleName}")
    @Operation(summary = "Remove role from a registred customer")
    public ResponseEntity<?> removeRole(@PathVariable Long customerId, @PathVariable RoleName roleName) {
        service.removeRole(customerId, roleName);
        return ResponseEntity.ok().body(Response.success("Role removed successfully", null));
    }

}
