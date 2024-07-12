package br.com.danielschiavo.cliente.service.cliente;

import br.com.danielschiavo.cliente.dto.request.cliente.AlterarClienteRequest;
import br.com.danielschiavo.cliente.dto.request.cliente.CadastrarClienteRequest;
import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.enums.NomeRole;
import br.com.danielschiavo.cliente.repository.ClienteRepository;
import br.com.danielschiavo.filestorage.model.File;
import br.com.danielschiavo.filestorage.service.FileStoragePerfilService;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.cliente.mapper.ClienteMapper;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Service
@Setter
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private ClienteMapper mapper;
	
	@Autowired
	private FileStoragePerfilService fileStorageService;


	@Transactional
	public void deletarFotoPerfilPorId(Long clienteId) {
		Cliente cliente = repository.getReferenceById(clienteId);
		fileStorageService.deletarFotoPerfilNoDisco(cliente.getFotoPerfil());

		cliente.setFotoPerfil("Padrao.jpeg");
		repository.save(cliente);
	}

	public Page<Cliente> pegarTodosClientes(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Cliente pegarClientePorIdPaginaInicial(Long clienteId) {
		return repository.findByIdPaginaInicial(clienteId)
				.orElseThrow(() -> new ValidacaoException("Não existe um cliente com o id " + clienteId));
	}
	
	public Cliente pegarClientePorId(Long clienteId) {
		return repository.findById(clienteId)
				.orElseThrow(() -> new ValidacaoException("Não existe um cliente com o id " + clienteId));
	}
	
	@Transactional
	public Cliente cadastrarCliente(Cliente cliente) {
		return repository.save(cliente);
	}
	
	@Transactional
	public Cliente alterarClientePorId(Long clienteId, Cliente clienteAtualizado) {
		Cliente cliente = repository.getReferenceById(clienteId);
		mapper.alterarCliente(clienteAtualizado, cliente);
		
		return repository.save(cliente);
	}
	
	@Transactional
	public Cliente alterarFotoPerfilPorId(MultipartFile novaFoto, Long clienteId) {
		Cliente cliente = repository.getReferenceById(clienteId);

		File file = fileStorageService.alterarFotoPerfil(novaFoto, cliente.getFotoPerfil());

		cliente.setFotoPerfil(file.getFileName());
		return repository.save(cliente);
	}

	public void adicionarRole(Long clienteId, NomeRole nomeRole) {
		Cliente cliente = repository.getReferenceById(clienteId);
		cliente.adicionarRole(nomeRole);

		repository.save(cliente);
	}

	public void removerRole(Long clienteId, NomeRole nomeRole) {
		Cliente cliente = repository.getReferenceById(clienteId);
		cliente.removerRole(nomeRole);

		repository.save(cliente);
	}


//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
