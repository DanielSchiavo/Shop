package br.com.danielschiavo.shop.models.endereco;

public record EnderecoDTO(
		Long id,
		String cep,
		String rua,
		String numero,
		String complemento,
		String bairro,
		String estado
		) {

	public static EnderecoDTO converterParaEnderecoDTO(Endereco endereco) {
		EnderecoDTO enderecoDTO = new EnderecoDTO(
												endereco.getId(),
												endereco.getCep(),
												endereco.getRua(),
												endereco.getNumero(),
												endereco.getComplemento(),
												endereco.getBairro(),
												endereco.getEstado()
												);
		
		return enderecoDTO;
	}
}
