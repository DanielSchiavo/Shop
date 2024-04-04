package br.com.danielschiavo.shop.mapper.pedido;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;

import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.dto.MostrarProdutoDoPedidoDTO;
import br.com.danielschiavo.shop.services.filestorage.FileStoragePedidoService;

@Mapper(componentModel = "spring")
public abstract class PedidoMapper {

	public List<MostrarProdutoDoPedidoDTO> pedidoParaMostrarProdutoDoPedidoDTO(Pedido pedido, @Context FileStoragePedidoService fileService) {
		List<MostrarProdutoDoPedidoDTO> lista = new ArrayList<>();
		pedido.getItemsPedido().forEach(itemPedido -> {
			ArquivoInfoDTO arquivoInfoDTO = fileService.pegarImagemPedidoPorNome(itemPedido.getPrimeiraImagem());
			lista.add(new MostrarProdutoDoPedidoDTO(itemPedido, arquivoInfoDTO.bytesArquivo()));
		});
		return lista;
	}
		
}
