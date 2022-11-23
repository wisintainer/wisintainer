package br.com.wisintainer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.wisintainer.helper.TipoBanco;
import br.com.wisintainer.helper.TipoBanco.TiposBanco;

@Entity
@TipoBanco(banco = TiposBanco.MYSQL)
@Table(name = "orcamento", schema = "adriano1409_wisintainer")
public class Orcamento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Integer id;

	@Column(name = "dataCriacao")
	private Date dataCriacao;

	@Column(name = "veiculoFabricante")
	private String veiculoFabricante;

	@Column(name = "veiculoModelo")
	private String veiculoModelo;

	@Column(name = "veiculoAnoFabricao")
	private Integer veiculoAnoFabricao;

	@Column(name = "veiculoAnoModelo")
	private Integer veiculoAnoModelo;

	@Column(name = "veiculoPlaca")
	private String veiculoPlaca;

	@Column(name = "veiculoChassi")
	private String veiculoChassi;

	@Column(name = "status")
	private Integer status;

	@Column(name = "entrega_empresa")
	private String entrega_empresa;

	@Column(name = "entrega_responsavel")
	private String entrega_responsavel;

	@Column(name = "entrega_endereco")
	private String entrega_endereco;

	@Column(name = "entrega_numero")
	private String entrega_numero;

	@Column(name = "entrega_bairro")
	private String entrega_bairro;

	@Column(name = "entrega_cidade")
	private String entrega_cidade;

	@Column(name = "entrega_estado")
	private String entrega_estado;

	@Column(name = "entrega_cep")
	private String entrega_cep;

	@Column(name = "entrega_telefone")
	private String entrega_telefone;

	@Column(name = "entrega_email")
	private String entrega_email;

	@Column(name = "entrega_requisitante")
	private String entrega_requisitante;

	@Column(name = "entrega_cnpj")
	private String entrega_cnpj;

	@Transient
	private List<ItemOrcamento> itensOrcamento;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getVeiculoFabricante() {
		return veiculoFabricante;
	}

	public void setVeiculoFabricante(String veiculoFabricante) {
		this.veiculoFabricante = veiculoFabricante;
	}

	public String getVeiculoModelo() {
		return veiculoModelo;
	}

	public void setVeiculoModelo(String veiculoModelo) {
		this.veiculoModelo = veiculoModelo;
	}

	public Integer getVeiculoAnoFabricao() {
		return veiculoAnoFabricao;
	}

	public void setVeiculoAnoFabricao(Integer veiculoAnoFabricao) {
		this.veiculoAnoFabricao = veiculoAnoFabricao;
	}

	public Integer getVeiculoAnoModelo() {
		return veiculoAnoModelo;
	}

	public void setVeiculoAnoModelo(Integer veiculoAnoModelo) {
		this.veiculoAnoModelo = veiculoAnoModelo;
	}

	public String getVeiculoPlaca() {
		return veiculoPlaca;
	}

	public void setVeiculoPlaca(String veiculoPlaca) {
		this.veiculoPlaca = veiculoPlaca;
	}

	public String getVeiculoChassi() {
		return veiculoChassi;
	}

	public void setVeiculoChassi(String veiculoChassi) {
		this.veiculoChassi = veiculoChassi;
	}

	public List<ItemOrcamento> getItensOrcamento() {
		return itensOrcamento;
	}

	public void setItensOrcamento(List<ItemOrcamento> itensOrcamento) {
		this.itensOrcamento = itensOrcamento;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEntrega_empresa() {
		return entrega_empresa;
	}

	public void setEntrega_empresa(String entrega_empresa) {
		this.entrega_empresa = entrega_empresa;
	}

	public String getEntrega_responsavel() {
		return entrega_responsavel;
	}

	public void setEntrega_responsavel(String entrega_responsavel) {
		this.entrega_responsavel = entrega_responsavel;
	}

	public String getEntrega_endereco() {
		return entrega_endereco;
	}

	public void setEntrega_endereco(String entrega_endereco) {
		this.entrega_endereco = entrega_endereco;
	}

	public String getEntrega_numero() {
		return entrega_numero;
	}

	public void setEntrega_numero(String entrega_numero) {
		this.entrega_numero = entrega_numero;
	}

	public String getEntrega_bairro() {
		return entrega_bairro;
	}

	public void setEntrega_bairro(String entrega_bairro) {
		this.entrega_bairro = entrega_bairro;
	}

	public String getEntrega_cidade() {
		return entrega_cidade;
	}

	public void setEntrega_cidade(String entrega_cidade) {
		this.entrega_cidade = entrega_cidade;
	}

	public String getEntrega_estado() {
		return entrega_estado;
	}

	public void setEntrega_estado(String entrega_estado) {
		this.entrega_estado = entrega_estado;
	}

	public String getEntrega_cep() {
		return entrega_cep;
	}

	public void setEntrega_cep(String entrega_cep) {
		this.entrega_cep = entrega_cep;
	}

	public String getEntrega_telefone() {
		return entrega_telefone;
	}

	public void setEntrega_telefone(String entrega_telefone) {
		this.entrega_telefone = entrega_telefone;
	}

	public String getEntrega_email() {
		return entrega_email;
	}

	public void setEntrega_email(String entrega_email) {
		this.entrega_email = entrega_email;
	}

	public String getEntrega_requisitante() {
		return entrega_requisitante;
	}

	public void setEntrega_requisitante(String entrega_requisitante) {
		this.entrega_requisitante = entrega_requisitante;
	}

	public String getEntrega_cnpj() {
		return entrega_cnpj;
	}

	public void setEntrega_cnpj(String entrega_cnpj) {
		this.entrega_cnpj = entrega_cnpj;
	}

	// 0 - Aberto
	// 1 - Aprovado
	// 2 - Cancelado
	public String getStatusAsString() {
		String status = "Indefinido";
		if (getStatus() == 0) {
			status = "Aberto";
		}

		if (getStatus() == 1) {
			status = "Aprovado";
		}

		if (getStatus() == 2) {
			status = "Cancelado";
		}

		return status;
	}
}
