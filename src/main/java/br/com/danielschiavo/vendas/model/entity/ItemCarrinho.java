package br.com.danielschiavo.vendas.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "clientes_carrinhos_items")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ItemCarrinho {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private Integer quantidade;
    
    private Long produtoId;
    
    private BigDecimal subTotal;
    
    @Column(name = "data_e_hora_insercao")
    private LocalDateTime dataEHoraInsercao;
    
    @Column(name = "data_e_hora_atualizacao")
    private LocalDateTime dataEHoraAtualizacao;
	
	@ManyToOne
	private Carrinho carrinho;
}
