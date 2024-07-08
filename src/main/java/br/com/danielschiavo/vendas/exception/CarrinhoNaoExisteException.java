package br.com.danielschiavo.vendas.exception;

public class CarrinhoNaoExisteException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public CarrinhoNaoExisteException(String message) {
        super(message);
    }

}
