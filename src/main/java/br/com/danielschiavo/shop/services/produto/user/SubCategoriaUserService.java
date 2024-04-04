package br.com.danielschiavo.shop.services.produto.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.produto.categoria.MostrarCategoriaDTO;
import br.com.danielschiavo.shop.models.produto.subcategoria.MostrarSubCategoriaComCategoriaDTO;
import br.com.danielschiavo.shop.models.produto.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.repositories.produto.SubCategoriaRepository;

@Service
public class SubCategoriaUserService {

	@Autowired
	private SubCategoriaRepository subCategoriaRepository;

	public Page<MostrarSubCategoriaComCategoriaDTO> listarSubCategorias(Pageable pageable) {
		Page<SubCategoria> pageSubCategoria = subCategoriaRepository.findAll(pageable);
		return pageSubCategoria.map(subCategoria -> new MostrarSubCategoriaComCategoriaDTO(subCategoria.getId(), subCategoria.getNome(), new MostrarCategoriaDTO(subCategoria.getCategoria().getId(), subCategoria.getCategoria().getNome())));
	}

}
