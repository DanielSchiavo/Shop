package br.com.danielschiavo.shop.models.subcategory;

import br.com.danielschiavo.shop.controllers.UpdateSubCategoryDTO;
import br.com.danielschiavo.shop.models.category.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "sub_category")
@Entity(name = "SubCategory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SubCategory {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Category category;

	public SubCategory(SubCategoryDTO dto) {
		this.id = dto.id();
		this.name = dto.name();
	}

	public void updateAttributes(UpdateSubCategoryDTO subCategoryDTO) {
		if (subCategoryDTO.name() != null) {
			this.name = subCategoryDTO.name();
		}
		if (subCategoryDTO.category() != null) {
			this.category = subCategoryDTO.category();
		}
	}
}
