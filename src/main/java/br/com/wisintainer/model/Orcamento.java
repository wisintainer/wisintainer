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

	
	
	
	

}
