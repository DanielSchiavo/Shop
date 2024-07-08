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

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ClienteMapper {
	
	@Mapping(source = "arquivoInfoDTO", target = "fotoPerfil")
    @Mapping(source = "cliente.nome", target = "nome")
	public abstract MostrarClientePaginaInicialResponse toPaginaInicialDto(Cliente cliente);

    @Mapping(source = "cliente.nome", target = "nome")
    public abstract MostrarClienteResponse toDto(Cliente cliente);
    
    @Mapping(target = "dataCriacaoConta", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "fotoPerfil", source = "cadastrarClienteDTO.fotoPerfil", defaultValue = "Padrao.jpeg")
    public abstract Cliente toEntity(CadastrarClienteRequest request);
    
    @BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    public abstract Cliente alterarCliente(AlterarClienteRequest alterarClienteDTO, @MappingTarget Cliente cliente);
    
}
