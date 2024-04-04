package br.com.danielschiavo.shop.services.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.produto.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.repositories.produto.SubCategoriaRepository;

@Service
public class SubCategoriaUtilidadeService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;
	
	public SubCategoria verificarSeExisteSubCategoriaPorId(Long idSubCategoria) {
		return subCategoriaRepository.findById(idSubCategoria).orElseThrow(() -> new ValidacaoException("Não existe categoria com o id " + idSubCategoria));
	}

}
