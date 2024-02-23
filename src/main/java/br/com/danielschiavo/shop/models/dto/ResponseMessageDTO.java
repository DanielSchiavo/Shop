package br.com.danielschiavo.shop.models.dto;

public record ResponseMessageDTO(String message) {
	
	public ResponseMessageDTO(String message) {
		this.message = message;
	}

}
