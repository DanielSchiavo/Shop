package br.com.danielschiavo.shop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.subcategory.SubCategory;
import br.com.danielschiavo.shop.repositories.SubCategoryRepository;

@Service
public class SubCategoryService {
	
	@Autowired
	private SubCategoryRepository repository;

	public void save(SubCategory subCategory) {
		repository.save(subCategory);
	}

	public SubCategory getReferenceById(Long id) {
		return repository.getReferenceById(id);
	}

	public List<SubCategory> findAll() {
		return repository.findAll();
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
