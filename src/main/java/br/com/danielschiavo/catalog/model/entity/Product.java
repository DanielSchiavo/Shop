package br.com.danielschiavo.catalog.model.entity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.danielschiavo.catalog.model.enums.ProductDeliveryType;
import br.com.danielschiavo.catalog.model.valueobject.ProductFile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Table(name = "products")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	private BigDecimal price;

	private Integer quantity;
	
	private Boolean active;

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<ProductDeliveryType> deliveryTypes = new HashSet<>();

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
	private Set<ProductFile> productFiles = new HashSet<>();
    
	private Long subCategoryId;

	
	
	public Set<ProductDeliveryType> getDeliveryTypes() {
		return Collections.unmodifiableSet(this.deliveryTypes);
	}

	public void addDeliveryType(ProductDeliveryType tipoEntrega) {
		this.deliveryTypes.add(tipoEntrega);
	}
	
    public void addDeliveryType(Set<ProductDeliveryType> tiposEntrega) {
        this.deliveryTypes.addAll(tiposEntrega);
    }


	public Set<ProductFile> getProductFiles() {
		return Collections.unmodifiableSet(this.productFiles);
	}

	public void addProductFile(ProductFile productFile) {
		this.productFiles.add(productFile);
	}
	
    public void addProductFiles(Set<ProductFile> arquivosProduto) {
        this.productFiles.addAll(arquivosProduto);
    }

	public List<String> getAllImageNames() {
		return getProductFiles().stream().map(ProductFile::getName).collect(Collectors.toList());
	}

	public String getNameFirstImage() {
		return getProductFiles().stream().filter(ap -> ap.getPosition().equals((byte) 0)).findFirst().get().getName();
	}
}
