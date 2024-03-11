package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.models.subcategoria.AlterarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaComCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.models.subcategoria.SubCategoriaDTO;
import br.com.danielschiavo.shop.repositories.SubCategoriaRepository;
import jakarta.validation.Valid;

@Service
public class SubCategoriaService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;

	@Autowired
	private CategoriaService categoriaService;

	public Page<MostrarSubCategoriaComCategoriaDTO> listarSubCategorias(Pageable pageable) {
		Page<SubCategoria> pageSubCategoria = subCategoriaRepository.findAll(pageable);
		return pageSubCategoria.map(MostrarSubCategoriaComCategoriaDTO::converterSubCategoriaParaMostrarSubCategoriaComCategoriaDTO);
	}

	@Transactional
	public MostrarSubCategoriaDTO alterarSubCategoriaPorId(Long idSubCategoria, AlterarSubCategoriaDTO alterarSubCategoriaDTO) {
		SubCategoria subCategoria = this.verificarSeExisteSubCategoriaPorId(idSubCategoria);
		if (alterarSubCategoriaDTO.nome() != null) {
			verificarSeNomeSubCategoriaJaExiste(alterarSubCategoriaDTO.nome());
			subCategoria.setNome(alterarSubCategoriaDTO.nome());
		}
		if (alterarSubCategoriaDTO.idCategoria() != null) {
			Categoria categoria = categoriaService.verificarSeExisteCategoriaPorId(alterarSubCategoriaDTO.idCategoria());
			subCategoria.setCategoria(categoria);
		}

		subCategoriaRepository.save(subCategoria);

		return new MostrarSubCategoriaDTO(subCategoria);
	}

	public void verificarSeNomeSubCategoriaJaExiste(String nome) {
		Optional<SubCategoria> optionalSubCategoria = subCategoriaRepository.findByNome(nome);
		if (optionalSubCategoria.isPresent()) {
			throw new ValidacaoException("A categoria de nome " + nome + " já existe");
		}
	}

	@Transactional
	public SubCategoria cadastrarSubCategoria(@Valid SubCategoriaDTO subCategoriaDTO) {
		Long idCategoria = subCategoriaDTO.categoria_id();
		Categoria categoria = categoriaService.verificarSeExisteCategoriaPorId(idCategoria);
		verificarSeNomeSubCategoriaJaExiste(subCategoriaDTO.nome());
		SubCategoria subCategoria = new SubCategoria(null, subCategoriaDTO.nome(), categoria);

		subCategoriaRepository.save(subCategoria);
		return subCategoria;
	}

	@Transactional
	public void deletarSubCategoriaPorId(Long id) {
		SubCategoria subCategoria = verificarSeExisteSubCategoriaPorId(id);
		subCategoriaRepository.delete(subCategoria);
	}

	public SubCategoria verificarSeExisteSubCategoriaPorId(Long idSubCategoria) {
		Optional<SubCategoria> optionalSubCategoria = subCategoriaRepository.findById(idSubCategoria);
		if (!optionalSubCategoria.isPresent()) {
			throw new ValidacaoException("Não existe categoria com o id " + idSubCategoria);
		}
		return optionalSubCategoria.get();
	}

}
