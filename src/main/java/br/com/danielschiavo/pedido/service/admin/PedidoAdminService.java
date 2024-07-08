package br.com.danielschiavo.pedido.service.admin;

import java.util.ArrayList;
import java.util.List;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.service.ClienteService;
import br.com.danielschiavo.filestorage.service.FileStoragePedidoService;
import br.com.danielschiavo.pedido.model.MostrarPedidoResponse;
import br.com.danielschiavo.pedido.model.MostrarProdutoDoPedidoResponse;
import br.com.danielschiavo.pedido.model.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.danielschiavo.pedido.repository.admin.PedidoRepository;

import br.com.danielschiavo.shared.infra.security.SecurityService;
import lombok.Setter;

@Service
@Setter	
public class PedidoAdminService {

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private FileStoragePedidoService fileStoragePedidoService;
	
	@Autowired
	private PedidoComumMapper pedidoMapper;

	public Page<MostrarPedidoResponse> pegarPedidosClientePorId(Long id, Pageable pageable) {
		Cliente cliente = clienteService.pegarClientePorId(id);
		String tokenComBearer = securityService.getTokenComBearer();

		Page<Pedido> pagePedidos = pedidoRepository.findAllByCliente(cliente, pageable);
		List<MostrarPedidoResponse> list = new ArrayList<>();

		for (Pedido pedido : pagePedidos) {
			List<MostrarProdutoDoPedidoResponse> listaMostrarProdutoDoPedidoDTO = pedidoMapper.pedidoParaMostrarProdutoDoPedidoDTO(pedido, fileStoragePedidoService);

			var mostrarPedidoDTO = new MostrarPedidoResponse(pedido, listaMostrarProdutoDoPedidoDTO);
			list.add(mostrarPedidoDTO);
		}
		return new PageImpl<>(list, pagePedidos.getPageable(),
				pagePedidos.getTotalElements());
	}
	

//	------------------------------
//	------------------------------
//	METODOS UTILITÁRIOS
//	------------------------------
//	------------------------------
	
}
