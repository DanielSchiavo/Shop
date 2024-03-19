package br.com.danielschiavo.shop.models.produto.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielschiavo.shop.models.produto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.services.SubCategoriaService;

@Service
public class ValidadorSubCategoria implements ValidadorCadastrarNovoProduto {

	@Autowired
	private SubCategoriaService subCategoriaService;
	
	@Override
	public void validar(CadastrarProdutoDTO cadastrarProdutoDTO) {
		Long idSubCategoria = cadastrarProdutoDTO.idSubCategoria();
		subCategoriaService.verificarSeExisteSubCategoriaPorId(idSubCategoria);
	}

}
