package br.com.danielschiavo.shop.models.pedido.itempedido;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.models.produto.Produto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "pedidos_items")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private BigDecimal preco;
	
	private Integer quantidade;
	
	private String nomeProduto;
	
	private String primeiraImagem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Produto produto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Pedido pedido;
	
	public ItemPedido(Produto produto, Integer quantidade, String nomePrimeiraImagem) {
		this.preco = produto.getPreco();
		this.quantidade = quantidade;
		this.produto = produto;
		this.nomeProduto = produto.getNome();
		this.primeiraImagem = produto.pegarNomePrimeiraImagem();
		this.primeiraImagem = nomePrimeiraImagem;
	}


}
