package br.com.wisintainer.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.wisintainer.helper.TipoBanco;
import br.com.wisintainer.helper.TipoBanco.TiposBanco;

@Entity
@TipoBanco(banco = TiposBanco.MYSQL)
@Table(name = "itemorcamento", schema = "adriano1409_wisintainer")
public class ItemOrcamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "produtoServico")
	private String produtoServico;

	@Column(name = "quantidade")
	private Integer quantidade;

	@Column(name = "orcamento_id")
	private Integer orcamento_id;

	@Column(name = "cor")
	private String cor;

	@Transient
	private boolean tememestoque;

	@Transient
	private Date previsaoDeEntrega;

	@Transient
	private Double valor;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(String produtoServico) {
		this.produtoServico = produtoServico;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getOrcamento_id() {
		return orcamento_id;
	}

	public void setOrcamento_id(Integer orcamento_id) {
		this.orcamento_id = orcamento_id;
	}

	public boolean isTememestoque() {
		return tememestoque;
	}

	public void setTememestoque(boolean tememestoque) {
		this.tememestoque = tememestoque;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}

	public Date getPrevisaoDeEntrega() {
		return previsaoDeEntrega;
	}

	public void setPrevisaoDeEntrega(Date previsaoDeEntrega) {
		this.previsaoDeEntrega = previsaoDeEntrega;
	}

}
