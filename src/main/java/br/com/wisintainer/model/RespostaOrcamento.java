package br.com.wisintainer.model;

import java.io.Serializable;

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
@Table(name = "respostaorcamento", schema = "adriano1409_wisintainer")
public class RespostaOrcamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "id_orcamento")
	private Integer id_orcamento;

	@Column(name = "id_item")
	private Integer id_item;

	@Column(name = "id_resposta")
	private Integer id_resposta;

	@Column(name = "valor")
	private Double valor;

	@Column(name = "nome_item")
	private String nome_item;

	@Column(name = "quantidade")
	private Integer quantidade;

	@Column(name = "tememestoque")
	private boolean tememestoque;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId_orcamento() {
		return id_orcamento;
	}

	public void setId_orcamento(Integer id_orcamento) {
		this.id_orcamento = id_orcamento;
	}

	public Integer getId_item() {
		return id_item;
	}

	public void setId_item(Integer id_item) {
		this.id_item = id_item;
	}

	public Integer getId_resposta() {
		return id_resposta;
	}

	public void setId_resposta(Integer id_resposta) {
		this.id_resposta = id_resposta;
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

	public String getNome_item() {
		return nome_item;
	}

	public void setNome_item(String nome_item) {
		this.nome_item = nome_item;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

}
