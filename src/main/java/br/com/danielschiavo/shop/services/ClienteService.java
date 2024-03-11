package br.com.danielschiavo.shop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.cliente.AlterarFotoPerfilDTO;
import br.com.danielschiavo.shop.models.cliente.AtualizarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.ClienteDTO;
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
	private EnderecoService enderecoService;
	
	@Autowired
	private FileStorageService fileService;
	
	@Autowired
	private TokenJWTService tokenJWTService;

	@Transactional
	public Cliente cadastrarCliente(ClienteDTO clientDTO) {
		var cliente = new Cliente(clientDTO);
		System.out.println(" TESTE ");
		if (clientDTO.endereco() != null) {
			Endereco endereco = new Endereco(clientDTO, cliente);
			enderecoService.save(endereco);
			cliente.getEnderecos().add(endereco);
			System.out.println(" TESTE ");
		}

		clienteRepository.save(cliente);
		
		return cliente;
	}
	
	public Page<MostrarClienteDTO> pegarTodosClientes(Pageable pageable) {
		Page<Cliente> pageClientes = clienteRepository.findAll(pageable);
		return pageClientes.map(this::converterParaDetalharClienteDTO);
	}

	private MostrarClienteDTO converterParaDetalharClienteDTO(Cliente cliente) {
	    return new MostrarClienteDTO(cliente);
	}

	public MostrarClienteDTO detalharClientePorId() {
		Long id = tokenJWTService.getClaimIdJWT();
		Cliente cliente = clienteRepository.findById(id).get();
		return new MostrarClienteDTO(cliente);
	}

	@Transactional
	public Cliente atualizarClientePorId(AtualizarClienteDTO updateClientDTO) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		var cliente = clienteRepository.getReferenceById(idCliente);
		cliente.atualizarAtributos(updateClientDTO);
		return cliente;
	}

	@Transactional
	public ArquivoInfoDTO alterarFotoPerfil(AlterarFotoPerfilDTO alterarFotoPerfilDTO) {
		var idCliente = tokenJWTService.getClaimIdJWT();
		Cliente cliente = verificarSeClienteExistePorId(idCliente);
		
		String nomeNovaFotoPerfil = alterarFotoPerfilDTO.nomeNovaFotoPerfil();
		
		fileService.verificarSeExisteFotoPerfilPorNome(nomeNovaFotoPerfil);
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(nomeNovaFotoPerfil);
		
		cliente.setFotoPerfil(nomeNovaFotoPerfil);
		clienteRepository.save(cliente);
		
		return arquivoInfoDTO;
	}

	public MostrarClientePaginaInicialDTO pegarDadosParaExibirNaPaginaInicial(Long id) {
		Cliente cliente = clienteRepository.findById(id).get();
		ArquivoInfoDTO arquivoInfoDTO = fileService.pegarFotoPerfilPorNome(cliente.getFotoPerfil());
		return new MostrarClientePaginaInicialDTO(cliente.getNome(), arquivoInfoDTO.bytesArquivo());
	}
	
	@Transactional
	public void deletarFotoPerfil() {
		var idCliente = tokenJWTService.getClaimIdJWT();
		Cliente cliente = verificarSeClienteExistePorId(idCliente);
		if (cliente.getFotoPerfil().equals("Padrao.jpeg")) {
			throw new ValidacaoException("O cliente não tem foto de perfil, ele já está com a foto padrão, portanto, não é possível deletar");
		}
		fileService.deletarFotoPerfilNoDisco(cliente.getFotoPerfil());
		cliente.setFotoPerfil("Padrao.jpeg");
		
		clienteRepository.save(cliente);
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
