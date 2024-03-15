package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.categoria.CriarCategoriaDTO;
import br.com.danielschiavo.shop.models.categoria.MostrarCategoriaDTO;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Transactional
	public MostrarCategoriaDTO criarCategoria(String nomeCategoria) {
		Optional<Categoria> optionalCategoria = categoriaRepository.findByNome(nomeCategoria);
		if (optionalCategoria.isPresent()) {
			throw new ValidacaoException("A categoria de nome " + nomeCategoria + " já existe");
		} else {
			Categoria categoria = new Categoria(nomeCategoria);

			categoriaRepository.save(categoria);
			return new MostrarCategoriaDTO(categoria);
		}
	}

	@Transactional
	public MostrarCategoriaDTO alterarNomeCategoriaPorId(Long id, CriarCategoriaDTO categoriaDTO) {
		Categoria categoria = verificarSeExisteCategoriaPorId(id);
		String novoNome = categoriaDTO.nome();
		Optional<Categoria> optionalCategoria = categoriaRepository.findByNome(novoNome);
		if (optionalCategoria.isPresent()) {
			throw new ValidacaoException("A categoria de nome " + novoNome + " já existe");
		}
		categoria.setNome(categoriaDTO.nome());
		categoriaRepository.save(categoria);
		return new MostrarCategoriaDTO(categoria);
	}

	@Transactional
	public void deletarCategoriaPorId(Long id) {
		Categoria categoria = verificarSeExisteCategoriaPorId(id);
		categoriaRepository.delete(categoria);
	}

	public Categoria verificarSeExisteCategoriaPorId(Long idCategoria) {
		Optional<Categoria> optionalCategoria = categoriaRepository.findById(idCategoria);
		if (!optionalCategoria.isPresent()) {
			throw new ValidacaoException("Não existe categoria com o id " + idCategoria);
		}
		return optionalCategoria.get();
	}

	public Page<Categoria> listarCategorias(Pageable pageable) {
		return categoriaRepository.findAll(pageable);
	}

}
