package br.com.danielschiavo.shop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.client.Client;
import br.com.danielschiavo.shop.repositories.ClientRepository;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;

	public void save(Client client) {
		repository.save(client);
	}
	
	public List<Client> getAllClients() {
		return repository.findAll();
	}

	public Client getReferenceById(Long id) {
		return repository.getReferenceById(id);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
