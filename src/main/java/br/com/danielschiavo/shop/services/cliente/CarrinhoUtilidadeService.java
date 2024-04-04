package br.com.danielschiavo.shop.services.cliente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.infra.exceptions.ValidacaoException;
import br.com.danielschiavo.shop.models.cliente.Cliente;
import br.com.danielschiavo.shop.models.cliente.carrinho.Carrinho;
import br.com.danielschiavo.shop.repositories.cliente.CarrinhoRepository;

@Service
public class CarrinhoUtilidadeService {

	@Autowired
	private CarrinhoRepository carrinhoRepository;
	
	public Carrinho verificarEPegarCarrinhoCliente(Cliente cliente) {
		return carrinhoRepository.findByCliente(cliente).orElseThrow(() -> new ValidacaoException("Não existe um carrinho para o cliente de ID número " + cliente.getId()));
	}

	public boolean deletarItemsCarrinhoAposPedidoGerado(List<Long> ids, Cliente cliente) {
		Carrinho carrinho = verificarEPegarCarrinhoCliente(cliente);
		int linhasAfetadas = carrinhoRepository.deletarItemsCarrinhoPorListaDeIds(ids, carrinho.getId());
		return linhasAfetadas > 0;
	}

}
