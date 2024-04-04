package br.com.danielschiavo.shop.services.cliente.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.mapper.cliente.ClienteMapper;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.dto.MostrarClienteDTO;
import br.com.danielschiavo.shop.repositories.cliente.ClienteRepository;
import br.com.danielschiavo.shop.services.filestorage.FileStoragePerfilService;
import lombok.Setter;

@Service
@Setter
public class ClienteAdminService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FileStoragePerfilService fileService;
	
	@Autowired
	private ClienteMapper clienteMapper;
	

	public void adminDeletarClientePorId(Long idCliente) {
		clienteRepository.deleteById(idCliente);
	}
	
	public Page<MostrarClienteDTO> adminDetalharTodosClientes(Pageable pageable) {
		Page<Cliente> pageClientes = clienteRepository.findAll(pageable);
		return pageClientes.map(cliente -> clienteMapper.clienteParaMostrarClienteDTO(cliente, fileService));
	}

//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	
}
