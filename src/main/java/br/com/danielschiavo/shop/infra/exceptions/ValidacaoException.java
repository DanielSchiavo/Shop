package br.com.danielschiavo.shop.infra.exceptions;

public class ValidacaoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidacaoException(String message) {
        super(message);
    }

    public ValidacaoException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

