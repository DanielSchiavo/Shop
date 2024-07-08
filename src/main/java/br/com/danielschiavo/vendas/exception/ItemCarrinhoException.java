package br.com.danielschiavo.vendas.exception;

public class ItemCarrinhoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ItemCarrinhoException(String mensagem) {
		super(mensagem);
	}

}
