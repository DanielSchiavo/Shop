package br.com.danielschiavo.shop.models.categoria;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "categorias")
@Entity(name = "Categoria")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Categoria implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, length = 80)
	private String nome;
	
	public Categoria(String nome) {
		this.nome = nome;
	}
}
