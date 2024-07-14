package br.com.danielschiavo.produto.service;

import br.com.danielschiavo.produto.model.entity.Categoria;
import br.com.danielschiavo.produto.dto.request.CriarCategoriaRequest;
import br.com.danielschiavo.produto.repository.CategoriaRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Transactional
	public void deletarCategoriaPorId(Long categoriaId) {
		Categoria categoria = categoriaRepository.getReferenceById(categoriaId);
		categoriaRepository.delete(categoria);
	}

	@Transactional
	public Categoria cadastrarCategoria(String nomeCategoria) {
		boolean isPresent = categoriaRepository.findByNomeLowerCase(nomeCategoria).isPresent();
		if (isPresent)	{
			throw new ValidacaoException("Já existe uma categoria com esse name");
		}

		Categoria categoria = new Categoria(null, nomeCategoria);
		categoriaRepository.save(categoria);
		return categoria;
	}

	@Transactional
	public Categoria alterarNomeCategoriaPorId(Long categoriaId, String nome) {
		var categoria = pegarCategoriaPorId(categoriaId);

		categoria.setNome(nome);
		return categoriaRepository.save(categoria);
	}

	public Categoria pegarCategoriaPorId(Long id){
		return categoriaRepository.findById(id)
				.orElseThrow(() -> new ValidacaoException("Não existe categoria com o id " + id));
	}

	public Page<Categoria> listarCategorias(Pageable pageable) {
		return categoriaRepository.findAll(pageable);
	}
	

//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
