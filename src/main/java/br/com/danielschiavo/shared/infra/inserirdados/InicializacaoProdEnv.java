package br.com.danielschiavo.shared.infra.inserirdados;

import java.time.LocalDate;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.cliente.model.enums.NomeRole;
import br.com.danielschiavo.cliente.model.entity.Role;
import br.com.danielschiavo.cliente.model.entity.Role.RoleBuilder;

@Profile("prod")
@Component
public class InicializacaoProdEnv implements CommandLineRunner {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	private Cliente.ClienteBuilder clienteBuilder = Cliente.builder();
	private RoleBuilder roleBuilder = Role.builder();
	
	@Override
	@Transactional
	public void run(String... args) throws Exception {
		inserirDado();
	}

	public void inserirDado() {
		boolean resultado = clienteRepository.findByCpf("12345678912").isEmpty();
		
		if (resultado) {
			Cliente cliente = clienteBuilder
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
			
			cliente.adicionarRole(roleBuilder.id(null)
					.role(NomeRole.ADMIN)
					.cliente(cliente).build());
			
			clienteRepository.save(cliente);
			
			
		}
	}
}
