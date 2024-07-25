package br.com.danielschiavo.customer.dto.response.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record ShowCustomersResponse(
        Long id,
        String name,
        String surname,
        @JsonProperty("profile_picture")
        String profilePicture,
        @JsonProperty("account_creation_date")
        LocalDate accountCreationDate
) {
}
