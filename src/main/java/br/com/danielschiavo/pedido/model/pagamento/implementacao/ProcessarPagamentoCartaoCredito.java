package br.com.danielschiavo.pedido.model.pagamento.implementacao;

import br.com.danielschiavo.pedido.model.pagamento.ProcessadorPagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProcessarPagamentoCartaoCredito extends ProcessadorPagamento {

	@Override
	public boolean executa() {
		System.out.println("Processando pagamento no cartão de crédito para o cliente " + super.getCliente().getNome() + " ele comprou R$" + super.getValorTotal() + " em produtos");
		return true;
	}

}
