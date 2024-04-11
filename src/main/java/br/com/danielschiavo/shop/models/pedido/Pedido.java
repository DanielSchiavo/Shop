package br.com.danielschiavo.shop.models.pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.cartao.Cartao;
import br.com.danielschiavo.shop.models.cliente.endereco.Endereco;
import br.com.danielschiavo.shop.models.pedido.entrega.EnderecoPedido;
import br.com.danielschiavo.shop.models.pedido.entrega.Entrega;
import br.com.danielschiavo.shop.models.pedido.itempedido.ItemPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.CartaoPedido;
import br.com.danielschiavo.shop.models.pedido.pagamento.MetodoPagamento;
import br.com.danielschiavo.shop.models.pedido.pagamento.Pagamento;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProduto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "pedidos")
@Entity(name = "Pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Pedido {

	@Id
	@GeneratedValue(generator = "UUID")
	public UUID id;
	
	private BigDecimal valorTotal;
	
	private LocalDateTime dataPedido;
	
	private String nomeCliente;
	
	private String cpf;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status_pedido")
	private StatusPedido statusPedido;
	
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itemsPedido = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Pagamento pagamento;
    
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Entrega entrega;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Cliente cliente;
	
	
	
	public List<ItemPedido> getItemsPedido() {
		return Collections.unmodifiableList(this.itemsPedido);
	}

	public void adicionarItemPedido(ItemPedido itemPedido) {
		this.itemsPedido.add(itemPedido);
	}
	
    public void verificarEAtualizarItemsPedidoDuplicados() {
        Map<Long, ItemPedido> mapIdProdutoParaItemPedido = new HashMap<>();

        for (ItemPedido itemPedido : itemsPedido) {
            Long idProduto = itemPedido.getProdutoId();
            int quantidade = itemPedido.getQuantidade();

            if (mapIdProdutoParaItemPedido.containsKey(idProduto)) {
                ItemPedido itemExistente = mapIdProdutoParaItemPedido.get(idProduto);
                int novaQuantidade = itemExistente.getQuantidade() + quantidade;
                itemExistente.setQuantidade(novaQuantidade);
                itemExistente.setSubTotal(itemExistente.getSubTotal().add(itemPedido.getSubTotal()));
            } else {
                mapIdProdutoParaItemPedido.put(idProduto, itemPedido);
            }
        }

        itemsPedido = new ArrayList<>(mapIdProdutoParaItemPedido.values());
    }
	
	
	
	
	public static PedidoBuilder builder() {
		return new PedidoBuilder();
	}
	
	public static class PedidoBuilder {

	    private Pedido pedido;
	    private List<Pedido> listaPedidos = new ArrayList<>();
	    
	    public PedidoBuilder cliente(Cliente cliente) {
	        if (this.pedido != null) {
	        	listaPedidos.add(pedido);
	        }
	        this.pedido = new Pedido();
	        this.pedido.setId(UUID.randomUUID());
	        this.pedido.setDataPedido(LocalDateTime.now());
	        this.pedido.setNomeCliente(cliente.getNome() + " " + cliente.getSobrenome());
	        this.pedido.setCpf(cliente.getCpf());
	        this.pedido.setStatusPedido(StatusPedido.A_PAGAR);
		        Pagamento pagamento = new Pagamento();
		        pagamento.setPedido(this.pedido);
	        this.pedido.setPagamento(pagamento);
		        Entrega entrega = new Entrega();
		        entrega.setPedido(this.pedido);
	        this.pedido.setEntrega(entrega);
	        this.pedido.setCliente(cliente);
	        return this;
	    }

	    public PedidoBuilder comItemPedidoIdQuantidadeProduto(Long id, Integer quantidade, Produto produto) {
			ArquivoProduto arquivoProduto = produto.getArquivosProduto().stream().filter(ap -> ap.getPosicao() == 0).findFirst().get();
	        ItemPedido itemPedido = new ItemPedido(id, produto.getPreco(), quantidade, produto.getNome(), arquivoProduto.getNome(), null, produto.getId(), this.pedido);
	        BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidade));
	        itemPedido.setSubTotal(subTotal);
	        this.pedido.adicionarItemPedido(itemPedido);
	        return this;
	    }
	    
	    public PedidoBuilder comItemPedido(ItemPedido itemPedido) {
	    	this.pedido.adicionarItemPedido(itemPedido);
	    	return this;
	    }
	    
	    public PedidoBuilder pagamentoIdMetodo(Long id, MetodoPagamento metodoPagamento) {
	    	this.pedido.getPagamento().setId(id);
	    	this.pedido.getPagamento().setMetodoPagamento(metodoPagamento);
	    	this.pedido.getPagamento().setStatusPagamento(metodoPagamento.statusPagamentoDeveSer());
	        return this;
	    }
	    
	    public PedidoBuilder pagamentoCartao(Cartao cartao) {
	    	CartaoPedido cartaoPedido = new CartaoPedido();
	    	cartaoPedido.setNomeBanco(cartao.getNomeBanco());
	    	cartaoPedido.setNumeroCartao(cartao.getNumeroCartao());
	    	cartaoPedido.setNomeNoCartao(cartao.getNomeNoCartao());
	    	cartaoPedido.setValidadeCartao(cartao.getValidadeCartao());
	    	cartaoPedido.setTipoCartao(cartao.getTipoCartao());
	    	this.pedido.getPagamento().setCartaoPedido(cartaoPedido);
	    	return this;
	    }
	    
	    public PedidoBuilder pagamentoNumeroDeParcelas(String numeroDeParcelas) {
	    	this.pedido.getPagamento().getCartaoPedido().setNumeroDeParcelas(numeroDeParcelas);
	    	return this;
	    }
	    
	    public PedidoBuilder entregaIdTipo(Long id, TipoEntrega tipoEntrega) {
	    	this.pedido.getEntrega().setId(id);
	    	this.pedido.getEntrega().setTipoEntrega(tipoEntrega);
	        return this;
	    }
	    
	    public PedidoBuilder entregaEndereco(Endereco endereco) {
	    	EnderecoPedido enderecoPedido = new EnderecoPedido(endereco.getCep(), endereco.getRua(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado());
	        this.pedido.getEntrega().setEnderecoPedido(enderecoPedido);
	    	return this;
	    }
	    
	    public Pedido getPedido() {
	    	if (this.pedido != null) {
	    		this.pedido.verificarEAtualizarItemsPedidoDuplicados();
	    		Pagamento pagamentoDto = this.pedido.getPagamento();
	    		
	    		CartaoPedido cartaoPedidoDTO = this.pedido.getPagamento().getCartaoPedido();
	            Pagamento pagamento = new Pagamento();
	            pagamento.setId(pagamentoDto.getId());;
	            pagamento.setMetodoPagamento(pagamentoDto.getMetodoPagamento());
	    		pagamento.setStatusPagamento(pagamentoDto.getStatusPagamento());
	    		pagamento.setDataPagamento(pagamentoDto.getDataPagamento());
	    		pagamento.setCartaoPedido(cartaoPedidoDTO != null ? new CartaoPedido(cartaoPedidoDTO.getNomeBanco(), cartaoPedidoDTO.getNumeroCartao(), cartaoPedidoDTO.getNomeNoCartao(), cartaoPedidoDTO.getValidadeCartao(), cartaoPedidoDTO.getNumeroDeParcelas(), cartaoPedidoDTO.getTipoCartao()) : null);
	            
	            Entrega entregaDto = this.pedido.getEntrega();
	            EnderecoPedido enderecoPedidoDto = this.pedido.getEntrega().getEnderecoPedido();
	            Entrega entrega = new Entrega();
	            entrega.setId(entregaDto.getId());
	            entrega.setTipoEntrega(entregaDto.getTipoEntrega());
	            entrega.setEnderecoPedido(enderecoPedidoDto != null ? new EnderecoPedido(enderecoPedidoDto.getCep(), enderecoPedidoDto.getRua(), enderecoPedidoDto.getNumero(), enderecoPedidoDto.getComplemento(), enderecoPedidoDto.getBairro(), enderecoPedidoDto.getCidade(), enderecoPedidoDto.getEstado()) : null);
	            
	            Pedido copiaPedido = new Pedido();
	            copiaPedido.setId(this.pedido.getId());
	            copiaPedido.setDataPedido(this.pedido.getDataPedido());
	            copiaPedido.setNomeCliente(this.pedido.getNomeCliente());
	            copiaPedido.setCpf(this.pedido.getCpf());
	            copiaPedido.setStatusPedido(this.pedido.getStatusPedido());
	            if (this.pedido.getItemsPedido().size() >= 1) {
	            	AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(BigDecimal.ZERO);
	            	this.pedido.getItemsPedido().forEach(item -> {
	            		copiaPedido.adicionarItemPedido(new ItemPedido(item.getId(), item.getPreco(), item.getQuantidade(), item.getNomeProduto(), item.getPrimeiraImagem(), item.getSubTotal(), item.getProdutoId(), null));
	            		valorTotal.updateAndGet(v -> v.add(item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()))));
	            	});
	            	copiaPedido.setValorTotal(valorTotal.get());
	            }
	            copiaPedido.setPagamento(pagamento);
	            copiaPedido.setEntrega(entrega);
	            copiaPedido.setCliente(this.pedido.getCliente());
	            this.pedido = null;	            
	    		return copiaPedido;
	    	}
	    	return null;
	    }

	    public List<Pedido> getPedidos() {
	        if(this.pedido != null) {
	            listaPedidos.add(this.pedido); // Adiciona o último pedido configurado à lista
	            listaPedidos.forEach(pedido -> {
	            	AtomicReference<BigDecimal> valorTotal = new AtomicReference<>(BigDecimal.ZERO);
	            	this.pedido.getItemsPedido().forEach(item -> {
	            		valorTotal.updateAndGet(v -> v.add(item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()))));
	            	});
	            	pedido.setValorTotal(valorTotal.get());
	            	
	            });
	            this.pedido = null; // Reseta o pedido atual da fábrica
	        }
	        return listaPedidos;
	    }
	    
	}
	
}
