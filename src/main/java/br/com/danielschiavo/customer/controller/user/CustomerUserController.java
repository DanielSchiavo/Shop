package br.com.danielschiavo.customer.controller.user;


import br.com.danielschiavo.customer.dto.request.customer.UpdateCustomerRequest;
import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.dto.response.customer.DetailCustomerResponse;
import br.com.danielschiavo.customer.dto.response.customer.ShowCustomerHomePageResponse;
import br.com.danielschiavo.customer.service.customer.CustomerService;
import br.com.danielschiavo.filestorage.service.FileReferenceService;
import br.com.danielschiavo.shared.DetailFileResponse;
import br.com.danielschiavo.shared.FileMapper;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/user/customers")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - User", description = "All endpoints related to a Customer, in which only him can use")
public class CustomerUserController {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private FileReferenceService fileService;

	@Autowired
	private FileMapper fileMapper;

	@GetMapping("/home-page")
	@Operation(summary = "Show Customer data for home page")
	public ResponseEntity<?> getCustomerHomePage() {
		Long customerId = securityService.getCustomerId();
		ShowCustomerHomePageResponse customer = customerService.getCustomerForHomePageById(customerId);

		DetailFileResponse detailFileResponse = customer.profilePicture();
		String fileName = customer.profilePicture().getFileName();

		fileMapper.mapFilesToDto(List.of(detailFileResponse), CustomerService.awsS3Directory, Set.of(fileName));

		return ResponseEntity.ok(Response.success("Success in recovering customer data for home page", customer));
	}

	@GetMapping
	@Operation(summary = "Show all Customer data")
	public ResponseEntity<?> getCustomer() {
		Long customerId = securityService.getCustomerId();
		DetailCustomerResponse customer = customerService.getCustomerById(customerId);

		DetailFileResponse detailFileResponse = customer.profilePicture();
		String fileName = customer.profilePicture().getFileName();

		fileMapper.mapFilesToDto(List.of(detailFileResponse), CustomerService.awsS3Directory, Set.of(fileName));

		return ResponseEntity.ok(Response.success("Success in recovering customer data", customer));
	}
	
	@PostMapping("/register")
	@Operation(summary = "Register customer")
	public ResponseEntity<?> registerCustomer(@RequestBody @Valid RegisterCustomerRequest request) {
		boolean existsProfilePicture = fileService.fileExists(CustomerService.awsS3Directory, request.profilePicture());
		if (!existsProfilePicture) {
			throw new ValidationException("Could not found profile picture");
		}

		DetailCustomerResponse response = customerService.registerCustomer(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Customer registred successfully!", null));

	}
	
	@PutMapping
	@Operation(summary = "Customer update your own data")
	public ResponseEntity<?> updateCustomer(@RequestBody @Valid UpdateCustomerRequest request) {
		Long customerId = securityService.getCustomerId();

		DetailCustomerResponse response = customerService.updateCustomerById(customerId, request);
		return ResponseEntity.ok(Response.success("Customer updated successfully!", null));
	}
	
	@PatchMapping("/profile-picture/{newProfilePictureName}")
	@Operation(summary = "Customer update your profile picture")
	public ResponseEntity<?> updateProfilePicture(@PathVariable String newProfilePictureName) {
		Long customerId = securityService.getCustomerId();

		boolean existsProfilePicture = fileService.fileExists(CustomerService.awsS3Directory, newProfilePictureName);
		if (!existsProfilePicture) {
			throw new ValidationException("Could not found profile picture");
		}

		String oldProfilePicture = customerService.updateProfilePictureById(newProfilePictureName, customerId);

		fileService.delete(CustomerService.awsS3Directory, oldProfilePicture);

		return ResponseEntity.ok(Response.success("Profile picture updated successfully!", null));
	}
}
