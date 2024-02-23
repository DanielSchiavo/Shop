package br.com.danielschiavo.shop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.categoria.Categoria;
import br.com.danielschiavo.shop.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	public Categoria cadastrarCategoria(String name) {
		var categoria = new Categoria(name);
		return categoriaRepository.save(categoria);
	}

	public Categoria atualizarNomePorId(Long id, String name) {
		Categoria categoria = categoriaRepository.getReferenceById(id);
		categoria.setNome(name);
		return categoriaRepository.save(categoria);
	}
}
