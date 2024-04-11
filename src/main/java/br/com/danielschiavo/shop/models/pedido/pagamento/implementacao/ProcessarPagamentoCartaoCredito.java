package br.com.danielschiavo.shop.models.pedido.pagamento.implementacao;

import br.com.danielschiavo.shop.models.pedido.pagamento.ProcessadorPagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcessarPagamentoCartaoCredito extends ProcessadorPagamento {

	@Override
	public boolean executa() {
		System.out.println("Processando pagamento no cartão de crédito para o cliente " + super.getCliente().getNome() + " ele comprou " + super.getCriarPedidoDTO().items().size() + " items");
		return true;
	}

}
