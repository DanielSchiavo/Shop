package br.com.danielschiavo.shop.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.danielschiavo.shop.infra.security.TokenJWTService;
import br.com.danielschiavo.shop.models.cliente.AtualizarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.ClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClientePaginaInicialDTO;
import br.com.danielschiavo.shop.models.cliente.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.MensagemEFotoPerfilDTO;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.pedido.Pedido;
import br.com.danielschiavo.shop.repositories.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clientRepository;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private FilesStorageService fileService;
	
	private TokenJWTService tokenJWTService;

	public Cliente cadastrarCliente(ClienteDTO clientDTO) {
		Endereco endereco = null;
		var cliente = new Cliente(clientDTO);
		
		if (clientDTO.endereco() != null) {
			endereco = new Endereco(clientDTO, cliente);
			enderecoService.save(endereco);
			cliente.getEnderecos().add(endereco);
		}

		clientRepository.save(cliente);
		
		return cliente;
	}
	
	public Page<MostrarClienteDTO> pegarTodosClientes(Pageable pageable) {
		Page<Cliente> pageClientes = clientRepository.findAll(pageable);
		return pageClientes.map(this::converterParaDetalharClienteDTO);
	}

	private MostrarClienteDTO converterParaDetalharClienteDTO(Cliente cliente) {
	    return new MostrarClienteDTO(cliente);
	}

	public MostrarClienteDTO detalharClientePorId() {
		Long id = tokenJWTService.getClaimIdJWT();
		Cliente cliente = clientRepository.findById(id).get();
		return new MostrarClienteDTO(cliente);
	}
	
	public MostrarClienteDTO detalharClientePorIdMaisTodosOsPedidos(Long id) {
		Cliente cliente = clientRepository.findById(id).get();
		List<Pedido> listPedidos = pedidoService.pegarPedidosPeloIdDoCliente(id);
		return new MostrarClienteDTO(cliente);
	}

	public Cliente atualizarClientePorId(Long id, AtualizarClienteDTO updateClientDTO) {
		Cliente cliente = clientRepository.getReferenceById(id);
		cliente.atualizarAtributos(updateClientDTO);
		return cliente;
	}

	public MensagemEFotoPerfilDTO atualizarFotoPerfil(Long id, MultipartFile novaImagem) {
		Cliente cliente = clientRepository.getReferenceById(id);
		
		String nomeFotoPerfil = fileService.gerarNovoNomeFotoPerfil(cliente.getId(), novaImagem);

		cliente.setFoto_perfil(nomeFotoPerfil);
		clientRepository.save(cliente);
		
		fileService.salvarNoDiscoFotoPerfil(nomeFotoPerfil, novaImagem);
		
		try {
			byte[] bytesImagemPerfil = fileService.pegarFotoPerfil(nomeFotoPerfil);
			String mensagem = "Alterou a imagem de perfil com sucesso! ";
			return new MensagemEFotoPerfilDTO(mensagem, bytesImagemPerfil);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao recuperar foto do perfil");
		}
	}

	public MostrarClientePaginaInicialDTO pegarDadosParaExibirNaPaginaInicial(Long id) {
		Cliente cliente = clientRepository.findById(id).get();
		try {
			byte[] bytesFotoPerfil = fileService.pegarFotoPerfil(cliente.getFoto_perfil());
			return new MostrarClientePaginaInicialDTO(cliente.getNome(), bytesFotoPerfil);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao recuperar foto do perfil");
		}
	}
	
	public void deletarFotoPerfil(Long id) {
		Cliente cliente = clientRepository.findById(id).get();

		fileService.deletarFotoPerfilNoDisco(cliente.getFoto_perfil());
	}

}
