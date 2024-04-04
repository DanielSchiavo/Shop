package br.com.danielschiavo.shop.models.produto.categoria;

import br.com.danielschiavo.shop.models.produto.subcategoria.MostrarSubCategoriaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MostrarCategoriaComSubCategoriaDTO {
	private Long id;
	private String nome;
	private MostrarSubCategoriaDTO subCategoria;
		 
	public static MostrarCategoriaComSubCategoriaDTOBuilder builder() {
		return new MostrarCategoriaComSubCategoriaDTOBuilder();
	}
	

	public static class MostrarCategoriaComSubCategoriaDTOBuilder {

		private MostrarCategoriaComSubCategoriaDTO categoriaAtual;

		public MostrarCategoriaComSubCategoriaDTOBuilder categoria(Long id, String nome) {
			categoriaAtual = new MostrarCategoriaComSubCategoriaDTO();
			categoriaAtual.setId(id);
			categoriaAtual.setNome(nome);
			return this;
		}

		public MostrarCategoriaComSubCategoriaDTOBuilder comSubCategoria(Long id, String nomeSubCategoria) {
			if (categoriaAtual == null) {
				throw new IllegalStateException("Uma Categoria deve ser criada antes de adicionar uma SubCategoria.");
			}
			MostrarSubCategoriaDTO subCategoria = new MostrarSubCategoriaDTO(id, nomeSubCategoria);
			categoriaAtual.setSubCategoria(subCategoria);
			return this;
		}
		
		public MostrarCategoriaComSubCategoriaDTO getCategoria() {
			if (categoriaAtual != null) {
				MostrarCategoriaComSubCategoriaDTO categoria = new MostrarCategoriaComSubCategoriaDTO(categoriaAtual.getId(), categoriaAtual.getNome(), categoriaAtual.getSubCategoria());
				categoriaAtual = null;
				return categoria;
			}
			return null;
		}
	}
}
