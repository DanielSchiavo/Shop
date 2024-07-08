package br.com.danielschiavo.produto.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import br.com.danielschiavo.produto.model.arquivosproduto.ArquivoProduto;
import br.com.danielschiavo.produto.model.tipoentregaproduto.TipoEntregaProduto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
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
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<TipoEntregaProduto> tiposEntrega = new HashSet<>();

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
	private Set<ArquivoProduto> arquivosProduto = new HashSet<>();
    
	private Long subCategoriaId;

	
	
	public Set<TipoEntregaProduto> getTiposEntrega() {
		return Collections.unmodifiableSet(this.tiposEntrega);
	}

	public void adicionarTipoEntrega(TipoEntregaProduto tipoEntrega) {
		this.tiposEntrega.add(tipoEntrega);
	}
	
    public void adicionarTiposEntrega(Set<TipoEntregaProduto> tiposEntrega) {
        this.tiposEntrega.addAll(tiposEntrega);
    }


	public Set<ArquivoProduto> getArquivosProduto() {
		return Collections.unmodifiableSet(this.arquivosProduto);
	}

	public void adicionarArquivoProduto(ArquivoProduto arquivoProduto) {
		this.arquivosProduto.add(arquivoProduto);
	}
	
    public void adicionarArquivosProduto(Set<ArquivoProduto> arquivosProduto) {
        this.arquivosProduto.addAll(arquivosProduto);
    }
}
