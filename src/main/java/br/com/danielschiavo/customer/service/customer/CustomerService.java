package br.com.danielschiavo.customer.service.customer;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.model.enums.RoleName;
import br.com.danielschiavo.customer.repository.CustomerRepository;
import br.com.danielschiavo.customer.service.customer.validators.registercustomer.ValidatorRegisterCustomer;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.service.FileService;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.customer.mapper.CustomerMapper;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Setter
public class CustomerService {
	
	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private CustomerMapper mapper;
	
	@Autowired
	private FileService fileService;

	@Autowired
	private List<ValidatorRegisterCustomer> validators;

	private static final String bucketName = "profile-picture";


	@Transactional
	public void deleteProfilePictureById(Long clienteId) {
		Customer customer = repository.getReferenceById(clienteId);
		fileService.deleteFile(bucketName, customer.getProfilePicture());

		customer.setProfilePicture("Default.jpeg");
		repository.save(customer);
	}

	public Page<Customer> getAllCustomers(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Customer getCustomerForHomePageById(Long customerId) {
		return repository.findByIdHomePage(customerId)
				.orElseThrow(() -> new ValidationException("There's no customer with id: " + customerId));
	}
	
	public Customer getCustomerById(Long customerId) {
		return repository.findById(customerId)
				.orElseThrow(() -> new ValidationException("There's no customer with id: " + customerId));
	}
	
	@Transactional
	public Customer registerCustomer(Customer customer) {
		validators.forEach(v -> v.validate(customer));
		return repository.save(customer);
	}
	
	@Transactional
	public Customer updateCustomerById(Long customerId, Customer updatedCustomer) {
		Customer customer = repository.getReferenceById(customerId);
		mapper.updateCustomer(updatedCustomer, customer);
		
		return repository.save(customer);
	}
	
	@Transactional
	public Customer updateProfilePictureById(String nameNewPicture, Long customerId) {
		Customer customer = getCustomerById(customerId);

		fileService.deleteFile(bucketName, customer.getProfilePicture());

		customer.setProfilePicture(nameNewPicture);
		return repository.save(customer);
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
