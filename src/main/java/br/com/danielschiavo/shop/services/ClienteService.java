package br.com.danielschiavo.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.ClienteDTO;
import br.com.danielschiavo.shop.models.cliente.DetalharClienteDTO;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.repositories.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clientRepository;
	
	@Autowired
	private EnderecoService adressService;
	
	@Autowired
	private PedidoService pedidoService;
	
	private TokenJWTService tokenJWTService;

	public Cliente createAndSave(ClienteDTO clientDTO) {
		Endereco endereco = null;
		var client = new Cliente(clientDTO);
		
		if (clientDTO.endereco() != null) {
			endereco = new Endereco(clientDTO);
			endereco.setCliente(client);
			adressService.save(endereco);
			client.getEnderecos().add(endereco);
		}

		clientRepository.save(client);
		
		return client;
	}
	
	public Page<DetalharClienteDTO> pegarTodosClientes(Pageable pageable) {
		Page<Cliente> pageClientes = clientRepository.findAll(pageable);
		return pageClientes.map(this::converterParaDetalharClienteDTO);
	}

	public Cliente getReferenceById(Long id) {
		return clientRepository.getReferenceById(id);
	}

	public void deleteById(Long id) {
		clientRepository.deleteById(id);
	}

	public Cliente pegarEVerificarId(Long clientId) {
		Optional<Cliente> optionalCliente = clientRepository.findById(clientId);
	    if (optionalCliente.isPresent()) {
	        Cliente cliente = optionalCliente.get();
	        return cliente;
	    } else {
	    	throw new RuntimeException("Não existe Cliente com esse ID ");
	    }
	}

	private DetalharClienteDTO converterParaDetalharClienteDTO(Cliente cliente) {
	    return new DetalharClienteDTO(cliente);
	}

	public DetalharClienteDTO detalharClientePorId() {
		Long id = tokenJWTService.getClaimIdJWT();
		Cliente cliente = pegarEVerificarId(id);
		return new DetalharClienteDTO(cliente);
	}
	
	public DetalharClienteDTO detalharClientePorIdMaisTodosOsPedidos(Long id) {
		Cliente cliente = pegarEVerificarId(id);
		List<Pedido> listPedidos = pedidoService.pegarPedidosPeloIdDoCliente(id);
		return new DetalharClienteDTO(cliente);
	}

}
