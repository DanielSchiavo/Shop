package br.com.danielschiavo.customer.controller.user;


import java.util.List;

import br.com.danielschiavo.customer.dto.request.address.UpdateAddressRequest;
import br.com.danielschiavo.customer.dto.request.address.RegisterAddressRequest;
import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.customer.dto.response.address.ShowAddressesResponse;
import br.com.danielschiavo.customer.mapper.AddressMapper;
import br.com.danielschiavo.customer.model.entity.Address;
import br.com.danielschiavo.customer.service.address.AddressService;
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

@RestController
@RequestMapping("/user/customers/addresses")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Customer - Address", description = "All endpoints related to addresses of a customer, in which only him can use")
public class AddressController {

	@Autowired
	private AddressService service;

	@Autowired
	private SecurityService securityService;

	@DeleteMapping("/{addressId}")
	@Operation(summary = "Delete an Address by id")
	public ResponseEntity<?> deleteAddress(@PathVariable Long addressId) {
		Long customerId = securityService.getCustomerId();
		service.deleteAddressById(addressId, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Address deleted successfully!", null));
	}
	
	@GetMapping
	@Operation(summary = "Get all Customer addresses")
	public ResponseEntity<?> getAllCustomerAddresses() {
		Long customerId = securityService.getCustomerId();
		List<ShowAddressesResponse> addresses = service.getAllAddressesByCustomerId(customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Success recovering addresses", addresses));
	}
	
	@GetMapping("/{addressId}")
	@Operation(summary = "Get a specific customer address with provided id")
	public ResponseEntity<?> getAddressById(@PathVariable Long addressId) {
		Long customerId = securityService.getCustomerId();
		DetailAddressResponse response = service.getAddressByIdAndCustomerId(addressId, customerId);
		return ResponseEntity.status(HttpStatus.OK).body(Response.success("Success recovering address",response));
	}
	
	@PostMapping
	@Operation(summary = "Register a new Address for a Customer")
	public ResponseEntity<?> registerAddress(@RequestBody @Valid RegisterAddressRequest request) {
		Long customerId = securityService.getCustomerId();

		DetailAddressResponse response = service.registerAddress(customerId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(Response.success("Address registered successfully!", null));
	}
	
	@PutMapping("/{addressId}")
	@Operation(summary = "Update a customer address by id")
	public ResponseEntity<?> updateAddress(@PathVariable Long addressId, @RequestBody UpdateAddressRequest request) {
		Long customerId = securityService.getCustomerId();

		DetailAddressResponse response = service.updateAddress(customerId, addressId, request);
		return ResponseEntity.ok().body(Response.success("Address updated successfully!", null));
	}
	
}
