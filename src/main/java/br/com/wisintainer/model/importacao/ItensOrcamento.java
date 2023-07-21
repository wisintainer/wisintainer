package br.com.wisintainer.model.importacao;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ItensOrcamento {

	public List<Item> item;

	public List<Item> getItem() {
		return item;
	}

	public void setItem(List<Item> item) {
		this.item = item;
	}

}
