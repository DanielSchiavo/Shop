package br.com.danielschiavo.customer.dto.response.customer;

import br.com.danielschiavo.shared.DetailFileResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ShowCustomerHomePageResponse (
		String name,
		@JsonProperty("profile_picture")
		DetailFileResponse profilePicture
) {

}
