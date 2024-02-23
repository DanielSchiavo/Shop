package br.com.danielschiavo.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria salvar(String name) {
		var categoria = new Categoria(name);
		return categoriaRepository.save(categoria);
	}

	public Categoria getReferenceById(Long id) {
		return categoriaRepository.getReferenceById(id);
	}
	
	public Page<Categoria> pegarCategorias(Pageable pageable){
		return categoriaRepository.findAll(pageable);
	}
	
	public void deleteById(Long id) {
		categoriaRepository.deleteById(id);
	}

	public Categoria alterarNomeCategoria(Long id, String name) {
		Categoria categoria = categoriaRepository.getReferenceById(id);
		categoria.setNome(name);
		return categoriaRepository.save(categoria);
	}
}
