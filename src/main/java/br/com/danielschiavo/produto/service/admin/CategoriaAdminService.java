package br.com.danielschiavo.produto.service.admin;

import br.com.danielschiavo.produto.model.categoria.Categoria;
import br.com.danielschiavo.produto.model.categoria.CriarCategoriaRequest;
import br.com.danielschiavo.produto.repository.admin.CategoriaRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CategoriaAdminService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Transactional
	public void deletarCategoriaPorId(Long id) {
		var mostrarCategoriaResponse = pegarCategoriaPorId(id);
		Categoria categoria = categoriaRepository.getReferenceById(mostrarCategoriaResponse.getId());

		categoriaRepository.delete(categoria);
	}

	@Transactional
	public Categoria criarCategoria(String nomeCategoria) {
		Optional<Categoria> optionalCategoria = categoriaRepository.findByNomeLowerCase(nomeCategoria);
		if (optionalCategoria.isPresent())	{
			throw new ValidacaoException("Já existe uma categoria com esse nome");
		}

		Categoria categoria = new Categoria(null, nomeCategoria);
		categoriaRepository.save(categoria);
		return categoria;
	}

	@Transactional
	public Categoria alterarNomeCategoriaPorId(Long categoriaId, CriarCategoriaRequest request) {
		var mostrarCategoriaResponse = pegarCategoriaPorId(categoriaId);

		Categoria categoria = categoriaRepository.getReferenceById(categoriaId);
		categoria.setNome(request.nome());
		categoriaRepository.save(categoria);

		return categoria;
	}

	public Categoria pegarCategoriaPorId(Long id){
		Categoria categoria = categoriaRepository.findById(id).orElseThrow(() -> new ValidacaoException("Não existe categoria com o id " + id));

		return categoria;
	}
	

//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
