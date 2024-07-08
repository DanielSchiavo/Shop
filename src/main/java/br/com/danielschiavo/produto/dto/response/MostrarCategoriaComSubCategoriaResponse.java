package br.com.danielschiavo.produto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MostrarCategoriaComSubCategoriaResponse {
	private Long id;
	private String nome;
	private MostrarSubCategoriaResponse subCategoria;
		 
	public static MostrarCategoriaComSubCategoriaDTOBuilder builder() {
		return new MostrarCategoriaComSubCategoriaDTOBuilder();
	}
	

	public static class MostrarCategoriaComSubCategoriaDTOBuilder {

		private MostrarCategoriaComSubCategoriaResponse categoriaAtual;

		public MostrarCategoriaComSubCategoriaDTOBuilder categoria(Long id, String nome) {
			categoriaAtual = new MostrarCategoriaComSubCategoriaResponse();
			categoriaAtual.setId(id);
			categoriaAtual.setNome(nome);
			return this;
		}

		public MostrarCategoriaComSubCategoriaDTOBuilder comSubCategoria(Long id, String nomeSubCategoria) {
			if (categoriaAtual == null) {
				throw new IllegalStateException("Uma Categoria deve ser criada antes de adicionar uma SubCategoria.");
			}
			MostrarSubCategoriaResponse subCategoria = new MostrarSubCategoriaResponse(id, nomeSubCategoria);
			categoriaAtual.setSubCategoria(subCategoria);
			return this;
		}
		
		public MostrarCategoriaComSubCategoriaResponse getCategoria() {
			if (categoriaAtual != null) {
				MostrarCategoriaComSubCategoriaResponse categoria = new MostrarCategoriaComSubCategoriaResponse(categoriaAtual.getId(), categoriaAtual.getNome(), categoriaAtual.getSubCategoria());
				categoriaAtual = null;
				return categoria;
			}
			return null;
		}
	}
}
