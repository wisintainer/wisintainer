package br.com.wisintainer.model.importacao;

import javax.xml.bind.annotation.XmlType;

@XmlType
public class Item {

	private String nome;

	private String quantidade;

	public Item() {
		super();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

}
