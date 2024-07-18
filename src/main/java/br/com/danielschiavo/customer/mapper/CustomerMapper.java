package br.com.danielschiavo.customer.mapper;


import br.com.danielschiavo.customer.dto.request.customer.UpdateCustomerRequest;
import br.com.danielschiavo.customer.dto.request.customer.RegisterCustomerRequest;
import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.dto.response.customer.ShowCustomerResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import br.com.danielschiavo.customer.dto.response.customer.ShowCustomerHomePageResponse;

import java.time.LocalDate;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {LocalDate.class})
public interface CustomerMapper {
	
	ShowCustomerHomePageResponse toHomePage(Customer customer);

    @Mapping(source = "customer.name", target = "name")
    ShowCustomerResponse toDto(Customer customer);
    
    @Mapping(target = "accountCreationDate", expression = "java(LocalDate.now())")
    @Mapping(target = "profilePicture", source = "request.profilePicture", defaultValue = "Default.jpeg")
    Customer toEntity(RegisterCustomerRequest request);

    Customer toEntity(UpdateCustomerRequest request);
    
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "authorities", ignore = true)
    void updateCustomer(Customer updatedCustomer, @MappingTarget Customer customer);
    
}
