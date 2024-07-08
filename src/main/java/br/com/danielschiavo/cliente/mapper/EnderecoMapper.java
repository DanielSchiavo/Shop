package br.com.danielschiavo.cliente.mapper;

import java.util.ArrayList;
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
public abstract class EnderecoMapper {

	public abstract MostrarEnderecoResponse toDto(Endereco endereco);
	
	public List<MostrarEnderecoResponse> toDto(List<Endereco> enderecos){
		List<MostrarEnderecoResponse> listaMostrarEnderecoDTO = new ArrayList<>();
		enderecos.forEach(endereco -> {
			MostrarEnderecoResponse mostrarEnderecoDTO = toDto(endereco);
			listaMostrarEnderecoDTO.add(mostrarEnderecoDTO);
		});
		return listaMostrarEnderecoDTO;
	}

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	public abstract Endereco toEntity(CadastrarEnderecoRequest request);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	public abstract void alterarEnderecoDtoParaEndereco(AlterarEnderecoRequest request, @MappingTarget Endereco endereco);
}
