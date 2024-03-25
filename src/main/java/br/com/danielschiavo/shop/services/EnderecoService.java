package br.com.danielschiavo.shop.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.infra.security.UsuarioAutenticadoService;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.endereco.AlterarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.CadastrarEnderecoDTO;
import br.com.danielschiavo.shop.models.endereco.Endereco;
import br.com.danielschiavo.shop.models.endereco.MostrarEnderecoDTO;
import br.com.danielschiavo.shop.repositories.EnderecoRepository;

@Service
public class EnderecoService {
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private UsuarioAutenticadoService usuarioAutenticadoService;
	
	@Transactional
	public void deletarEnderecoPorIdToken(Long idEndereco) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		Endereco endereco = verificarSeEnderecoExistePorIdEnderecoECliente(idEndereco, cliente);
		
		enderecoRepository.delete(endereco);
	}
	
	public List<MostrarEnderecoDTO> pegarEnderecosClientePorIdToken() {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		List<Endereco> enderecos = cliente.getEnderecos();
		if (enderecos.isEmpty()) {
			throw new ValidacaoException("Cliente não possui nenhum endereço cadastrado");
		}
		
		return enderecos.stream()
				.map(MostrarEnderecoDTO::converterParaMostrarEnderecoDTO).toList();
	}
	
	@Transactional
	public MostrarEnderecoDTO cadastrarNovoEnderecoPorIdToken(CadastrarEnderecoDTO novoEnderecoDTO) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		Endereco novoEndereco = new Endereco(novoEnderecoDTO);
		novoEndereco.setCliente(cliente);
		
		List<Endereco> enderecos = cliente.getEnderecos();
		enderecos.add(novoEndereco);
		if (novoEnderecoDTO.enderecoPadrao() == true && !enderecos.isEmpty()) {
			enderecos.forEach(endereco -> {
				if (endereco.getEnderecoPadrao() == true) {
					endereco.setEnderecoPadrao(false);
					enderecoRepository.save(endereco);
				}
			});
		}
		
		if (novoEnderecoDTO.enderecoPadrao() == false && enderecos.isEmpty()) {
			novoEndereco.setEnderecoPadrao(true);
		}
		
		enderecoRepository.save(novoEndereco);
		return new MostrarEnderecoDTO(novoEndereco);
	}
	
	@Transactional
	public MostrarEnderecoDTO alterarEnderecoPorIdToken(AlterarEnderecoDTO enderecoDTO, Long idEndereco) {
		Cliente cliente = usuarioAutenticadoService.getCliente();
		List<Endereco> enderecos = cliente.getEnderecos();
		Endereco endereco = verificarSeEnderecoExistePorIdEnderecoECliente(idEndereco, cliente);
		
		endereco.alterarEndereco(enderecoDTO);
		
		if (enderecoDTO.enderecoPadrao() == true && !enderecos.isEmpty()) {
			enderecos.forEach(e -> {
				if (e.getEnderecoPadrao() == true) {
					e.setEnderecoPadrao(false);
					enderecoRepository.save(e);
				}
			});
		}
		
		if (enderecoDTO.enderecoPadrao() == false && enderecos.isEmpty()) {
			endereco.setEnderecoPadrao(true);
		}
		
		enderecoRepository.save(endereco);
		return new MostrarEnderecoDTO(endereco);
	}
	
	
//	------------------------------
//	------------------------------
//	METODOS UTILITARIOS
//	------------------------------
//	------------------------------

	public Endereco verificarSeEnderecoExistePorIdEnderecoECliente(Long idEndereco, Cliente cliente) {
		return enderecoRepository.findByIdAndCliente(idEndereco, cliente).orElseThrow(() -> new ValidacaoException("Não existe endereço de id número " + idEndereco + " para o cliente de id número " + cliente.getId()));
	}
	
	public Endereco verificarID(Long clienteId, Long enderecoId) {
		Optional<Endereco> optionalEndereco = enderecoRepository.findByClienteIdAndEnderecoId(clienteId, enderecoId);
		if (optionalEndereco.isPresent()) {
			Endereco endereco = optionalEndereco.get();
			return endereco;
		} else {	
			throw new RuntimeException("ID do Endereco ou Cliente inexistente! ");
		}
	}

	
}
