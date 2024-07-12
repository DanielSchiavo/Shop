package br.com.danielschiavo.pedido.service.pagamento.processador;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProcessarPagamentoCartaoCredito extends ProcessadorPagamento {

	@Override
	public boolean executa(Cliente cliente, BigDecimal valorTotal) {
		System.out.println("Processando pagamento no cartão de crédito para o cliente " + cliente.getNome() + " ele comprou R$" + valorTotal + " em produtos");
		return true;
	}

}
