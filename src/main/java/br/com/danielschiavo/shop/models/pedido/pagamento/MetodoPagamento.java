package br.com.danielschiavo.shop.models.pedido.pagamento;

public enum MetodoPagamento {
	CARTAO_CREDITO
	{
		@Override
		public boolean precisaDeCartao() {
			return true;
		}

		@Override
		public boolean podeParcelar() {
			return true;
		}
	},
 	CARTAO_DEBITO
 	{
		@Override
		public boolean precisaDeCartao() {
			return true;
		}

		@Override
		public boolean podeParcelar() {
			return false;
		}
	},
 	PIX
 	{
		@Override
		public boolean precisaDeCartao() {
			return false;
		}

		@Override
		public boolean podeParcelar() {
			return false;
		}
	},
 	BOLETO {
		@Override
		public boolean precisaDeCartao() {
			return false;
		}

		@Override
		public boolean podeParcelar() {
			return false;
		}
	};
	
	public abstract boolean precisaDeCartao();
	
	public abstract boolean podeParcelar();
}
