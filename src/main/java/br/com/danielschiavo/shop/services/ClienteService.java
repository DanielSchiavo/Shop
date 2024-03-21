package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.cliente.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.AlterarFotoPerfilDTO;
import br.com.danielschiavo.shop.models.cliente.CadastrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClientePaginaInicialDTO;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.repositories.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FileStorageService fileService;
	
	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;

	@Transactional
	public void deletarFotoPerfilPorIdToken() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		if (cliente.getFotoPerfil().equals("Padrao.jpeg")) {
			throw new ValidacaoException("O cliente não tem foto de perfil, ele já está com a foto padrão, portanto, não é possível deletar");
		}
		fileService.deletarFotoPerfilNoDisco(cliente.getFotoPerfil());
		cliente.setFotoPerfil("Padrao.jpeg");
		
		clienteRepository.save(cliente);
	}
	
	public MostrarClientePaginaInicialDTO detalharClientePorIdTokenPaginaInicial() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(cliente.getFotoPerfil());
		return new MostrarClientePaginaInicialDTO(cliente.getNome(), arquivoInfoDTO);
	}
	
	public MostrarClienteDTO detalharClientePorIdToken() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(cliente.getFotoPerfil());
		return new MostrarClienteDTO(cliente, arquivoInfoDTO);
	}
	
	@Transactional
	public MostrarClienteDTO cadastrarCliente(CadastrarClienteDTO clientDTO) {
		var cliente = new Cliente(clientDTO);
		if (clientDTO.endereco() != null) {
			Endereco endereco = new Endereco(clientDTO, cliente);
			cliente.getEnderecos().add(endereco);
		}

		clienteRepository.save(cliente);
		
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(cliente.getFotoPerfil());
		
		return new MostrarClienteDTO(cliente, arquivoInfoDTO);
	}
	
	@Transactional
	public MostrarClienteDTO alterarClientePorIdToken(AlterarClienteDTO updateClientDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		cliente.atualizarAtributos(updateClientDTO);
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(cliente.getFotoPerfil());
		return new MostrarClienteDTO(cliente, arquivoInfoDTO);
	}
	
	@Transactional
	public ArquivoInfoDTO alterarFotoPerfilPorIdToken(AlterarFotoPerfilDTO alterarFotoPerfilDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		
		String nomeNovaFotoPerfil = alterarFotoPerfilDTO.nomeNovaFotoPerfil();
		
		fileService.verificarSeExisteFotoPerfilPorNome(nomeNovaFotoPerfil);
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(nomeNovaFotoPerfil);
		
		cliente.setFotoPerfil(nomeNovaFotoPerfil);
		clienteRepository.save(cliente);
		
		return arquivoInfoDTO;
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS PARA ADMINISTRADORES
//	------------------------------
//	------------------------------
	
	public void adminDeletarClientePorId(Long idCliente) {
		clienteRepository.deleteById(idCliente);
	}
	
	public Page<MostrarClienteDTO> adminDetalharTodosClientes(Pageable pageable) {
		Page<Cliente> pageClientes = clienteRepository.findAll(pageable);
		return pageClientes.map(this::converterParaDetalharClienteDTO);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	private MostrarClienteDTO converterParaDetalharClienteDTO(Cliente cliente) {
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(cliente.getFotoPerfil());
	    return new MostrarClienteDTO(cliente, arquivoInfoDTO);
	}

	public Cliente verificarSeClienteExistePorId(Long id) {
		Optional<Cliente> optionalCliente = clienteRepository.findById(id);
		if (optionalCliente.isPresent()) {
			return optionalCliente.get();
		} else {
			throw new ValidacaoException("Não existe um cliente com o id " + id);
		}
	}

	

	


}
