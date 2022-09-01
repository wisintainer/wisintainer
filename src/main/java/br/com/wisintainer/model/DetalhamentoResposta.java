package br.com.wisintainer.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import br.com.wisintainer.helper.TipoBanco;
import br.com.wisintainer.helper.TipoBanco.TiposBanco;

//@Entity
//@TipoBanco(banco = TiposBanco.MYSQL)
public class DetalhamentoResposta {

	@Column(name = "idRespostaOrcamento")
	private Integer idRespostaOrcamento;

	@Column(name = "nomeFornecedor")
	private String nomeFornecedor;

	@Column(name = "produtoServico")
	private String produtoServico;

	@Column(name = "valor")
	private Double valor;

	@Column(name = "temEmEstoque")
	private boolean temEmEstoque;

	public Integer getIdRespostaOrcamento() {
		return idRespostaOrcamento;
	}

	public void setIdRespostaOrcamento(Integer idRespostaOrcamento) {
		this.idRespostaOrcamento = idRespostaOrcamento;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(String produtoServico) {
		this.produtoServico = produtoServico;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public boolean isTemEmEstoque() {
		return temEmEstoque;
	}

	public void setTemEmEstoque(boolean temEmEstoque) {
		this.temEmEstoque = temEmEstoque;
	}

}
