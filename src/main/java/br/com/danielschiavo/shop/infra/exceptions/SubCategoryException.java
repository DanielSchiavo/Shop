package br.com.danielschiavo.shop.infra.exceptions;

public class SubCategoryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SubCategoryException(String message) {
        super(message);
    }

    public SubCategoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
