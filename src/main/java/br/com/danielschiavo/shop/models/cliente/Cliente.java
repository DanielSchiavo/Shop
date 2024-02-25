package br.com.danielschiavo.shop.models.cliente;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.danielschiavo.shop.models.endereco.Endereco;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "clientes")
@Entity(name = "Cliente")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cliente implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String cpf;
	private String nome;
	
	private String sobrenome;
	
	private LocalDate dataNascimento;
	
	private LocalDate dataCriacaoConta;
	
	private String email;
	private String senha;
	private String celular;
	private String foto_perfil;
	
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Endereco> enderecos = new ArrayList<>();
	
	public Cliente(ClienteDTO clienteDTO) {
		this.cpf = clienteDTO.cpf();
		this.nome = clienteDTO.nome();
		this.sobrenome = clienteDTO.sobrenome();
		this.dataNascimento = clienteDTO.data_nascimento();
		this.email = clienteDTO.email();
		this.senha = clienteDTO.senha();
		this.celular = clienteDTO.celular();
	}

	public void atualizarAtributos(AtualizarClienteDTO atualizarClienteDTO) {
		if (atualizarClienteDTO.cpf() != null) {
			this.cpf = atualizarClienteDTO.cpf();
		}
		if (atualizarClienteDTO.nome() != null) {
			this.nome = atualizarClienteDTO.nome();
		}
		if (atualizarClienteDTO.sobrenome() != null) {
			this.sobrenome = atualizarClienteDTO.sobrenome();
		}
		if (atualizarClienteDTO.dataNascimento() != null) {
			this.dataNascimento = atualizarClienteDTO.dataNascimento();
		}
		if (atualizarClienteDTO.email() != null) {
			this.email = atualizarClienteDTO.email();
		}
		if (atualizarClienteDTO.senha() != null) {
			this.senha = atualizarClienteDTO.senha();
		}
		if (atualizarClienteDTO.celular() != null) {
			this.celular = atualizarClienteDTO.celular();
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    List<GrantedAuthority> authorities = new ArrayList<>();

	    if (isAdmin()) {
	        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	    }

	    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

	    return authorities;
	}
	
	private boolean isAdmin() {
		if ("daniel.schiavo35@gmail.com".equals(this.getEmail())) {
			return true;
		}
		
		return false;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return senha;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
