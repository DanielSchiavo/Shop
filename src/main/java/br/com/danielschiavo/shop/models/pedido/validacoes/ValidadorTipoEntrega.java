package br.com.danielschiavo.shop.models.pedido.validacoes;

import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.pedido.CriarPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;

@Service
public class ValidadorTipoEntrega implements ValidadorCriarNovoPedido {

	@Override
	public void validar(CriarPedidoDTO pedidoDTO) {
		TipoEntrega tipoEntregaDTO = pedidoDTO.entrega().tipoEntrega();
		Long idEnderecoDTO = pedidoDTO.entrega().idEndereco();
		if (tipoEntregaDTO != TipoEntrega.ENTREGA_DIGITAL && tipoEntregaDTO != TipoEntrega.RETIRADA_NA_LOJA && idEnderecoDTO == null) {
			System.out.println(" TESTE " + idEnderecoDTO);
			throw new ValidacaoException("O tipo de entrega definido foi " + tipoEntregaDTO + ", portanto, é necessário que envie o endereço do local de entrega.");
		}
	}

}
