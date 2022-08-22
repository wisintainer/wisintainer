package br.com.wisintainer.model;

import java.util.Date;
import java.util.List;

public class Orcamento {
	private Integer id;
	private Date dataCriacao;
	private String veiculoFabricante;
	private String veiculoModelo;
	private Integer veiculoAnoFabricao;
	private Integer veiculoAnoModelo;
	private String veiculoPlaca;
	private String veiculoChassi;

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
