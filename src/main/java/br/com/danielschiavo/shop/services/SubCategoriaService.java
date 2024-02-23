package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.UpdateSubCategoryDTO;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;
import br.com.danielschiavo.shop.repositories.SubCategoriaRepository;
import jakarta.validation.Valid;

@Service
public class SubCategoriaService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public SubCategoria save(SubCategoriaDTO dto) {
		var subCategory = new SubCategoria();
		subCategory.setNome(dto.nome());
		subCategory.setCategoria(categoriaRepository.getReferenceById(dto.categoria_id()));
		subCategoriaRepository.save(subCategory);
		return subCategory;
	}

	public SubCategoria getReferenceById(Long id) {
		return subCategoriaRepository.getReferenceById(id);
	}

	public Page<SubCategoria> findAll(Pageable pageable) {
		return subCategoriaRepository.findAll(pageable);
	}

	public void deleteById(Long id) {
		subCategoriaRepository.deleteById(id);
	}

	public SubCategoria alterarSubCategoriaPorId(Long subCategoryId, UpdateSubCategoryDTO categoryDTO) {
		SubCategoria subCategory = this.verificarId(subCategoryId);
		if (categoryDTO.name() != null) {
			subCategory.setNome(categoryDTO.name());
		}
		if (categoryDTO.category_id() != null) {
			Optional<Categoria> categoryOptional = categoriaRepository.findById(categoryDTO.category_id());
			if (categoryOptional.isEmpty()) {
				throw new RuntimeException("Não existe Categoria com esse ID");
			}
			subCategory.setCategoria(categoryOptional.get());
		}
		
		return subCategoriaRepository.save(subCategory);
	}

	public SubCategoria verificarId(Long subcategoryid) {
		Optional<SubCategoria> subCategoryOptional = subCategoriaRepository.findById(subcategoryid);
	    if (subCategoryOptional.isPresent()) {
	        SubCategoria subCategory = subCategoryOptional.get();
	        return subCategory;
	    } else {
	    	throw new RuntimeException("Não existe Sub Categoria com esse ID");
	    }
	}

	public SubCategoria cadastrarSubCategoria(@Valid SubCategoriaDTO dto) {
		SubCategoria subCategoria = save(dto);
		return subCategoria;
	}

}
