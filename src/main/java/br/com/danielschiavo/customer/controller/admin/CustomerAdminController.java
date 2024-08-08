package br.com.danielschiavo.customer.controller.admin;


import br.com.danielschiavo.customer.dto.response.customer.DetailCustomerResponse;
import br.com.danielschiavo.customer.dto.response.customer.ShowCustomersResponse;
import br.com.danielschiavo.customer.mapper.CustomerMapper;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.model.enums.RoleName;
import br.com.danielschiavo.customer.service.customer.CustomerService;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.DetailFileResponse;
import br.com.danielschiavo.shared.FileMapper;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/customers")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - Admin", description = "All endpoints related to a Customer that only Administrators can use")
public class CustomerAdminController {

    @Autowired
    private CustomerService service;

    @Autowired
    private FileMapper fileMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Show all the Customer's data")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        DetailCustomerResponse customer = service.getCustomerById(id);

        DetailFileResponse detailFileResponse = customer.profilePicture();
        String fileName = customer.profilePicture().getFileName();

        fileMapper.mapFilesToDto(List.of(detailFileResponse), CustomerService.awsS3Directory, Set.of(fileName));

        return ResponseEntity.ok(Response.success("Success in recovering customer's data", customer));
    }

	@GetMapping
	@Operation(summary = "Show all registred customers")
	public ResponseEntity<?> getAllCustomers(Pageable pageable) {
		var customers = service.getAllCustomers(pageable);

        List<DetailFileResponse> detailFileResponses = customers.stream().map(ShowCustomersResponse::profilePicture).toList();

        Set<String> fileNames = customers.stream().map(c -> c.profilePicture().getFileName()).collect(Collectors.toSet());

        fileMapper.mapFilesToDto(detailFileResponses, CustomerService.awsS3Directory, fileNames);

        return ResponseEntity.ok(Response.success("Success in recovering all registred customers", new PageImpl<>(customers, pageable, customers.size())));
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
