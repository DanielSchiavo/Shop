package br.com.danielschiavo.shop.models.cartao;

import br.com.danielschiavo.shop.models.cliente.Cliente;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "clientes_cartoes")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cartao {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nomeBanco;
	
	private String numeroCartao;
	
	private String nomeNoCartao;
	
	private String validadeCartao;
	
	private Boolean cartaoPadrao;
	
	@Enumerated(EnumType.STRING)
	private TipoCartao tipoCartao;
	
	@ManyToOne
	private Cliente cliente;

	public Cartao(CartaoDTO cartaoDTO) {
		this.numeroCartao = cartaoDTO.numeroCartao();
		this.nomeNoCartao = cartaoDTO.nomeNoCartao();
		this.validadeCartao = cartaoDTO.validadeCartao();
		this.cartaoPadrao = cartaoDTO.cartaoPadrao();
		this.tipoCartao = cartaoDTO.tipoCartao();
	}
}