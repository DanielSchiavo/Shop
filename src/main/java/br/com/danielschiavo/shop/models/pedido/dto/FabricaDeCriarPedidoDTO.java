package br.com.danielschiavo.shop.models.pedido.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.pedido.entrega.CriarEntregaDTO;
import br.com.danielschiavo.shop.models.pedido.itempedido.AdicionarItemPedidoDTO;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;

public class FabricaDeCriarPedidoDTO {
	
	private boolean estaConstruindoNovoCriarPedidoDTO;
	private List<CriarPedidoDTO> listaCriarPedidoDTO = new ArrayList<>();
	
	private MetodoPagamento metodoPagamento;
	private Long cartaoId = null;
	private String numeroParcelas = null;
	private TipoEntrega tipoEntrega;
	private Long enderecoId = null;
	private Boolean veioPeloCarrinho;
	private List<AdicionarItemPedidoDTO> items = new ArrayList<>();
	
	public FabricaDeCriarPedidoDTO iniciarNovoPedido() {
	    if (estaConstruindoNovoCriarPedidoDTO) {
	        finalizarPedidoAtual();
	    }
	    estaConstruindoNovoCriarPedidoDTO = true;
	    return this;
	}
	
	public FabricaDeCriarPedidoDTO comMetodoPagamento(MetodoPagamento metodoPagamento) {
		this.metodoPagamento = metodoPagamento;
		return this;
	}

	public FabricaDeCriarPedidoDTO comCartaoENumeroParcelas(Long cartaoId, String numeroParcelas) {
		this.cartaoId = cartaoId;
		this.numeroParcelas = numeroParcelas;
		return this;
	}
	
	public FabricaDeCriarPedidoDTO comTipoEntrega(TipoEntrega tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
		return this;
	}
	
	public FabricaDeCriarPedidoDTO comEndereco(Long enderecoId) {
		this.enderecoId = enderecoId;
		return this;
	}
	
	public FabricaDeCriarPedidoDTO veioPeloCarrinho(boolean veioPeloCarrinho) {
		this.veioPeloCarrinho = veioPeloCarrinho;
		return this;
	}
	
	public FabricaDeCriarPedidoDTO comItem(Long produtoId, Integer quantidade) {
		this.items.add(new AdicionarItemPedidoDTO(produtoId, quantidade));
		return this;
	}
	
	public List<CriarPedidoDTO> getPedidos() {
		if (this.estaConstruindoNovoCriarPedidoDTO == true) {
			finalizarPedidoAtual();
		}
		return listaCriarPedidoDTO;
	}
	
	public CriarPedidoDTO getPedido() {
		if (this.estaConstruindoNovoCriarPedidoDTO == true) {
			CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(this.metodoPagamento, this.cartaoId, this.numeroParcelas);
			CriarEntregaDTO criarEntregaDTO = new CriarEntregaDTO(this.tipoEntrega, this.enderecoId);
			List<AdicionarItemPedidoDTO> copiaItems = new ArrayList<>(items);
			CriarPedidoDTO criarPedidoDTO = new CriarPedidoDTO(criarPagamentoDTO, criarEntregaDTO, this.veioPeloCarrinho, copiaItems);
			limparAtributos();
			this.estaConstruindoNovoCriarPedidoDTO = false;
			return criarPedidoDTO;
		}
		return null;
	}
	
	private void finalizarPedidoAtual() {
		CriarPagamentoDTO criarPagamentoDTO = new CriarPagamentoDTO(this.metodoPagamento, this.cartaoId, this.numeroParcelas);
		CriarEntregaDTO criarEntregaDTO = new CriarEntregaDTO(this.tipoEntrega, this.enderecoId);
		List<AdicionarItemPedidoDTO> copiaItems = new ArrayList<>(items);
		CriarPedidoDTO criarPedidoDTO = new CriarPedidoDTO(criarPagamentoDTO, criarEntregaDTO, this.veioPeloCarrinho, copiaItems);
		listaCriarPedidoDTO.add(criarPedidoDTO);
		limparAtributos();
		this.estaConstruindoNovoCriarPedidoDTO = false;
	}

	private void limparAtributos() {
		this.metodoPagamento = null;
		this.cartaoId = null;
		this.numeroParcelas = null;
		this.tipoEntrega = null;
		this.enderecoId = null;
		this.veioPeloCarrinho = null;
		items.clear();
	}
}
