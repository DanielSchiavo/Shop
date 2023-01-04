package br.com.danielschiavo.shop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.category.Category;
import br.com.danielschiavo.shop.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;

	public void save(Category category) {
		repository.save(category);
	}

	public Category getReferenceById(Long id) {
		return repository.getReferenceById(id);
	}
	
	public List<Category> findAll(){
		return repository.findAll();
	}
	
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
