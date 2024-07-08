package br.com.danielschiavo.produto.service;

import java.util.Optional;

import br.com.danielschiavo.produto.dto.request.AlterarSubCategoriaRequest;
import br.com.danielschiavo.produto.dto.request.CadastrarSubCategoriaRequest;
import br.com.danielschiavo.produto.dto.response.MostrarSubCategoriaResponse;
import br.com.danielschiavo.produto.model.entity.Categoria;
import br.com.danielschiavo.produto.model.entity.SubCategoria;
import br.com.danielschiavo.produto.repository.SubCategoriaRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;

@Service
public class SubCategoriaService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;

	@Autowired
	private CategoriaService categoriaService;
	
	@Transactional
	public void deletarSubCategoriaPorId(Long subCategoriaId) {
		SubCategoria subCategoria = subCategoriaRepository.getReferenceById(subCategoriaId);
		subCategoriaRepository.delete(subCategoria);
	}
	
	@Transactional
	public SubCategoria cadastrarSubCategoria(@Valid CadastrarSubCategoriaRequest request) {
		Categoria categoria = categoriaService.pegarCategoriaPorId(request.categoriaId());

		boolean isPresent = subCategoriaRepository.findByNomeLowerCase(request.nome()).isPresent();
		if (isPresent) {
			throw new ValidacaoException("A Sub Categoria de nome " + request.nome() + " já existe");
		}

		SubCategoria subCategoria = new SubCategoria(null, request.nome(), categoria.getId());
		return subCategoriaRepository.save(subCategoria);
	}
	
	@Transactional
	public SubCategoria alterarSubCategoriaPorId(AlterarSubCategoriaRequest request, Long subCategoriaId) {
		var subCategoria = pegarSubCategoriaPorId(subCategoriaId);

		if (request.categoriaId() != null) {
			Categoria categoria = categoriaService.pegarCategoriaPorId(request.categoriaId());
			subCategoria.setCategoriaId(categoria.getId());
		}

		if (request.nome() != null) {
			subCategoria.setNome(request.nome());
		}

		return subCategoriaRepository.save(subCategoria);
	}

	public SubCategoria pegarSubCategoriaPorId(Long subCategoriaId){
		return subCategoriaRepository.findById(subCategoriaId)
				.orElseThrow(() -> new ValidacaoException("Não existe sub categoria com o id " + subCategoriaId));
	}

	public Page<SubCategoria> listarSubCategorias(Pageable pageable) {
		return subCategoriaRepository.findAll(pageable);
	}

}
