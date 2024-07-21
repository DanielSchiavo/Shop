package br.com.danielschiavo.customer.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.danielschiavo.customer.model.enums.RoleName;
import br.com.danielschiavo.customer.model.valueobject.Role;
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

@Table(name = "customers")
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(of = {"id", "email", "cpf"})
public class Customer implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String cpf;
	
	private String name;
	
	private String surname;
	
	private LocalDate birthDate;
	
	private LocalDate accountCreationDate;
	
	private String email;
	
	private String password;
	
	private String cellphoneNumber;
	
	private String profilePicture;
	
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Singular
	private final Set<Role> roles = new HashSet<>();


	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(this.roles);
	}

	public void adicionarRole(Role role) {
		this.roles.add(role);
	}

	public void adicionarRole(RoleName roleName) {
		this.roles.add(new Role(null, LocalDateTime.now(), roleName, this));
	}

	public void removerRole(Role role) {
		this.roles.remove(role);
	}

	public void removerRole(RoleName roleName) {
		this.roles.forEach(role -> {
			if (role.getRole().equals(roleName)){
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
		return roles.stream().anyMatch(role -> role.getRole() == RoleName.ADMIN);
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return password;
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
