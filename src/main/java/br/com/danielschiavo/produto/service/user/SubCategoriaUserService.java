package br.com.danielschiavo.produto.service.user;

import br.com.danielschiavo.produto.repository.user.SubCategoriaRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.produto.model.categoria.subcategoria.SubCategoria;

@Service
public class SubCategoriaUserService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;

	public Page<SubCategoria> listarSubCategorias(Pageable pageable) {
		return subCategoriaRepository.findAll(pageable);
	}

	public SubCategoria verificarSeExisteSubCategoriaPorId(Long idSubCategoria) {
		return subCategoriaRepository.findById(idSubCategoria).orElseThrow(() -> new ValidacaoException("Não existe categoria com o id " + idSubCategoria));
	}

}
