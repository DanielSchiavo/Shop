package br.com.danielschiavo.produto.service.user;

import br.com.danielschiavo.produto.model.categoria.Categoria;
import br.com.danielschiavo.produto.repository.user.CategoriaRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CategoriaUserService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Page<Categoria> listarCategorias(Pageable pageable) {
		return categoriaRepository.findAll(pageable);
	}

	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	public Categoria verificarSeExisteCategoriaPorId(Long idCategoria) {
		return categoriaRepository.findById(idCategoria).orElseThrow(() -> new ValidacaoException("Não existe categoria com o id " + idCategoria));
	}
}
