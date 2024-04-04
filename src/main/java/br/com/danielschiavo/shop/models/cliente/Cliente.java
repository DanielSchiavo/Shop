package br.com.danielschiavo.shop.models.cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.danielschiavo.shop.models.cliente.carrinho.Carrinho;
import br.com.danielschiavo.shop.models.cliente.carrinho.itemcarrinho.ItemCarrinho;
import br.com.danielschiavo.shop.models.cliente.cartao.Cartao;
import br.com.danielschiavo.shop.models.cliente.endereco.Endereco;
import br.com.danielschiavo.shop.models.cliente.role.NomeRole;
import br.com.danielschiavo.shop.models.cliente.role.Role;
import br.com.danielschiavo.shop.models.produto.Produto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "clientes")
@Entity(name = "Cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
	private Set<Role> roles = new HashSet<>();
	
    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Endereco> enderecos = new ArrayList<>();

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Cartao> cartoes = new ArrayList<>();
	
	@OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Carrinho carrinho;
	
	
	
	
	public List<Cartao> getCartoes() {
		return Collections.unmodifiableList(this.cartoes);
	}

	public void adicionarCartao(Cartao cartao) {
		this.cartoes.add(cartao);
	}

	public List<Endereco> getEnderecos() {
		return Collections.unmodifiableList(this.enderecos);
	}

	public void adicionarEndereco(Endereco endereco) {
		this.enderecos.add(endereco);
	}
	
	public Set<Role> getRoles() {
		return Collections.unmodifiableSet(this.roles);
	}

	public void adicionarRole(Role role) {
		this.roles.add(role);
	}
	
	public void removerRole(Role role) {
		this.roles.remove(role);
	}
	
	
	
	public static ClienteBuilder builder() {
		return new ClienteBuilder();
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

	
	
	public static class ClienteBuilder {
		
		private Cliente cliente;
		private List<Cliente> listaClientes = new ArrayList<>();
		
		public ClienteBuilder id(Long id) {
	        if(this.cliente != null) {
	        	listaClientes.add(this.cliente);
	        }
			this.cliente = new Cliente();
	        this.cliente.setId(id);
			return this;
		}

		public ClienteBuilder cpf(String cpf) {
	        this.cliente.setCpf(cpf);
			return this;
		}
		
		public ClienteBuilder nome(String nome) {
	        this.cliente.setNome(nome);
			return this;
		}
		
		public ClienteBuilder sobrenome(String sobrenome) {
	        this.cliente.setSobrenome(sobrenome);
			return this;
		}
		
		public ClienteBuilder dataNascimento(LocalDate dataNascimento) {
	        this.cliente.setDataNascimento(dataNascimento);
			return this;
		}
		
		public ClienteBuilder dataCriacaoConta(LocalDate dataCriacaoConta) {
	        this.cliente.setDataCriacaoConta(dataCriacaoConta);
			return this;
		}
		
		public ClienteBuilder email(String email) {
	        this.cliente.setEmail(email);
			return this;
		}

		public ClienteBuilder senha(String senha) {
	        this.cliente.setSenha(senha);
			return this;
		}
		
		public ClienteBuilder celular(String celular) {
	        this.cliente.setCelular(celular);
			return this;
		}
		
		public ClienteBuilder fotoPerfil(String fotoPerfil) {
	        this.cliente.setFotoPerfil(fotoPerfil);
			return this;
		}
		
		public ClienteBuilder adicionarRole(Role role) {
			role.setCliente(this.cliente);
			role.setDataAtribuicao(LocalDateTime.now());
			this.cliente.adicionarRole(role);
			return this;
		}
		
		public ClienteBuilder adicionarEndereco(Endereco endereco) {
			endereco.setCliente(this.cliente);
			this.cliente.adicionarEndereco(endereco);
			return this;
		}
		
		public ClienteBuilder adicionarCartao(Cartao cartao) {
			cartao.setCliente(this.cliente);
			this.cliente.adicionarCartao(cartao);
			return this;
		}
		
	    public ClienteBuilder carrinho(boolean trueOrFalse) {
	    	if (trueOrFalse == true) {
	    		Carrinho carrinho = new Carrinho();
	    		carrinho.setDataAtualizacao(LocalDateTime.now());
	    		carrinho.setCliente(this.cliente);
	    		this.cliente.setCarrinho(carrinho);
	    		return this;
	    	}
	    	return this;
	    }
	    
		public ClienteBuilder comItemCarrinhoIdQuantidadeProduto(Long id, Integer quantidade, Produto produto) {
			ItemCarrinho itemCarrinho = new ItemCarrinho(id, quantidade, produto, this.cliente.getCarrinho());
			this.cliente.getCarrinho().adicionarItemCarrinho(itemCarrinho);
			return this;
		}
	    
	    public Cliente getCliente() {
	        if(this.cliente != null) {
	        	Cliente copiaCliente = new Cliente();
	        	copiaCliente.setId(this.cliente.getId());
	        	copiaCliente.setCpf(this.cliente.getCpf());
	        	copiaCliente.setNome(this.cliente.getNome());
	        	copiaCliente.setSobrenome(this.cliente.getSobrenome());
	        	copiaCliente.setDataNascimento(this.cliente.getDataNascimento());
	        	copiaCliente.setDataCriacaoConta(this.cliente.getDataCriacaoConta());
	        	copiaCliente.setEmail(this.cliente.getEmail());
	        	copiaCliente.setSenha(this.cliente.getSenha());
	        	copiaCliente.setCelular(this.cliente.getCelular());
	        	copiaCliente.setFotoPerfil(this.cliente.getFotoPerfil());
	        	this.cliente.getEnderecos().forEach(endereco -> {
	        		copiaCliente.adicionarEndereco(new Endereco(endereco.getId(), endereco.getCep(), endereco.getRua(), endereco.getNumero(), endereco.getComplemento(), endereco.getBairro(), endereco.getCidade(), endereco.getEstado(), endereco.getEnderecoPadrao(), copiaCliente));
	        	});
	        	this.cliente.getCartoes().forEach(cartao -> {
	        		copiaCliente.adicionarCartao(new Cartao(cartao.getId(), cartao.getNomeBanco(), cartao.getNumeroCartao(), cartao.getNomeNoCartao(), cartao.getValidadeCartao(), cartao.getCartaoPadrao(), cartao.getTipoCartao(), copiaCliente));
	        	});
	        	this.cliente = null;
	        	return copiaCliente;
	        }
	        return null;
	    }
	    
	    public List<Cliente> getClientes() {
	        if(this.cliente != null) {
	            listaClientes.add(this.cliente); // Adiciona o último produto configurado à lista
	            this.cliente = null; // Reseta o produto atual da fábrica
	        }
	        List<Cliente> novaLista = new ArrayList<>(listaClientes);
	        listaClientes.clear();
	        return novaLista;
	    }
	}
}
