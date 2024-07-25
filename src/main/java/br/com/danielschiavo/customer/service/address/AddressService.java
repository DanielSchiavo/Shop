package br.com.danielschiavo.customer.service.address;


import br.com.danielschiavo.customer.dto.request.address.RegisterAddressRequest;
import br.com.danielschiavo.customer.dto.request.address.UpdateAddressRequest;
import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.customer.dto.response.address.ShowAddressesResponse;
import br.com.danielschiavo.customer.model.entity.Address;
import br.com.danielschiavo.customer.repository.AddressRepository;
import br.com.danielschiavo.customer.mapper.AddressMapper;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;

import java.util.List;

@Service
@Setter
public class AddressService {
	
	@Autowired
	private AddressRepository repository;
	
	@Autowired
	private AddressMapper mapper;
	
	@Transactional
	public void deleteAddressById(Long addressId, Long customerId) {
		Address probe = new Address();
		probe.setId(addressId);
		probe.setCustomerId(customerId);
		Example<Address> example = Example.of(probe);
		if (!repository.exists(example)) {
			throw new ValidationException("Could not delete this address, contact an administrator");
		}
		repository.deleteByIdAndCustomerId(addressId, customerId);
	}
	
	public List<ShowAddressesResponse> getAllAddressesByCustomerId(Long customerId) {
		List<Address> addresses = repository.findAllByCustomerId(customerId)
				.orElseThrow(() -> new ValidationException("Customer doesn't have any registered Address"));

		return mapper.toListShowAddresses(addresses);
	}
	
	public DetailAddressResponse getAddressByIdAndCustomerId(Long addressId, Long customerId) {
		Address address = repository.findByIdAndCustomerId(addressId, customerId)
				.orElseThrow(() -> new ValidationException("Customer doesn't have a Address with id: " + addressId));

		return mapper.toDetailAddress(address);
	}
	
	@Transactional
	public DetailAddressResponse registerAddress(Long customerId, RegisterAddressRequest request) {
		Address registerAddress = mapper.toEntity(request);

		registerAddress.setCustomerId(customerId);

		defineOtherAddressAsIsDefaultFalse(customerId);

		return mapper.toDetailAddress(repository.save(registerAddress));
	}
	
	@Transactional
	public DetailAddressResponse updateAddress(Long addressId, Long customerId, UpdateAddressRequest request) {
		Address address = repository.findByIdAndCustomerId(addressId, customerId)
				.orElseThrow(() -> new ValidationException("Could not update this address, contact an administrator"));

		mapper.updateAddress(request, address);

		if (request.isDefault()) {
			defineOtherAddressAsIsDefaultFalse(customerId);
		}

		return mapper.toDetailAddress(repository.save(address));
	}

	public void defineOtherAddressAsIsDefaultFalse(Long customerId) {
		repository.findByCustomerIdAndIsDefault(customerId, true).ifPresent(address -> {
			address.setIsDefault(false);
			repository.save(address);
		});
	}
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
