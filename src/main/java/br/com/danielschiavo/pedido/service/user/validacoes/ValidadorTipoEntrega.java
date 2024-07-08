package br.com.danielschiavo.pedido.service.user.validacoes;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.pedido.model.FazerPedidoRequest;
import br.com.danielschiavo.pedido.model.TipoEntrega;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.stereotype.Service;


@Service
public class ValidadorTipoEntrega implements ValidadorCriarNovoPedido {

	@Override
	public void validar(FazerPedidoRequest pedidoDTO, Cliente cliente) {
		TipoEntrega tipoEntregaDTO = pedidoDTO.entrega().tipoEntrega();
		Long idEnderecoDTO = pedidoDTO.entrega().enderecoId();
		
		if (tipoEntregaDTO.precisaDeEndereco() && idEnderecoDTO == null) {
			throw new ValidacaoException("O tipo de entrega definido foi " + tipoEntregaDTO + ", portanto, é necessário que envie o endereço do local de entrega.");
		}
		
		if (!tipoEntregaDTO.precisaDeEndereco() && idEnderecoDTO != null) {
			throw new ValidacaoException("O tipo de entrega definido foi " + tipoEntregaDTO + ", portanto, não é necessário que envie o enderecoId.");
		}
		
	}

}
