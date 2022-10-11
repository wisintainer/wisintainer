package br.com.wisintainer.model;

import java.io.Serializable;
import java.text.DecimalFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.wisintainer.helper.TipoBanco;
import br.com.wisintainer.helper.TipoBanco.TiposBanco;

@Entity
@TipoBanco(banco = TiposBanco.MYSQL)
@Table(name = "aprovacao", schema = "adriano1409_wisintainer")
public class Aprovacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "resposta_orcamento_id")
	private Integer resposta_orcamento_id;

	@Column(name = "orcamento_id")
	private Integer orcamento_id;

	@Column(name = "item_id")
	private Integer item_id;

	@Column(name = "item_valor")
	private Double item_valor;

	@Column(name = "item_tememestoque")
	private boolean item_tememestoque;

	@Column(name = "item_nome")
	private String item_nome;

	@Column(name = "item_quantidade")
	private Integer item_quantidade;

	@Column(name = "fornecedor_id")
	private Integer fornecedor_id;

	@Column(name = "fornecedor_nome")
	private String fornecedor_nome;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getResposta_orcamento_id() {
		return resposta_orcamento_id;
	}

	public void setResposta_orcamento_id(Integer resposta_orcamento_id) {
		this.resposta_orcamento_id = resposta_orcamento_id;
	}

	public Integer getOrcamento_id() {
		return orcamento_id;
	}

	public void setOrcamento_id(Integer orcamento_id) {
		this.orcamento_id = orcamento_id;
	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public Double getItem_valor() {
		return item_valor;
	}

	public void setItem_valor(Double item_valor) {
		this.item_valor = item_valor;
	}

	public boolean isItem_tememestoque() {
		return item_tememestoque;
	}

	public void setItem_tememestoque(boolean item_tememestoque) {
		this.item_tememestoque = item_tememestoque;
	}

	public String getItem_nome() {
		return item_nome;
	}

	public void setItem_nome(String item_nome) {
		this.item_nome = item_nome;
	}

	public Integer getItem_quantidade() {
		return item_quantidade;
	}

	public void setItem_quantidade(Integer item_quantidade) {
		this.item_quantidade = item_quantidade;
	}

	public Integer getFornecedor_id() {
		return fornecedor_id;
	}

	public void setFornecedor_id(Integer fornecedor_id) {
		this.fornecedor_id = fornecedor_id;
	}

	public String getFornecedor_nome() {
		return fornecedor_nome;
	}

	public void setFornecedor_nome(String fornecedor_nome) {
		this.fornecedor_nome = fornecedor_nome;
	}

	public String temEmEstoqueAsString() {
		if (isItem_tememestoque()) {
			return "Sim";
		} else {
			return "Não";
		}
	}
	
	public String valorAsString() {
		DecimalFormat decimal = new DecimalFormat("###,###,###,##0.00");
		return decimal.format(getItem_valor());
	}

}
