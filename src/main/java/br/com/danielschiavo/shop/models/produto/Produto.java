package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProduto;
import br.com.danielschiavo.shop.models.produto.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.models.produto.tipoentregaproduto.TipoEntregaProduto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Table(name = "produtos")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String descricao;

	private BigDecimal preco;

	private Integer quantidade;
	
	private Boolean ativo;

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private Set<TipoEntregaProduto> tiposEntrega = new HashSet<>();

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
	private List<ArquivoProduto> arquivosProduto = new ArrayList<>();
    
	@JoinColumn(name = "sub_categoria_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private SubCategoria subCategoria;

	
	
	public Set<TipoEntregaProduto> getTiposEntrega() {
		return Collections.unmodifiableSet(this.tiposEntrega);
	}

	public void adicionarTipoEntrega(TipoEntregaProduto tipoEntrega) {
		this.tiposEntrega.add(tipoEntrega);
	}

	public List<ArquivoProduto> getArquivosProduto() {
		return Collections.unmodifiableList(arquivosProduto);
	}

	public void adicionarArquivoProduto(ArquivoProduto arquivoProduto) {
		this.arquivosProduto.add(arquivoProduto);
	}
	
	
	
	
	public static ProdutoBuilder builder() {
		return new ProdutoBuilder();
	}
	
	public static class ProdutoBuilder {
		
		private Produto produto;
		private List<Produto> listaProdutos = new ArrayList<>();
		
		public ProdutoBuilder id(Long id) {
	        if(this.produto != null) {
	            listaProdutos.add(this.produto);
	        }
			this.produto = new Produto();
	        this.produto.setId(id);
			return this;
		}

		public ProdutoBuilder nome(String nome) {
	        this.produto.setNome(nome);
			return this;
		}
		
		public ProdutoBuilder descricao(String descricao) {
	        this.produto.setDescricao(descricao);
			return this;
		}
		
		public ProdutoBuilder preco(double preco) {
	        this.produto.setPreco(BigDecimal.valueOf(preco));
			return this;
		}
		
		public ProdutoBuilder quantidade(Integer quantidade) {
	        this.produto.setQuantidade(quantidade);
			return this;
		}

		public ProdutoBuilder ativo(boolean ativo) {
	        this.produto.setAtivo(ativo);
			return this;
		}
		
		public ProdutoBuilder tipoEntregaIdTipo(Long id, TipoEntrega tipoEntrega) {
			this.produto.adicionarTipoEntrega(new TipoEntregaProduto(id, tipoEntrega, this.produto));
			return this;
		}
		
		public ProdutoBuilder arquivoProdutoIdNomePosicao(Long id, String nome, Byte posicao) {
			this.produto.adicionarArquivoProduto(new ArquivoProduto(id, nome, posicao, this.produto));
			return this;
		}
		
	    public ProdutoBuilder subCategoria(SubCategoria subCategoria) {
	        this.produto.setSubCategoria(subCategoria);
	        return this;
	    }
	    
	    public Produto getProduto() {
	        if(this.produto != null) {
	        	Produto copiaProduto = new Produto();
	        	copiaProduto.setId(this.produto.getId());
	        	copiaProduto.setNome(this.produto.getNome());
	        	copiaProduto.setDescricao(this.produto.getDescricao());
	        	copiaProduto.setPreco(this.produto.getPreco());
	        	copiaProduto.setQuantidade(this.produto.getQuantidade());
	        	copiaProduto.setAtivo(this.produto.getAtivo());
	        	copiaProduto.setSubCategoria(this.produto.getSubCategoria());
	        	this.produto.getTiposEntrega().forEach(tipo -> {
	        		copiaProduto.adicionarTipoEntrega(new TipoEntregaProduto(tipo.getId(), tipo.getTipoEntrega(), copiaProduto));
	        	});
	        	this.produto.getArquivosProduto().forEach(arquivo -> {
	        		copiaProduto.adicionarArquivoProduto(new ArquivoProduto(arquivo.getId(), arquivo.getNome(), arquivo.getPosicao(), copiaProduto));
	        	});
	        	this.produto = null;
	        	return copiaProduto;
	        }
	        return null;
	    }
	    
	    public List<Produto> getProdutos() {
	        if(this.produto != null) {
	            listaProdutos.add(this.produto); // Adiciona o último produto configurado à lista
	            this.produto = null; // Reseta o produto atual da fábrica
	        }
	        List<Produto> novaLista = new ArrayList<>(listaProdutos);
	        listaProdutos.clear();
	        return novaLista;
	    }
	}
}
