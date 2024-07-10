package br.com.danielschiavo.cliente.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.dto.response.cartao.MostrarCartaoResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(componentModel = "spring")
public interface CartaoMapper {

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Cartao toEntity(CadastrarCartaoRequest request, Long clienteId);

	MostrarCartaoResponse toDto(Cartao cartao);
	
	List<MostrarCartaoResponse> toDto(List<Cartao> cartoes);
}
