package br.com.danielschiavo.pedido.service.pedido.validacoes.fazerpedido;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.pedido.model.entity.Pedido;
import br.com.danielschiavo.pedido.model.enums.TipoEntrega;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidarTipoEntrega implements ValidadorFazerPedido {

	@Override
	public void validar(Pedido pedido, Customer customer, Long cartaoId, Long enderecoId) {
		TipoEntrega tipoEntrega = pedido.getEntrega().getTipoEntrega();

		boolean tipoEntregaPrecisaDeEntrecoEOIdDoEnderecoEstaNulo = tipoEntrega.precisaDeEndereco() && enderecoId == null;

		if (tipoEntregaPrecisaDeEntrecoEOIdDoEnderecoEstaNulo) {
			throw new ValidacaoException("O tipo de entrega definido foi " + tipoEntrega + ", portanto, é necessário que envie o endereço do local de entrega.");
		}

		boolean tipoEntregaNaoPrecisaDeEnderecoEOIdDoEnderecoFoiEnviado = !tipoEntrega.precisaDeEndereco() && enderecoId != null;
		
		if (tipoEntregaNaoPrecisaDeEnderecoEOIdDoEnderecoFoiEnviado) {
			throw new ValidacaoException("O tipo de entrega definido foi " + tipoEntrega + ", portanto, não é necessário que envie o enderecoId.");
		}
		
	}

}
