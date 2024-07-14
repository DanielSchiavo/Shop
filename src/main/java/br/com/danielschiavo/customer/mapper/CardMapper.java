package br.com.danielschiavo.customer.mapper;

import java.util.List;

import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.dto.response.card.MostrarCartaoResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(componentModel = "spring")
public interface CardMapper {

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Card toEntity(RegisterCardRequest request, Long clienteId);

	MostrarCartaoResponse toDto(Card card);
	
	List<MostrarCartaoResponse> toDto(List<Card> cartoes);
}
