package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.subcategoria.AlterarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaComCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoriaDTO;
import br.com.danielschiavo.shop.repositories.SubCategoriaRepository;
import jakarta.validation.Valid;

@Service
public class SubCategoriaService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;
	
	@Autowired
	private CategoriaService categoriaService;

	public Page<MostrarSubCategoriaComCategoriaDTO> listarSubCategorias(Pageable pageable) {
		Page<SubCategoria> pageSubCategoria = subCategoriaRepository.findAll(pageable);
		return pageSubCategoria.map(MostrarSubCategoriaComCategoriaDTO::converterSubCategoriaParaMostrarSubCategoriaComCategoriaDTO);
	}

	@Transactional
	public MostrarSubCategoriaDTO alterarSubCategoriaPorId(Long idSubCategoria, AlterarSubCategoriaDTO alterarSubCategoriaDTO) {
		SubCategoria subCategoria = this.verificarSeExisteIdSubCategoria(idSubCategoria);
		if (alterarSubCategoriaDTO.nome() != null) {
			subCategoria.setNome(alterarSubCategoriaDTO.nome());
		}
		if (alterarSubCategoriaDTO.idCategoria() != null) {
			Categoria categoria = categoriaService.verificarSeExisteIdCategoria(alterarSubCategoriaDTO.idCategoria());
			subCategoria.setCategoria(categoria);
		}
		
		subCategoriaRepository.save(subCategoria);
		
		return new MostrarSubCategoriaDTO(subCategoria);
	}

	@Transactional
	public SubCategoria cadastrarSubCategoria(@Valid SubCategoriaDTO subCategoriaDTO) {
		Long idCategoria = subCategoriaDTO.categoria_id();
		Categoria categoria = categoriaService.verificarSeExisteIdCategoria(idCategoria);
		SubCategoria subCategoria = new SubCategoria(subCategoriaDTO.categoria_id(), subCategoriaDTO.nome(), categoria);
		verificarSeNomeSubCategoriaJaExiste(subCategoria);
//		categoria.getSubCategoria().add(subCategoria);
		
		subCategoriaRepository.save(subCategoria);
		return subCategoria;
	}
	
	@Transactional
	public void deletarSubCategoriaPorId(Long id) {
		SubCategoria subCategoria = verificarSeExisteIdSubCategoria(id);
		subCategoriaRepository.delete(subCategoria);
	}
	
	private void verificarSeNomeSubCategoriaJaExiste(SubCategoria subCategoria) {
	    ExampleMatcher caseInsensitiveMatcher = ExampleMatcher.matchingAll()
	            .withIgnoreCase("nome")
	            .withIgnorePaths("id", "categoria.nome", "categoria.subCategoria")
	            .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
		Example<SubCategoria> example = Example.of(subCategoria, caseInsensitiveMatcher);
		
		if (subCategoriaRepository.exists(example)) {
			throw new ValidacaoException("O nome da Sub Categoria de nome " + subCategoria.getNome() + " já existe para a Categoria de nome " + subCategoria.getCategoria().getNome());
		}
	}
	
	public SubCategoria verificarSeExisteIdSubCategoria(Long idSubCategoria) {
		Optional<SubCategoria> optionalSubCategoria = subCategoriaRepository.findById(idSubCategoria);
		if (!optionalSubCategoria.isPresent()) {
			throw new ValidacaoException("Não existe categoria com o id " + idSubCategoria);
		}
		return optionalSubCategoria.get();
	}

}
