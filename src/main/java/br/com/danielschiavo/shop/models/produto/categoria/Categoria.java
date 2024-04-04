package br.com.danielschiavo.shop.models.produto.categoria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.danielschiavo.shop.models.produto.subcategoria.SubCategoria;
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

@Table(name = "categorias")
@Entity(name = "Categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "nome")
public class Categoria implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @Builder.ObtainVia
	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<SubCategoria> subCategoria = new ArrayList<>();

    
    
	public List<SubCategoria> getSubCategorias() {
		return Collections.unmodifiableList(this.subCategoria);
	}

	public void adicionarSubCategoria(SubCategoria subCategoria) {
		this.subCategoria.add(subCategoria);
	}
	
	public static CategoriaBuilder builder() {
		return new CategoriaBuilder();
	}
	
	
	
	public static class CategoriaBuilder {

		private Categoria categoriaAtual;
		private final List<Categoria> categoriasCriadas = new ArrayList<>();

		public CategoriaBuilder categoria(Long id, String nome) {
			if (categoriaAtual != null) {
				categoriasCriadas.add(categoriaAtual);
			}
			categoriaAtual = new Categoria();
			categoriaAtual.setId(id);
			categoriaAtual.setNome(nome);
			return this;
		}

		public CategoriaBuilder comSubCategoria(Long id, String nomeSubCategoria) {
			if (categoriaAtual == null) {
				throw new IllegalStateException("Uma Categoria deve ser criada antes de adicionar uma SubCategoria.");
			}
			SubCategoria subCategoria = new SubCategoria();
			subCategoria.setId(id);
			subCategoria.setNome(nomeSubCategoria);
			subCategoria.setCategoria(categoriaAtual);
			categoriaAtual.adicionarSubCategoria(subCategoria);
			return this;
		}
		
		public Categoria getCategoria() {
			if (categoriaAtual != null) {
				Categoria categoria = new Categoria(categoriaAtual.getId(), categoriaAtual.getNome(), new ArrayList<>(categoriaAtual.getSubCategorias()));
				categoriaAtual = null;
				return categoria;
			}
			return null;
		}

		public List<Categoria> getCategorias() {
			if (categoriaAtual != null && !categoriasCriadas.contains(categoriaAtual)) {
				categoriasCriadas.add(categoriaAtual);
			}
			return categoriasCriadas;
		}
	}
}


