package br.com.danielschiavo.cliente.mapper;

import java.util.List;

import br.com.danielschiavo.cliente.dto.request.endereco.AlterarEnderecoRequest;
import br.com.danielschiavo.cliente.dto.request.endereco.CadastrarEnderecoRequest;
import br.com.danielschiavo.cliente.model.entity.Endereco;
import br.com.danielschiavo.cliente.dto.response.endereco.MostrarEnderecoResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(componentModel = "spring")
public interface EnderecoMapper {

	MostrarEnderecoResponse toDto(Endereco endereco);
	
	List<MostrarEnderecoResponse> toDto(List<Endereco> enderecos);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Endereco toEntity(CadastrarEnderecoRequest request);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Endereco toEntity(AlterarEnderecoRequest request);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	void alterarEndereco(Endereco enderecoAtualizado, @MappingTarget Endereco endereco);
}
