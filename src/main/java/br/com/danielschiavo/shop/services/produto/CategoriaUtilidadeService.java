package br.com.danielschiavo.shop.services.produto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.produto.categoria.Categoria;
import br.com.danielschiavo.shop.repositories.produto.CategoriaRepository;

@Service
public class CategoriaUtilidadeService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria verificarSeExisteCategoriaPorId(Long idCategoria) {
		return categoriaRepository.findById(idCategoria).orElseThrow(() -> new ValidacaoException("Não existe categoria com o id " + idCategoria));
	}
	
}
