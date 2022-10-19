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
@Table(name = "ordemdecompra_aprovacoes", schema = "adriano1409_wisintainer")
public class OrdemDeCompraAprovacoes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "ordemdecompra_id")
	private Integer ordemdecompra_id;

	@Column(name = "aprovacao_id")
	private Integer aprovacao_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrdemdecompra_id() {
		return ordemdecompra_id;
	}

	public void setOrdemdecompra_id(Integer ordemdecompra_id) {
		this.ordemdecompra_id = ordemdecompra_id;
	}

	public Integer getAprovacao_id() {
		return aprovacao_id;
	}

	public void setAprovacao_id(Integer aprovacao_id) {
		this.aprovacao_id = aprovacao_id;
	}

}
