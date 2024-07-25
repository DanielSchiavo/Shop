package br.com.danielschiavo.customer.dto.response.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ShowCustomerHomePageResponse (
		String name,
		@JsonProperty("profile_picture")
		String profilePicture
) {

}
