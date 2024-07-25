package br.com.danielschiavo.customer.mapper;

import java.util.List;

import br.com.danielschiavo.customer.dto.request.card.RegisterCardRequest;
import br.com.danielschiavo.customer.dto.response.address.DetailAddressResponse;
import br.com.danielschiavo.customer.dto.response.card.DetailCardResponse;
import br.com.danielschiavo.customer.model.entity.Card;
import br.com.danielschiavo.customer.dto.response.card.ShowCardResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;


@Mapper(componentModel = "spring")
public interface CardMapper {

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
	Card toEntity(RegisterCardRequest request, Long customerId);

	Card toEntity(DetailCardResponse request, Long customerId);

	DetailCardResponse toDetailCard(Card card);
	
	List<ShowCardResponse> toListShowCard(List<Card> cards);
}
