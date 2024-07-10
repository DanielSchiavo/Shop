package br.com.danielschiavo.cliente.model.valueobject;

import java.time.LocalDateTime;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.enums.NomeRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "clientes_roles")
@Entity(name = "Role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "cliente")
@Builder
@EqualsAndHashCode
public class Role {

	@Id 
	private Long id;
	
	@Column(name = "data_e_hora_atribuicao")
	private LocalDateTime dataEHoraAtribuicao;
	
	@Enumerated(EnumType.STRING)
	private NomeRole role;
	
	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
	private Cliente cliente;
	
}
