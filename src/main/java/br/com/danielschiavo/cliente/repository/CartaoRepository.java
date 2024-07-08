package br.com.danielschiavo.cliente.repository;

import java.util.List;
import java.util.Optional;

import br.com.danielschiavo.cliente.model.entity.Cliente;
import br.com.danielschiavo.cliente.model.entity.Cartao;
import br.com.danielschiavo.cliente.model.enums.TipoCartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;


public interface CartaoRepository extends JpaRepository<Cartao, Long>{

	Optional<Cartao> findByNumeroCartaoAndTipoCartaoAndClienteId(String numeroCartao, TipoCartao tipoCartao, Long clienteId);

	Optional<Cartao> findByIdAndClienteId(Long cartaoId, Long clienteId);

	Optional<List<Cartao>> findAllByClienteId(Long clienteId);

	@Modifying
	void deleteByIdAndClienteId(Long cartaoId, Long clienteId);
}
