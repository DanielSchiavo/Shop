package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "produtos")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, unique = true, nullable = false)
	private String nome;
	@Column(nullable = false, columnDefinition = "TEXT")
	private String descricao;
	@Column(length = 8, nullable = false, columnDefinition = "NUMERIC(8,2)")
	private BigDecimal preco;
	@Column(nullable = false, columnDefinition = "INT")
	private Integer quantidade;
	@Column(nullable = false, columnDefinition = "BOOLEAN")
	private Boolean ativo;

	@JoinColumn(name = "sub_categoria_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private SubCategoria subCategoria;
	
	@JoinColumn(name = "categoria_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private Categoria categoria;
	
    @ElementCollection(targetClass = TipoEntrega.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "produtos_tipo_entrega", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "tipo_entrega")
    private Set<TipoEntrega> tiposEntrega = new HashSet<>();

	@ElementCollection
	@CollectionTable(
			name = "produtos_arquivos",
			joinColumns = @JoinColumn(name = "produto_id")
			)
	private List<ArquivosProduto> arquivosProduto = new ArrayList<>();
	
	public Produto(ProdutoDTO produtoDTO, SubCategoria subCategoria) {
		this.nome = produtoDTO.nome();
		this.descricao = produtoDTO.descricao();
		this.preco = produtoDTO.preco();
		this.quantidade = produtoDTO.quantidade();
		this.ativo = produtoDTO.ativo();
		this.subCategoria = subCategoria;
	}

	public void atualizarAtributos(AtualizarProdutoDTO atualizarProdutoDTO) {
		if (atualizarProdutoDTO.nome() != null) {
			this.nome = atualizarProdutoDTO.nome();
		}
		if (atualizarProdutoDTO.descricao() != null) {
			this.descricao = atualizarProdutoDTO.descricao();
		}
		if (atualizarProdutoDTO.preco() != null) {
			this.preco = atualizarProdutoDTO.preco();
		}
		if (atualizarProdutoDTO.quantidade() != null) {
			this.quantidade = atualizarProdutoDTO.quantidade();
		}
		if (atualizarProdutoDTO.ativo() != null) {
			this.ativo = atualizarProdutoDTO.ativo();
		}
	}

	public void setArquivosProduto(List<String> listaArquivos, int[] arrayPosicao) {
		for(int i=0; i<listaArquivos.size() && i<arrayPosicao.length; i++) {
			ArquivosProduto arquivosProduto = new ArquivosProduto();
			arquivosProduto.setNome(listaArquivos.get(i));
			arquivosProduto.setPosicao(arrayPosicao[i]);
		    this.arquivosProduto.add(arquivosProduto);
		}
	}

	public void adicionarArquivosProduto(String nomeArquivo, int posicao) {
		ArquivosProduto arquivo = new ArquivosProduto();
		arquivo.setNome(nomeArquivo);
		arquivo.setPosicao(posicao);
		this.arquivosProduto.add(arquivo);
	}
	
	public String pegarPrimeiraImagem(List<ArquivosProduto> arquivosProduto) {
		ArquivosProduto arquivoProduto = arquivosProduto.stream().filter(ap -> ap.getPosicao() == 0).findFirst().get();
		return arquivoProduto.getNome();
	}
}
