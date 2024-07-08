package br.com.danielschiavo.produto.dto.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MostrarCategoriaComTodasSubCategoriaResponse {
	private Long id;
	private String nome;
	private List<MostrarSubCategoriaResponse> subCategorias = new ArrayList<>();
	
	
	
	public List<MostrarSubCategoriaResponse> getSubCategorias() {
		return Collections.unmodifiableList(this.subCategorias);
	}

	public void adicionarSubCategoria(MostrarSubCategoriaResponse mostrarSubCategoriaResponse) {
		this.subCategorias.add(mostrarSubCategoriaResponse);
	}
	
	
	
	public static MostrarCategoriaComTodasSubCategoriaDTOBuilder builder() {
		return new MostrarCategoriaComTodasSubCategoriaDTOBuilder();
	}
	
	public static class MostrarCategoriaComTodasSubCategoriaDTOBuilder {

		private MostrarCategoriaComTodasSubCategoriaResponse categoriaAtual;
		private final List<MostrarCategoriaComTodasSubCategoriaResponse> categoriasCriadas = new ArrayList<>();

		public MostrarCategoriaComTodasSubCategoriaDTOBuilder categoria(Long id, String nome) {
			if (categoriaAtual != null) {
				categoriasCriadas.add(categoriaAtual);
			}
			categoriaAtual = new MostrarCategoriaComTodasSubCategoriaResponse();
			categoriaAtual.setId(id);
			categoriaAtual.setNome(nome);
			return this;
		}

		public MostrarCategoriaComTodasSubCategoriaDTOBuilder comSubCategoria(Long id, String nomeSubCategoria) {
			if (categoriaAtual == null) {
				throw new IllegalStateException("Uma Categoria deve ser criada antes de adicionar uma SubCategoria.");
			}
			MostrarSubCategoriaResponse subCategoria = new MostrarSubCategoriaResponse(id, nomeSubCategoria);
			categoriaAtual.adicionarSubCategoria(subCategoria);
			return this;
		}
		
		public MostrarCategoriaComTodasSubCategoriaResponse getCategoria() {
			if (categoriaAtual != null) {
				MostrarCategoriaComTodasSubCategoriaResponse categoria = new MostrarCategoriaComTodasSubCategoriaResponse(categoriaAtual.getId(), categoriaAtual.getNome(), new ArrayList<>(categoriaAtual.getSubCategorias()));
				categoriaAtual = null;
				return categoria;
			}
			return null;
		}

		public List<MostrarCategoriaComTodasSubCategoriaResponse> getCategorias() {
			if (categoriaAtual != null && !categoriasCriadas.contains(categoriaAtual)) {
				categoriasCriadas.add(categoriaAtual);
			}
			List<MostrarCategoriaComTodasSubCategoriaResponse> copiaLista = new ArrayList<>(categoriasCriadas);
			categoriasCriadas.clear();
			return copiaLista;
		}
	}
}
