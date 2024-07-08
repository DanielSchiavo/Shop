package br.com.danielschiavo.cliente.mapper;

import java.util.ArrayList;
import java.util.List;

import br.com.danielschiavo.cliente.dto.request.cartao.CadastrarCartaoRequest;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.dto.response.cartao.MostrarCartaoResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public abstract class CartaoMapper {

	public Cartao toEntity(CadastrarCartaoRequest cartaoDTO, Long clienteId) {
		Cartao cartao = new Cartao();
		cartao.setNumeroCartao(cartaoDTO.numeroCartao());
		cartao.setNomeNoCartao(cartaoDTO.nomeNoCartao());
		cartao.setValidadeCartao(cartaoDTO.validadeCartao());
		cartao.setCartaoPadrao(cartaoDTO.cartaoPadrao());
		cartao.setTipoCartao(cartaoDTO.tipoCartao());
		cartao.setClienteId(clienteId);
		return cartao;
	}
	
	public abstract MostrarCartaoResponse toDto(Cartao cartao);
	
	public List<MostrarCartaoResponse> listaCartaoParaListaMostrarCartaoDto(List<Cartao> cartoes) {
		List<MostrarCartaoResponse> listaMostrarCartaoDTO = new ArrayList<>();
		cartoes.forEach(cartao -> {
			MostrarCartaoResponse mostrarCartaoDTO = toDto(cartao);
			listaMostrarCartaoDTO.add(mostrarCartaoDTO);
		});
		return listaMostrarCartaoDTO;
	}
}
