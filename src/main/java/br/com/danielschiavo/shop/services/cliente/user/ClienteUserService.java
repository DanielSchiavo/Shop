package br.com.danielschiavo.shop.services.cliente.user;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.mapper.cliente.ClienteMapper;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.dto.AlterarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.dto.AlterarFotoPerfilDTO;
import br.com.danielschiavo.shop.models.cliente.dto.CadastrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.dto.MostrarClienteDTO;
import br.com.danielschiavo.shop.models.cliente.endereco.Endereco;
import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.repositories.cliente.ClienteRepository;
import br.com.danielschiavo.shop.services.filestorage.FileStoragePerfilService;
import lombok.Setter;

@Service
@Setter
public class ClienteUserService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private FileStoragePerfilService fileService;
	
	@Autowired
	private ClienteMapper clienteMapper;
	
	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;

	@Transactional
	public void deletarFotoPerfilPorIdToken() throws IOException {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		if (cliente.getFotoPerfil().equals("Padrao.jpeg")) {
			throw new ValidacaoException("O cliente não tem foto de perfil, ele já está com a foto padrão, portanto, não é possível deletar");
		}
		fileService.deletarFotoPerfilNoDisco(cliente.getFotoPerfil());
		cliente.setFotoPerfil("Padrao.jpeg");
		clienteRepository.save(cliente);
	}
	
	public MostrarClienteDTO detalharClientePorIdTokenPaginaInicial() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		return clienteMapper.clienteParaMostrarClienteDtoPaginaInicial(cliente, fileService);
	}
	
	public MostrarClienteDTO detalharClientePorIdToken() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		return clienteMapper.clienteParaMostrarClienteDTO(cliente, fileService);
	}
	
	@Transactional
	public MostrarClienteDTO cadastrarCliente(CadastrarClienteDTO clienteDTO) {
		Cliente cliente = clienteMapper.cadastrarClienteDtoParaCliente(clienteDTO);
		if (clienteDTO.endereco() != null) {
			Endereco endereco = clienteMapper.cadastrarClienteDtoParaEndereco(clienteDTO, cliente);
			cliente.adicionarEndereco(endereco);
		}
		clienteRepository.save(cliente);
		return clienteMapper.clienteParaMostrarClienteDTO(cliente, fileService);
	}
	
	@Transactional
	public MostrarClienteDTO alterarClientePorIdToken(AlterarClienteDTO alterarClienteDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		clienteMapper.alterarClienteDtoSetarAtributosEmCliente(alterarClienteDTO, cliente);
		return clienteMapper.clienteParaMostrarClienteDTO(cliente, fileService);
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
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

}
