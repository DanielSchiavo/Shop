package br.com.danielschiavo.catalog.model.valueobject;


import br.com.danielschiavo.catalog.model.entity.Product;
import br.com.danielschiavo.catalog.model.enums.ProductFileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "products_files")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
@Builder
public class ProductFile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String fileName;

	@Enumerated(EnumType.STRING)
	private ProductFileType type;

	private String urlVideo;

	private Byte position;
	
	@ManyToOne
	private Product product;
}
