package br.com.danielschiavo.shop.models.pedido;

public enum TipoEntrega {
	CORREIOS
	{
		@Override
		public boolean precisaDeEndereco() {
			return true;
		}
	},
 	ENTREGA_EXPRESSA
 	{
		@Override
		public boolean precisaDeEndereco() {
			return true;
		}
	},
 	RETIRADA_NA_LOJA
 	{
		@Override
		public boolean precisaDeEndereco() {
			return false;
		}
	},
 	ENTREGA_DIGITAL
 	{
		@Override
		public boolean precisaDeEndereco() {
			return false;
		}
	};
	
	public abstract boolean precisaDeEndereco();
}
