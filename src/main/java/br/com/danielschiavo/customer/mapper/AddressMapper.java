package br.com.danielschiavo.customer.mapper;

import java.util.List;

import br.com.danielschiavo.customer.dto.request.address.UpdateAddressRequest;
import br.com.danielschiavo.customer.dto.request.address.RegisterAddressRequest;
import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.customer.dto.response.address.ShowAddressesResponse;
import br.com.danielschiavo.customer.model.entity.Address;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(componentModel = "spring")
public interface AddressMapper {

	DetailAddressResponse toDetailAddress(Address address);
	
	List<ShowAddressesResponse> toListShowAddresses(List<Address> addresses);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Address toEntity(RegisterAddressRequest request);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Address toEntity(DetailAddressResponse response);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	void updateAddress(UpdateAddressRequest request, @MappingTarget Address address);
}
