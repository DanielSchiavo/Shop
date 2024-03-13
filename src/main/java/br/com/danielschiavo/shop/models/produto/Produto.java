package br.com.danielschiavo.shop.models.produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivosProduto;
import br.com.danielschiavo.shop.models.produto.tipoentregaproduto.TipoEntregaProduto;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
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

	private String nome;

	private String descricao;

	private BigDecimal preco;

	private Integer quantidade;
	
	private Boolean ativo;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
    private Set<TipoEntregaProduto> tiposEntrega = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL)
	private List<ArquivosProduto> arquivosProduto = new ArrayList<>();
    
	@JoinColumn(name = "categoria_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private Categoria categoria;
	
	@JoinColumn(name = "sub_categoria_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private SubCategoria subCategoria;
	
	public Produto(CadastrarProdutoDTO produtoDTO, SubCategoria subCategoria) {
		this.nome = produtoDTO.nome();
		this.descricao = produtoDTO.descricao();
		this.preco = produtoDTO.preco();
		this.quantidade = produtoDTO.quantidade();
		this.ativo = produtoDTO.ativo();
		this.subCategoria = subCategoria;
	}

	public void alterarAtributos(AlterarProdutoDTO alterarProdutoDTO, Produto produto) {
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
			Set<TipoEntregaProduto> novoTiposEntrega = new HashSet<>();
			alterarProdutoDTO.tipoEntrega().forEach(tipo -> {
				novoTiposEntrega.add(new TipoEntregaProduto(null, tipo, produto));
			});
			this.tiposEntrega = novoTiposEntrega;
			
		}
		if (alterarProdutoDTO.arquivos() != null) {
			List<ArquivosProduto> novaListaArquivosProduto = new ArrayList<>();
			alterarProdutoDTO.arquivos().forEach(arquivoProdutoDTO -> {
				novaListaArquivosProduto.add(new ArquivosProduto(null, arquivoProdutoDTO.nome(), arquivoProdutoDTO.posicao().byteValue(), produto));
			});
			this.arquivosProduto = novaListaArquivosProduto;
		}
	}

	public void adicionarArquivosProduto(String nomeArquivo, int posicao) {
		ArquivosProduto arquivo = new ArquivosProduto();
		arquivo.setNome(nomeArquivo);
		arquivo.setPosicao((byte) posicao);
		this.arquivosProduto.add(arquivo);
	}
	
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
		cadastrarProdutoDTO.tipoEntrega().forEach(te -> {
			this.tiposEntrega.add(new TipoEntregaProduto(null, te, this));
		});;
		cadastrarProdutoDTO.arquivos().forEach(a -> {
			this.arquivosProduto.add(new ArquivosProduto(null, a.nome(), a.posicao().byteValue(), this));
		});
	}
}
