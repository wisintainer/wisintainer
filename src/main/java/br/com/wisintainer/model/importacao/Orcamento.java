package br.com.wisintainer.model.importacao;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "orcamento")
@XmlAccessorType(XmlAccessType.FIELD)
public class Orcamento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String status;

	public ItensOrcamento itens_orcamento;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ItensOrcamento getItens_orcamento() {
		return itens_orcamento;
	}

	public void setItens_orcamento(ItensOrcamento itens_orcamento) {
		this.itens_orcamento = itens_orcamento;
	}

}
