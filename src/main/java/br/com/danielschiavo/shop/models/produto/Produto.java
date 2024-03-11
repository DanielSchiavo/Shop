package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.pedido.TipoEntrega;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivosProduto;
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

	@JoinColumn(name = "categoria_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private Categoria categoria;
	
	@JoinColumn(name = "sub_categoria_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private SubCategoria subCategoria;
	
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
	
	public Produto(CadastrarProdutoDTO produtoDTO, SubCategoria subCategoria) {
		this.nome = produtoDTO.nome();
		this.descricao = produtoDTO.descricao();
		this.preco = produtoDTO.preco();
		this.quantidade = produtoDTO.quantidade();
		this.ativo = produtoDTO.ativo();
		this.subCategoria = subCategoria;
	}

	public void atualizarAtributos(AlterarProdutoDTO alterarProdutoDTO) {
		if (alterarProdutoDTO.nome() != null) {
			this.nome = alterarProdutoDTO.nome();
		}
		if (alterarProdutoDTO.descricao() != null) {
			this.descricao = alterarProdutoDTO.descricao();
		}
		if (alterarProdutoDTO.preco() != null) {
			this.preco = alterarProdutoDTO.preco();
		}
		if (alterarProdutoDTO.quantidade() != null) {
			this.quantidade = alterarProdutoDTO.quantidade();
		}
		if (alterarProdutoDTO.ativo() != null) {
			this.ativo = alterarProdutoDTO.ativo();
		}
		if (alterarProdutoDTO.tipoEntrega() != null) {
			this.tiposEntrega = alterarProdutoDTO.tipoEntrega();
		}
		if (alterarProdutoDTO.arquivos() != null) {
			List<ArquivosProduto> novaListaArquivosProduto = new ArrayList<>();
			alterarProdutoDTO.arquivos().forEach(arquivoProdutoDTO -> {
				novaListaArquivosProduto.add(new ArquivosProduto(arquivoProdutoDTO.nome(), arquivoProdutoDTO.posicao()));
			});
			this.arquivosProduto = novaListaArquivosProduto;
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
	
//	public String pegarNomePrimeiraImagem(List<ArquivosProduto> arquivosProduto) {
//		ArquivosProduto arquivoProduto = arquivosProduto.stream().filter(ap -> ap.getPosicao() == 0).findFirst().get();
//		return arquivoProduto.getNome();
//	}
	
	public String pegarNomePrimeiraImagem() {
		ArquivosProduto arquivoProduto = this.arquivosProduto.stream().filter(ap -> ap.getPosicao() == 0).findFirst().get();
		return arquivoProduto.getNome();
	}

	public Produto(CadastrarProdutoDTO cadastrarProdutoDTO, Categoria categoria, SubCategoria subCategoria) {
		this.nome = cadastrarProdutoDTO.nome();
		this.descricao = cadastrarProdutoDTO.descricao();
		this.preco = cadastrarProdutoDTO.preco();
		this.quantidade = cadastrarProdutoDTO.quantidade();
		this.ativo = cadastrarProdutoDTO.ativo();
		this.categoria = categoria;
		this.subCategoria = subCategoria;
		this.tiposEntrega = cadastrarProdutoDTO.tipoEntrega();
		cadastrarProdutoDTO.arquivos().forEach(a -> {
			this.arquivosProduto.add(new ArquivosProduto(a.nome(), a.posicao()));
		});
	}
}
