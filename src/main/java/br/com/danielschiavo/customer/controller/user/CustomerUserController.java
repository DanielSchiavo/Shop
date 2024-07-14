package br.com.danielschiavo.customer.controller.user;


import br.com.danielschiavo.customer.dto.request.customer.UpdateCustomerRequest;
import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.mapper.CustomerMapper;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.service.cliente.CustomerService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user/clientes")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - User", description = "All endpoints related to a Customer, in which only him can use")
public class CustomerUserController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private CustomerMapper mapper;
	
	@GetMapping("/home-page")
	@Operation(summary = "Show Customer data for home page")
	public ResponseEntity<?> getCustomerHomePage() {
		Long customerId = securityService.getCustomerId();
		Customer customer = customerService.getCustomerForHomePageById(customerId);
		
		return ResponseEntity.ok(Response.success("Success in recovering customer data for home page", mapper.toHomePage(customer)));
	}

	@GetMapping
	@Operation(summary = "Show all Customer data")
	public ResponseEntity<?> getCustomer() {
		Long customerId = securityService.getCustomerId();
		Customer customer = customerService.getCustomerById(customerId);

		return ResponseEntity.ok(Response.success("Success in recovering customer data", mapper.toDto(customer)));
	}
	
	@PostMapping("/register")
	@Operation(summary = "Register customer")
	public ResponseEntity<?> registerCustomer(@RequestBody @Valid RegisterCustomerRequest request) {
		Customer registerCustomer = mapper.toEntity(request);
		Customer customer = customerService.registerCustomer(registerCustomer);

		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Customer registred successfully!", null));

	}
	
	@PutMapping
	@Operation(summary = "Customer update your own data")
	public ResponseEntity<?> updateCustomer(@RequestBody @Valid UpdateCustomerRequest request) {
		Long customerId = securityService.getCustomerId();
		Customer updatedCustomer = mapper.toEntity(request);

		Customer customer = customerService.updateCustomerById(customerId, updatedCustomer);
		return ResponseEntity.ok(Response.success("Customer updated successfully!", null));
	}
	
	@PutMapping("/profile-picture")
	@Operation(summary = "Customer update your profile picture")
	public ResponseEntity<?> updateProfilePicture(@RequestPart(name = "picture", required = true) MultipartFile newPicture) {
		Long customerId = securityService.getCustomerId();
		Customer customer = customerService.updateProfilePictureById(newPicture, customerId);
		return ResponseEntity.ok(Response.success("Profile picture updated successfully!", null));
	}

	@DeleteMapping("/profile-picture")
	@Operation(summary = "Customer delete your profile picture")
	public ResponseEntity<?> deleteProfilePicture() {
		Long customerId = securityService.getCustomerId();
		customerService.deleteProfilePictureById(customerId);
		return ResponseEntity.ok().body(Response.success("Profile picture deleted successfully", null));
	}
}
