package br.com.danielschiavo.produto.service.admin;

import java.util.Optional;

import br.com.danielschiavo.produto.model.categoria.subcategoria.AlterarSubCategoriaRequest;
import br.com.danielschiavo.produto.model.categoria.subcategoria.CadastrarSubCategoriaRequest;
import br.com.danielschiavo.produto.model.categoria.subcategoria.MostrarSubCategoriaResponse;
import br.com.danielschiavo.produto.model.categoria.subcategoria.SubCategoria;
import br.com.danielschiavo.produto.repository.admin.SubCategoriaRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SubCategoriaAdminService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;

	@Autowired
	private CategoriaAdminService categoriaService;
	
	@Transactional
	public void deletarSubCategoriaPorId(Long id) {
		SubCategoria subCategoria = subCategoriaRepository.getReferenceById(id);
		subCategoriaRepository.delete(subCategoria);
	}
	
	@Transactional
	public SubCategoria cadastrarSubCategoria(@Valid CadastrarSubCategoriaRequest request) {
		categoriaService.pegarCategoriaPorId(request.categoriaId());

		Optional<SubCategoria> optionalSubCategoria = subCategoriaRepository.findByNome(request.nome());
		if (optionalSubCategoria.isPresent()) {
			throw new ValidacaoException("A Sub Categoria de nome " + request.nome() + " já existe");
		}

		SubCategoria subCategoria = new SubCategoria(null, request.nome(), request.categoriaId());
		subCategoriaRepository.save(subCategoria);
		
		return subCategoria;
	}
	
	@Transactional
	public SubCategoria alterarSubCategoriaPorId(Long subCategoriaId, AlterarSubCategoriaRequest request) {
		var mostrarSubCategoriaResponse = pegarSubCategoriaPorId(subCategoriaId);
		SubCategoria subCategoria = subCategoriaRepository.getReferenceById(mostrarSubCategoriaResponse.id());

		if (request.categoriaId() != null) {
			categoriaService.pegarCategoriaPorId(request.categoriaId());
		}

		if (request.nome() != null) {
			Optional<SubCategoria> optionalSubCategoria = subCategoriaRepository.findByNome(request.nome());
			if (optionalSubCategoria.isPresent()) {
				throw new ValidacaoException("A Sub Categoria de nome " + request.nome() + " já existe");
			}
			subCategoria.setNome(request.nome());
		}

		subCategoriaRepository.save(subCategoria);

		return subCategoria;
	}

	public MostrarSubCategoriaResponse pegarSubCategoriaPorId(Long id){
		SubCategoria subCategoria = subCategoriaRepository.findById(id).orElseThrow(() -> new ValidacaoException("Não existe sub categoria com o id " + id));

		return new MostrarSubCategoriaResponse(subCategoria.getId(), subCategoria.getNome());
	}

}
