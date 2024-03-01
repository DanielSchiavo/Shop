package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaDTO;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Transactional
	public MostrarCategoriaDTO cadastrarCategoria(String name) {
		var categoria = new Categoria(name);
		verificarSeNomeCategoriaJaExiste(categoria);
		
		categoriaRepository.save(categoria);
		return new MostrarCategoriaDTO(categoria);
	}

	@Transactional
	public MostrarCategoriaDTO atualizarNomePorId(Long id, String name) {
		Categoria categoria = verificarSeExisteIdCategoria(id);
		categoria.setNome(name);
		verificarSeNomeCategoriaJaExiste(categoria);
		categoriaRepository.save(categoria);
		return new MostrarCategoriaDTO(categoria);
	}

	@Transactional
	public void deletarPorId(Long id) {
		if (categoriaRepository.existsById(id)) {
			categoriaRepository.deleteById(id);
		}
		else {
			throw new ValidacaoException("O ID da Categoria de numero " + id + " não existe");
		}
	}
	
	private void verificarSeNomeCategoriaJaExiste(Categoria categoria) {
	    ExampleMatcher caseInsensitiveMatcher = ExampleMatcher.matchingAll()
	            .withIgnoreCase("nome")
	            .withIgnorePaths("id", "subCategoria")
	            .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
		Example<Categoria> example = Example.of(categoria, caseInsensitiveMatcher);
		
		if (categoriaRepository.exists(example)) {
			throw new ValidacaoException("A categoria de nome " + categoria.getNome() + " já existe");
		}
	}
	
	public Categoria verificarSeExisteIdCategoria(Long idCategoria) {
		Optional<Categoria> optionalCategoria = categoriaRepository.findById(idCategoria);
		if (!optionalCategoria.isPresent()) {
			throw new ValidacaoException("Não existe categoria com o id " + idCategoria);
		}
		return optionalCategoria.get();
	}

}
