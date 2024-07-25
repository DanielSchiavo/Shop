package br.com.danielschiavo.customer.service.customer;

import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.dto.request.customer.UpdateCustomerRequest;
import br.com.danielschiavo.customer.dto.response.customer.DetailCustomerResponse;
import br.com.danielschiavo.customer.dto.response.customer.ShowCustomersResponse;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.model.enums.RoleName;
import br.com.danielschiavo.customer.repository.CustomerRepository;
import br.com.danielschiavo.customer.service.customer.validators.registercustomer.ValidatorRegisterCustomer;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.customer.mapper.CustomerMapper;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Setter
public class CustomerService {
	
	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private CustomerMapper mapper;

	@Autowired
	private List<ValidatorRegisterCustomer> validators;

	public static final String bucketName = "profile-picture", directory = "directory";


	@Transactional
	public String deleteProfilePictureById(Long customerId) {
		Customer customer = repository.findById(customerId)
				.orElseThrow(() -> new ValidationException("Could not delete profile picture, contact an administrator"));

		String oldProfilePicture = customer.getProfilePicture();
		customer.setProfilePicture("Default.jpeg");
		repository.save(customer);
		return oldProfilePicture;
	}

	public Page<ShowCustomersResponse> getAllCustomers(Pageable pageable) {
		Page<Customer> all = repository.findAll(pageable);

		List<ShowCustomersResponse> list = all.getContent().stream()
				.map(mapper::toShowCustomers).collect(Collectors.toList());

		return new PageImpl<>(list, pageable, all.getTotalElements());
	}

	public DetailCustomerResponse getCustomerForHomePageById(Long customerId) {
		Customer customer = repository.findByIdHomePage(customerId)
				.orElseThrow(() -> new ValidationException("There's no customer with id: " + customerId));
		return mapper.toDetailCustomer(customer);
	}
	
	public DetailCustomerResponse getCustomerById(Long customerId) {
		Customer customer = repository.findById(customerId)
				.orElseThrow(() -> new ValidationException("There's no customer with id: " + customerId));
		return mapper.toDetailCustomer(customer);
	}
	
	@Transactional
	public DetailCustomerResponse registerCustomer(RegisterCustomerRequest request) {
		validators.forEach(v -> v.validate(request));
		Customer customer = mapper.toEntity(request);
		return mapper.toDetailCustomer(repository.save(customer));
	}
	
	@Transactional
	public DetailCustomerResponse updateCustomerById(Long customerId, UpdateCustomerRequest request) {
		Customer customer = repository.findById(customerId)
				.orElseThrow(() -> new ValidationException("Could not update, contact an administrator"));
		mapper.updateCustomer(request, customer);
		
		return mapper.toDetailCustomer(repository.save(customer));
	}
	
	@Transactional
	public String updateProfilePictureById(String nameNewPicture, Long customerId) {
		Customer customer = repository.findById(customerId)
				.orElseThrow(() -> new ValidationException("Could not update profile picture, contact an administrator"));

		String oldProfilePicture = customer.getProfilePicture();
		customer.setProfilePicture(nameNewPicture);

		return oldProfilePicture;
	}

	public void addRole(Long custumerId, RoleName roleName) {
		Customer customer = repository.getReferenceById(custumerId);
		customer.adicionarRole(roleName);

		repository.save(customer);
	}

	public void removeRole(Long customerId, RoleName roleName) {
		Customer customer = repository.getReferenceById(customerId);
		customer.removerRole(roleName);

		repository.save(customer);
	}


//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
