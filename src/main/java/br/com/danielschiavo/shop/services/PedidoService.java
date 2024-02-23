package br.com.danielschiavo.shop.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.carrinho.ItemCarrinhoDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.pedido.ItemPedido;
import br.com.danielschiavo.shop.models.pedido.MostrarPedidosAConfirmarDTO;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.pedido.StatusPedido;
import br.com.danielschiavo.shop.models.produto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.repositories.PedidoRepository;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private TokenJWTService tokenJWTService;
	
	public Produto validarProduto(Long produtoId) {
		Produto produto = produtoService.verificarId(produtoId);

		if (produto.getAtivo() == false) {
			throw new RuntimeException("O produto de nome: " + produto.getNome() + " não se encontra ativo nesse momento.");
		}
		
		return produto;
	}

	public void createOrder(Cliente cliente, Endereco enderecoDeEntrega, List<ItemCarrinhoDTO> itensCarrinhoDTO) {
		Pedido pedido = new Pedido(cliente, enderecoDeEntrega);
		itensCarrinhoDTO.forEach(itemCarrinhoDTO -> {
			Produto produto = this.validarProduto(itemCarrinhoDTO.produto_id());
			
			ItemPedido itemPedido = new ItemPedido(produto, itemCarrinhoDTO);
			pedido.getItemsPedido().add(itemPedido);
		});
	}

	public Page<MostrarPedidosAConfirmarDTO> pegarPedidosAConfirmar(Pageable pageable) {
		Page<Pedido> pedidosAguardandoConfirmacao = pedidoRepository.findAllByStatusPedido(StatusPedido.A_CONFIRMAR, pageable);
		
		List<MostrarPedidosAConfirmarDTO> list = new ArrayList<>();
		
		for (Pedido pedido : pedidosAguardandoConfirmacao) {
			var mostrarPedidosAConfirmarDTO = new MostrarPedidosAConfirmarDTO(pedido);
			list.add(mostrarPedidosAConfirmarDTO);
		    }
		return new PageImpl<>(list, pedidosAguardandoConfirmacao.getPageable(), pedidosAguardandoConfirmacao.getTotalElements());
	}

	public List<Pedido> pegarPedidosPeloIdDoCliente(Long id) {
		return pedidoRepository.findAllByClienteIdOrderByDataPedidoAsc(id);
	}
	
}
