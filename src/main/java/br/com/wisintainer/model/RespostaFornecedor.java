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
@Table(name = "respostafornecedor", schema = "adriano1409_wisintainer")
public class RespostaFornecedor implements Serializable {

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

	@Column(name = "id_fornecedor")
	private Integer id_fornecedor;

	@Column(name = "fornecedorrespondeu")
	private boolean fornecedorrespondeu;

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

	public Integer getId_fornecedor() {
		return id_fornecedor;
	}

	public void setId_fornecedor(Integer id_fornecedor) {
		this.id_fornecedor = id_fornecedor;
	}

	public boolean isFornecedorrespondeu() {
		return fornecedorrespondeu;
	}

	public void setFornecedorrespondeu(boolean fornecedorrespondeu) {
		this.fornecedorrespondeu = fornecedorrespondeu;
	}

}
