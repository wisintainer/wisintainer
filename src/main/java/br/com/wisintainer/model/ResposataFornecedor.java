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
public class ResposataFornecedor implements Serializable {

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

	@Column(name = "valor")
	private Double valor;

	@Column(name = "tememestoque")
	private boolean tememestoque;

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

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public boolean isTememestoque() {
		return tememestoque;
	}

	public void setTememestoque(boolean tememestoque) {
		this.tememestoque = tememestoque;
	}

	public boolean isFornecedorrespondeu() {
		return fornecedorrespondeu;
	}

	public void setFornecedorrespondeu(boolean fornecedorrespondeu) {
		this.fornecedorrespondeu = fornecedorrespondeu;
	}

}
