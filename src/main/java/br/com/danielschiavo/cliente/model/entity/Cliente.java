package br.com.danielschiavo.cliente.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.danielschiavo.cliente.model.enums.NomeRole;
import br.com.danielschiavo.cliente.model.valueobject.Role;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import lombok.Singular;
import lombok.ToString;

@Table(name = "clientes")
@Entity(name = "Cliente")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = {"id", "email", "cpf"})
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
	
	private String fotoPerfil;
	
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Singular
	private final Set<Role> roles = new HashSet<>();


	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(this.roles);
	}

	public void adicionarRole(Role role) {
		this.roles.add(role);
	}

	public void adicionarRole(NomeRole nomeRole) {
		this.roles.add(new Role(null, LocalDateTime.now(), nomeRole, this));
	}

	public void removerRole(Role role) {
		this.roles.remove(role);
	}

	public void removerRole(NomeRole nomeRole) {
		this.roles.forEach(role -> {
			if (role.getRole().equals(nomeRole)){
				this.roles.remove(role);
			}
		});
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
		return roles.stream().anyMatch(role -> role.getRole() == NomeRole.ADMIN);
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
