package br.com.danielschiavo.shop.models.product;

import java.math.BigDecimal;

import br.com.danielschiavo.shop.models.subcategory.SubCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "products")
@Entity(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	private String description;
	
	@Column(name="first_image")
	private String firstImage;
	
	private String images;
	private BigDecimal price;
	private Integer quantity;
	private Boolean active;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private SubCategory subCategory;
	
	public Product(@Valid ProductDTO dto) {
		this.name = dto.name();
		this.description = dto.description();
		this.images = dto.images();
		this.price = dto.price();
		this.quantity = dto.quantity();
		this.active = dto.active();
		this.subCategory = dto.subCategory();
	}

	public void changeActive() {
		if (active) {
			this.active = false;
		} else {
			this.active = true;
		}
	}
	
	public void updateAttributes(UpdateProductDTO productDTO) {
		if (productDTO.name() != null) {
			this.name = productDTO.name();
		}
		if (productDTO.description() != null) {
			this.description = productDTO.description();
		}
		if (productDTO.images() != null) {
			this.images = productDTO.images();
		}
		if (productDTO.price() != null) {
			this.price = productDTO.price();
		}
		if (productDTO.quantity() != null) {
			this.quantity = productDTO.quantity();
		}
		if (productDTO.active() != null) {
			this.active = productDTO.active();
		}
		if (productDTO.subCategory() != null) {
			this.subCategory = productDTO.subCategory();
		}
	}
}
