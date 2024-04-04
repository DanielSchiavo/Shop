package br.com.danielschiavo.shop.mapper.produto;

import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import br.com.danielschiavo.shop.models.filestorage.ArquivoInfoDTO;
import br.com.danielschiavo.shop.models.produto.Produto;
import br.com.danielschiavo.shop.models.produto.arquivosproduto.ArquivoProduto;
import br.com.danielschiavo.shop.models.produto.categoria.Categoria;
import br.com.danielschiavo.shop.models.produto.categoria.MostrarCategoriaComSubCategoriaDTO;
import br.com.danielschiavo.shop.models.produto.dto.AlterarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.dto.CadastrarProdutoDTO;
import br.com.danielschiavo.shop.models.produto.dto.DetalharProdutoDTO;
import br.com.danielschiavo.shop.models.produto.dto.MostrarProdutosDTO;
import br.com.danielschiavo.shop.models.produto.subcategoria.MostrarSubCategoriaDTO;
import br.com.danielschiavo.shop.models.produto.subcategoria.SubCategoria;
import br.com.danielschiavo.shop.models.produto.tipoentregaproduto.TipoEntregaProduto;
import br.com.danielschiavo.shop.services.filestorage.FileStorageProdutoService;
import br.com.danielschiavo.shop.services.produto.CategoriaUtilidadeService;
import br.com.danielschiavo.shop.services.produto.ProdutoUtilidadeService;

@Mapper(componentModel = "spring")
public abstract class ProdutoMapper {
	
	@Mapping(target = "arquivos", ignore = true)
	@Mapping(target = "categoria", ignore = true)
	public abstract DetalharProdutoDTO produtoParaDetalharProdutoDTO(Produto produto, @Context FileStorageProdutoService fileStorageProdutoService, @Context ProdutoUtilidadeService produtoUtilidadeService, @Context CategoriaUtilidadeService categoriaUtilidadeService);

    @AfterMapping
    public void todosArquivosProdutoEMostrarCategoriaComSubCategoriaDTO(@MappingTarget DetalharProdutoDTO.DetalharProdutoDTOBuilder dto, Produto produto, @Context FileStorageProdutoService fileStorageProdutoService, @Context ProdutoUtilidadeService produtoUtilidadeService, @Context CategoriaUtilidadeService categoriaUtilidadeService) {
    	List<String> listaDeNomes = produtoUtilidadeService.pegarNomeTodosArquivos(produto);
		List<ArquivoInfoDTO> listaArquivoInfoDTO = fileStorageProdutoService.mostrarArquivoProdutoPorListaDeNomes(listaDeNomes);
    	
		System.out.println(" TESTE ");
		
		var subCategoria = new MostrarSubCategoriaDTO(produto.getSubCategoria().getId(), produto.getSubCategoria().getNome());
    	Categoria categoria = categoriaUtilidadeService.verificarSeExisteCategoriaPorId(produto.getSubCategoria().getCategoria().getId());
    	MostrarCategoriaComSubCategoriaDTO mostrarCategoriaComSubCategoriaDTO = new MostrarCategoriaComSubCategoriaDTO(categoria.getId(), categoria.getNome(), subCategoria);
    	dto.arquivos(listaArquivoInfoDTO).categoria(mostrarCategoriaComSubCategoriaDTO);
    }
    
	@Mapping(target = "categoria", ignore = true)
	@Mapping(target = "primeiraImagem", ignore = true)
	public abstract MostrarProdutosDTO produtoParaMostrarProdutosDTO(Produto produto, @Context FileStorageProdutoService fileStorageService, @Context ProdutoUtilidadeService produtoUtilidadeService, @Context CategoriaUtilidadeService categoriaUtilidadeService);

    @AfterMapping
    public void primeiraImagemEMostrarCategoriaComSubCategoriaDTO(@MappingTarget MostrarProdutosDTO.MostrarProdutosDTOBuilder dto, Produto produto, @Context FileStorageProdutoService fileStorageProdutoService, @Context ProdutoUtilidadeService produtoUtilidadeService, @Context CategoriaUtilidadeService categoriaUtilidadeService) {
    	String nomePrimeiraImagem = produtoUtilidadeService.pegarNomePrimeiraImagem(produto);
    	ArquivoInfoDTO arquivoInfoDTO = fileStorageProdutoService.pegarArquivoProdutoPorNome(nomePrimeiraImagem);
    	
    	
    	var subCategoria = new MostrarSubCategoriaDTO(produto.getSubCategoria().getId(), produto.getSubCategoria().getNome());
    	Categoria categoria = categoriaUtilidadeService.verificarSeExisteCategoriaPorId(produto.getSubCategoria().getCategoria().getId());
    	MostrarCategoriaComSubCategoriaDTO mostrarCategoriaComSubCategoriaDTO = new MostrarCategoriaComSubCategoriaDTO(categoria.getId(), categoria.getNome(), subCategoria);
    	dto.primeiraImagem(arquivoInfoDTO.bytesArquivo()).categoria(mostrarCategoriaComSubCategoriaDTO);
    }
    
    public Page<MostrarProdutosDTO> pageProdutosParaPageMostrarProdutosDTO(Page<Produto> pageProdutos, @Context FileStorageProdutoService fileStorageService, @Context ProdutoUtilidadeService produtoService, @Context ProdutoMapper produtoMapper, @Context CategoriaUtilidadeService categoriaUtilidadeService){
    	return pageProdutos.map(produto -> produtoMapper.produtoParaMostrarProdutosDTO(produto, fileStorageService, produtoService, categoriaUtilidadeService));
    }
    
    public void alterarProdutoDtoParaProduto(AlterarProdutoDTO alterarProdutoDTO, Produto produto, SubCategoria subCategoria) {
		if (alterarProdutoDTO.nome() != null) {
			produto.setNome(alterarProdutoDTO.nome());
		}
		if (alterarProdutoDTO.descricao() != null) {
			produto.setDescricao(alterarProdutoDTO.descricao());
		}
		if (alterarProdutoDTO.preco() != null) {
			produto.setPreco(alterarProdutoDTO.preco());
		}
		if (alterarProdutoDTO.quantidade() != null) {
			produto.setQuantidade(alterarProdutoDTO.quantidade());
		}
		if (alterarProdutoDTO.ativo() != null) {
			produto.setAtivo(alterarProdutoDTO.ativo());
		}
		if (alterarProdutoDTO.tipoEntrega() != null) {
			alterarProdutoDTO.tipoEntrega().forEach(tipo -> {
				produto.adicionarTipoEntrega(new TipoEntregaProduto(null, tipo, produto));
			});
		}
		if (alterarProdutoDTO.arquivos() != null) {
			alterarProdutoDTO.arquivos().forEach(arquivoProdutoDTO -> {
				produto.adicionarArquivoProduto(new ArquivoProduto(null, arquivoProdutoDTO.nome(), arquivoProdutoDTO.posicao().byteValue(), produto));
			});
		}
		if (alterarProdutoDTO.idSubCategoria() != null) {
			produto.setSubCategoria(subCategoria);
		}
    }
    
	public Produto cadastrarProdutoDtoParaProduto(CadastrarProdutoDTO cadastrarProdutoDTO, SubCategoria subCategoria) {
		Produto produto = new Produto();
		
		produto.setNome(cadastrarProdutoDTO.nome());
		produto.setDescricao(cadastrarProdutoDTO.descricao());
		produto.setPreco(cadastrarProdutoDTO.preco());
		produto.setQuantidade(cadastrarProdutoDTO.quantidade());
		produto.setAtivo(cadastrarProdutoDTO.ativo());
		produto.setSubCategoria(subCategoria);
		cadastrarProdutoDTO.tipoEntrega().forEach(te -> {
			produto.adicionarTipoEntrega(new TipoEntregaProduto(null, te, produto));
		});;
		cadastrarProdutoDTO.arquivos().forEach(a -> {
			produto.adicionarArquivoProduto(new ArquivoProduto(null, a.nome(), a.posicao().byteValue(), produto));
		});
		return produto;
	}
}
