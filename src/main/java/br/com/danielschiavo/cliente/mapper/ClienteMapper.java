package br.com.danielschiavo.cliente.mapper;


import br.com.danielschiavo.cliente.dto.request.cliente.AlterarClienteRequest;
import br.com.danielschiavo.cliente.dto.request.cliente.CadastrarClienteRequest;
import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.dto.response.cliente.MostrarClienteResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

import br.com.danielschiavo.cliente.dto.response.cliente.MostrarClientePaginaInicialResponse;

import java.time.LocalDate;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {LocalDate.class})
public interface ClienteMapper {
	
	MostrarClientePaginaInicialResponse toPaginaInicialDto(Cliente cliente);

    @Mapping(source = "cliente.nome", target = "nome")
    MostrarClienteResponse toDto(Cliente cliente);
    
    @Mapping(target = "dataCriacaoConta", expression = "java(LocalDate.now())")
    @Mapping(target = "fotoPerfil", source = "request.fotoPerfil", defaultValue = "Padrao.jpeg")
    Cliente toEntity(CadastrarClienteRequest request);
    
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void alterarCliente(AlterarClienteRequest alterarClienteDTO, @MappingTarget Cliente cliente);
    
}
