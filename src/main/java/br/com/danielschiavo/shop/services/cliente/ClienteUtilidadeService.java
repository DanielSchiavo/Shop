package br.com.danielschiavo.shop.services.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.repositories.cliente.ClienteRepository;
import lombok.Setter;

@Service
@Setter
public class ClienteUtilidadeService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente verificarSeClienteExistePorId(Long id) {
		return clienteRepository.findById(id).orElseThrow(() -> new ValidacaoException("Não existe um cliente com o id " + id));
	}
	
}
