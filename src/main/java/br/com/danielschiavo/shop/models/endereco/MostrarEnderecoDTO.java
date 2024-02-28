package br.com.danielschiavo.shop.models.endereco;

public record MostrarEnderecoDTO(
		Long id,
		String cep,
		String rua,
		String numero,
		String complemento,
		String bairro,
		String cidade,
		String estado,
		Boolean enderecoPadrao
		) {

	public MostrarEnderecoDTO(Endereco endereco) {
		this(
				endereco.getId(),
				endereco.getCep(),
				endereco.getRua(),
				endereco.getNumero(),
				endereco.getComplemento(),
				endereco.getBairro(),
				endereco.getCidade(),
				endereco.getEstado(),
				endereco.getEnderecoPadrao()
				);
	}

	public static MostrarEnderecoDTO converterParaEnderecoDTO(Endereco endereco) {
		MostrarEnderecoDTO enderecoDTO = new MostrarEnderecoDTO(
												endereco.getId(),
												endereco.getCep(),
												endereco.getRua(),
												endereco.getNumero(),
												endereco.getComplemento(),
												endereco.getBairro(),
												endereco.getCidade(),
												endereco.getEstado(),
												endereco.getEnderecoPadrao()
												);
		
		return enderecoDTO;
	}
}
