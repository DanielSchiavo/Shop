package br.com.danielschiavo.shop.infra.exceptions;

import org.springframework.http.HttpStatus;


public record MensagemErroDTO(String erro, String mensagem) {
	
    public MensagemErroDTO(HttpStatus status, RuntimeException e) {
        this(status.toString(), e.getMessage());
    }
}