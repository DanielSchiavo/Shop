package br.com.danielschiavo.shop.models.produto.categoria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.danielschiavo.shop.models.produto.subcategoria.MostrarSubCategoriaDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MostrarCategoriaComTodasSubCategoriaDTO {
	private Long id;
	private String nome;
	private List<MostrarSubCategoriaDTO> subCategorias = new ArrayList<>();
	
	
	
	public List<MostrarSubCategoriaDTO> getSubCategorias() {
		return Collections.unmodifiableList(this.subCategorias);
	}

	public void adicionarSubCategoria(MostrarSubCategoriaDTO mostrarSubCategoriaDTO) {
		this.subCategorias.add(mostrarSubCategoriaDTO);
	}
	
	
	
	public static MostrarCategoriaComTodasSubCategoriaDTOBuilder builder() {
		return new MostrarCategoriaComTodasSubCategoriaDTOBuilder();
	}
	
	public static class MostrarCategoriaComTodasSubCategoriaDTOBuilder {

		private MostrarCategoriaComTodasSubCategoriaDTO categoriaAtual;
		private final List<MostrarCategoriaComTodasSubCategoriaDTO> categoriasCriadas = new ArrayList<>();

		public MostrarCategoriaComTodasSubCategoriaDTOBuilder categoria(Long id, String nome) {
			if (categoriaAtual != null) {
				categoriasCriadas.add(categoriaAtual);
			}
			categoriaAtual = new MostrarCategoriaComTodasSubCategoriaDTO();
			categoriaAtual.setId(id);
			categoriaAtual.setNome(nome);
			return this;
		}

		public MostrarCategoriaComTodasSubCategoriaDTOBuilder comSubCategoria(Long id, String nomeSubCategoria) {
			if (categoriaAtual == null) {
				throw new IllegalStateException("Uma Categoria deve ser criada antes de adicionar uma SubCategoria.");
			}
			MostrarSubCategoriaDTO subCategoria = new MostrarSubCategoriaDTO(id, nomeSubCategoria);
			categoriaAtual.adicionarSubCategoria(subCategoria);
			return this;
		}
		
		public MostrarCategoriaComTodasSubCategoriaDTO getCategoria() {
			if (categoriaAtual != null) {
				MostrarCategoriaComTodasSubCategoriaDTO categoria = new MostrarCategoriaComTodasSubCategoriaDTO(categoriaAtual.getId(), categoriaAtual.getNome(), new ArrayList<>(categoriaAtual.getSubCategorias()));
				categoriaAtual = null;
				return categoria;
			}
			return null;
		}

		public List<MostrarCategoriaComTodasSubCategoriaDTO> getCategorias() {
			if (categoriaAtual != null && !categoriasCriadas.contains(categoriaAtual)) {
				categoriasCriadas.add(categoriaAtual);
			}
			List<MostrarCategoriaComTodasSubCategoriaDTO> copiaLista = new ArrayList<>(categoriasCriadas);
			categoriasCriadas.clear();
			return copiaLista;
		}
	}
}
