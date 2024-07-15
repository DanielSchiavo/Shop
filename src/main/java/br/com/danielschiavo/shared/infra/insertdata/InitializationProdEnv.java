package br.com.danielschiavo.shared.infra.insertdata;

import java.time.LocalDate;

import br.com.danielschiavo.customer.model.entity.Customer;
import br.com.danielschiavo.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.customer.model.enums.RoleName;
import br.com.danielschiavo.customer.model.valueobject.Role;
import br.com.danielschiavo.customer.model.valueobject.Role.RoleBuilder;

@Profile("prod")
@Component
public class InitializationProdEnv implements CommandLineRunner {
	
	@Autowired
	private CustomerRepository clienteRepository;
	
	private Customer.ClienteBuilder clienteBuilder = Customer.builder();
	private RoleBuilder roleBuilder = Role.builder();
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		inserirDado();
	}

	public void inserirDado() {
		boolean resultado = clienteRepository.findByCpf("12345678912").isEmpty();
		
		if (resultado) {
			Customer customer = clienteBuilder
					.id(null)
					.cpf("12345678912")
					.nome("Daniel")
					.sobrenome("Schiavo Rosseto")
					.dataNascimento(LocalDate.of(2000, 3, 3))
					.dataCriacaoConta(LocalDate.now())
					.email("daniel.schiavo35@gmail.com")
					.senha("$2a$12$g/401MRFl.y7b4x5jOPjeu5d31oI9a.uI9WL1pWXR.0ocFj9J/DNu")
					.celular("27996121255")
					.fotoPerfil("Padrao.jpeg")
					.build();
			
			customer.adicionarRole(roleBuilder.id(null)
					.role(RoleName.ADMIN)
					.customer(customer).build());
			
			clienteRepository.save(customer);
			
			
		}
	}
}
