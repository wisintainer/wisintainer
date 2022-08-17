package br.com.wisintainer.helper;

public enum ConstantesEnum {

	PARAMETRO_URL_EDITAR_CLIENTELA("habilitacao"),
	PARAMETRO_URL_EDITAR_EMPRESA("cnpj"),
	PARAMETRO_URL_EDITAR_RESPONSAVEL_FINANCEIRO("cpf"),
	PARAMETRO_EDITAR_EMPRESA("cnpj"),
	PARAMETRO_EDITAR_RESPONSAVEL_FINANCEIRO("cpf"),
	DIAS_BUSCA_RECEITA_WS("1"),
	PARAMETRO_URL_MENSAGEM("msg"),
	PARAMETRO_SESSAO_MENSAGEM("scaweb_sessao_mensagem");

	private String tipo;

	ConstantesEnum(final String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return tipo;
	}
}
