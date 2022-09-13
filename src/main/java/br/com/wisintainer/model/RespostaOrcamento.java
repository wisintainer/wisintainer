package br.com.wisintainer.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import com.google.protobuf.DoubleValueOrBuilder;

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

	@PostConstruct
	public void inicializar() {
		this.nomeFornecedor = new String();
	}

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

	@Column(name = "id_fornecedor")
	private Integer id_fornecedor;

	@Column(name = "nome_fornecedor")
	private String nomeFornecedor;

	@Column(name = "data_resposta")
	private Date data_resposta;

	public String temEmEstoqueAsString() {
		if (tememestoque) {
			return "Sim";
		} else {
			return "Não";
		}
	}

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

	public Integer getId_fornecedor() {
		return id_fornecedor;
	}

	public void setId_fornecedor(Integer id_fornecedor) {
		this.id_fornecedor = id_fornecedor;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public Date getData_resposta() {
		return data_resposta;
	}

	public void setData_resposta(Date data_resposta) {
		this.data_resposta = data_resposta;
	}

	public String getValorAsString() {
		DecimalFormat decimal = new DecimalFormat("###,###,###,##0.00");
		return decimal.format(getValor());
	}

	public String valorTotalPorItem() {
		DecimalFormat decimal = new DecimalFormat("###,###,###,##0.00");
		return decimal.format(quantidade * valor);
	}

}
