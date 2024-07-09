package br.com.danielschiavo.vendas.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.vendas.model.Carrinho;
import br.com.danielschiavo.vendas.exception.CarrinhoNaoExisteException;
import br.com.danielschiavo.vendas.dto.response.MostrarCarrinhoClienteResponse;
import br.com.danielschiavo.vendas.dto.RemoverProdutoDoCarrinhoDTO;
import br.com.danielschiavo.vendas.dto.request.AdicionarItemCarrinhoRequest;
import br.com.danielschiavo.vendas.model.ItemCarrinho;
import br.com.danielschiavo.vendas.exception.ItemCarrinhoException;
import br.com.danielschiavo.vendas.repository.CarrinhoRepository;
import br.com.danielschiavo.cliente.repository.ClienteRepository;
import br.com.danielschiavo.shared.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.danielschiavo.shared.infra.security.SecurityService;
import br.com.danielschiavo.vendas.mapper.CarrinhoMapper;
import lombok.Setter;

@Service
@Setter
public class CarrinhoService {

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	@Autowired
	private CarrinhoMapper carrinhoMapper;
	
	@Transactional
	public List<RemoverProdutoDoCarrinhoDTO> deletarProdutoNoCarrinhoPorIdToken(List<Long> produtosId) {
		Cliente cliente = securityService.getCliente();
		Carrinho carrinho = pegarCarrinho(cliente);
		verificarSeTemItemsNoCarrinho(cliente, carrinho);
		
		List<RemoverProdutoDoCarrinhoDTO> removerProdutoDoCarrinho = new ArrayList<>();
		for (Long id : produtosId) {
			boolean removeu = false;
            for (ItemCarrinho itemCarrinho : carrinho.getItemsCarrinho()) {
                if (itemCarrinho.getProdutoId() == id) {
                    carrinho.removerItemCarrinho(itemCarrinho);
                    removeu = true;
                    break;
                }
            }
		    var removerProdutoDoCarrinhoDTOBuilder = RemoverProdutoDoCarrinhoDTO.builder().produtoId(id);
		    if (!removeu) {
		    	removerProdutoDoCarrinhoDTOBuilder.erro("Não foi possivel remover o produto porque ele não está no carrinho");
		    } else {
		    	removerProdutoDoCarrinhoDTOBuilder.mensagem("Produto removido do carrinho com sucesso!");
		    }
		    removerProdutoDoCarrinho.add(removerProdutoDoCarrinhoDTOBuilder.build());
		}
		carrinhoRepository.save(carrinho);
		return removerProdutoDoCarrinho;
	}
	
	public MostrarCarrinhoClienteResponse pegarCarrinhoClientePorIdToken() {
		Cliente cliente = securityService.getCliente();
		Carrinho carrinho = pegarCarrinho(cliente);
		verificarSeTemItemsNoCarrinho(cliente, carrinho);

		return carrinhoMapper.carrinhoParaMostrarCarrinhoClienteDTO(carrinho);
	}
	
	@Transactional
	public String adicionarProdutosNoCarrinhoPorIdToken(AdicionarItemCarrinhoRequest itemCarrinhoDTO) {
		if (itemCarrinhoDTO.quantidade() <= 0) {
			throw new ValidacaoException("A quantidade do produto deve ser maior ou igual a 1, o valor fornecido foi: "
					+ itemCarrinhoDTO.quantidade());
		}
		
		Cliente clienteDetached = securityService.getCliente();
		//O .save(cliente) é necessario porque cliente aqui está como detached, após o .save(cliente) ele será managed, portanto, poderá ser usado para persistencia
		Cliente clienteManaged = clienteRepository.save(clienteDetached);
		Carrinho carrinho = null;
		try {
			carrinho = pegarCarrinho(clienteManaged);
		} catch (CarrinhoNaoExisteException e) {
			carrinho = Carrinho.builder().cliente(clienteManaged).dataEHoraAtualizacao(LocalDateTime.now()).build();
		}
		
		String mensagemSucesso = "Produto adicionado no carrinho!";
		carrinho.setDataEHoraAtualizacao(LocalDateTime.now());

	    List<ItemCarrinho> itensCarrinho = carrinho.getItemsCarrinho();
	    boolean itemEncontrado = false;
	    for (ItemCarrinho item : itensCarrinho) {
	        if (item.getProdutoId() == itemCarrinhoDTO.produtoId()) {
	            item.setQuantidade(item.getQuantidade() + itemCarrinhoDTO.quantidade());
	            itemEncontrado = true;
	            break;
	        }
	    }
	    
	    if (!itemEncontrado) {
	        ItemCarrinho itemCarrinho = ItemCarrinho.builder()
									                .id(null)
									                .quantidade(itemCarrinhoDTO.quantidade())
									                .produtoId(itemCarrinhoDTO.produtoId())
									                .subTotal(BigDecimal.ONE)
									                .dataEHoraInsercao(LocalDateTime.now())
									                .carrinho(carrinho).build();
	        carrinho.adicionarItemCarrinho(itemCarrinho);
	    }
	    
	    carrinhoRepository.save(carrinho);
	    return mensagemSucesso;
	}

	@Transactional
	public void setarQuantidadeProdutoNoCarrinhoPorIdToken(AdicionarItemCarrinhoRequest itemCarrinhoDTO) {
		Cliente cliente = securityService.getCliente();
		Carrinho carrinho = pegarCarrinho(cliente);
		verificarSeTemItemsNoCarrinho(cliente, carrinho);

        for (ItemCarrinho itemCarrinho : carrinho.getItemsCarrinho()) {
            if (itemCarrinho.getProdutoId() == itemCarrinhoDTO.produtoId()) {
                if (itemCarrinhoDTO.quantidade() == 0) {
                    carrinho.removerItemCarrinho(itemCarrinho);
                    carrinhoRepository.save(carrinho);
                    return;
                }
                itemCarrinho.setQuantidade(itemCarrinhoDTO.quantidade());
                carrinhoRepository.save(carrinho);
                return;
            }
        }
		throw new ValidacaoException("Não existe produto de id número " + itemCarrinhoDTO.produtoId() + " no carrinho");
	}

	public Carrinho pegarCarrinho(Cliente cliente) {
		return carrinhoRepository.findByCliente(cliente).orElseThrow(() -> new CarrinhoNaoExisteException("Não existe carrinho para esse cliente"));
	}

	public void verificarSeTemItemsNoCarrinho(Cliente cliente, Carrinho carrinho) {
		boolean empty = carrinho.getItemsCarrinho().isEmpty();
		if (empty) {
			throw new ItemCarrinhoException("O cliente não tem items no carrinho no momento");
		}
	}

	public String deletarItemsCarrinhoAposPedidoGerado(List<Long> ids, Cliente cliente) {
		Carrinho carrinho = pegarCarrinho(cliente);
		verificarSeTemItemsNoCarrinho(cliente, carrinho);
		int linhasAfetadas = carrinhoRepository.deletarItemsCarrinhoPorListaDeIds(ids, carrinho.getClienteId());
		if (linhasAfetadas > 0) {
			return "Item removido com sucesso";
		} else {
			return "Não existe um produto no carrinho com esse ID";
		}
	}
}
