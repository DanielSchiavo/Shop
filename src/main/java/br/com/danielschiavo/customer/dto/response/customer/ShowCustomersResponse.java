package br.com.danielschiavo.customer.dto.response.customer;

import br.com.danielschiavo.shared.DetailFileResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ShowCustomersResponse(
        Long id,
        String name,
        String surname,
        @JsonProperty("profile_picture")
        DetailFileResponse profilePicture,
        @JsonProperty("account_creation_date")
        LocalDate accountCreationDate
) {
}
