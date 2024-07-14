package br.com.danielschiavo.customer.service.endereco;


import br.com.danielschiavo.customer.model.entity.Address;
import br.com.danielschiavo.customer.repository.AddressRepository;
import br.com.danielschiavo.customer.mapper.AddressMapper;
import br.com.danielschiavo.shared.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
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
		repository.deleteByIdAndCustomerId(addressId, customerId);
	}
	
	public List<Address> getAllAddressesByCustomerId(Long customerId) {
		return repository.findAllByCustomerId(customerId)
				.orElseThrow(() -> new ValidationException("Customer doesn't have any registered Address"));
	}
	
	public Address getAddressByIdAndCustomerId(Long addressId, Long customerId) {
		return repository.findByIdAndCustomerId(addressId, customerId)
				.orElseThrow(() -> new ValidationException("Customer doesn't have a Address with id: " + addressId));
	}
	
	@Transactional
	public Address registerAddress(Long customerId, Address address) {
		address.setCustomerId(customerId);

		List<Address> addresses = getAllAddressesByCustomerId(customerId);

		if (address.getIsDefault()) {
			addresses.stream().filter(end -> end.getIsDefault().equals(true)).forEach(end -> end.setIsDefault(false));
		}

		addresses.add(address);
		repository.saveAll(addresses);
		return address;
	}
	
	@Transactional
	public Address updateAddress(Long addressId, Long customerId, Address updatedAddress) {
		List<Address> allAddresses = getAllAddressesByCustomerId(customerId);
		
		Address address = allAddresses.stream().filter(end -> end.getId().equals(addressId))
				.findFirst().orElseThrow(() -> new ValidationException("Address can't be updated because Customer doesn't have any registered Address with id: " + addressId));

		mapper.updateAddress(updatedAddress, address);

		if (updatedAddress.getIsDefault()) {
			allAddresses.stream().filter(end -> end.getIsDefault().equals(true) && !end.getId().equals(addressId))
					.forEach(end -> end.setIsDefault(false));
		}

		repository.saveAll(allAddresses);
		return address;
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
