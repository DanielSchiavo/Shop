package br.com.danielschiavo.shop.models.produto.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.produto.dto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.services.produto.SubCategoriaUtilidadeService;

@Service
public class ValidadorSubCategoria implements ValidadorCadastrarNovoProduto {

	@Autowired
	private SubCategoriaUtilidadeService subCategoriaUtilidadeService;
	
	@Override
	public void validar(CadastrarProdutoDTO cadastrarProdutoDTO) {
		Long idSubCategoria = cadastrarProdutoDTO.idSubCategoria();
		subCategoriaUtilidadeService.verificarSeExisteSubCategoriaPorId(idSubCategoria);
	}

}
