package br.com.wisintainer.model;

import java.io.Serializable;

import br.com.wisintainer.helper.SeveridadeEnum;

public class MensagemRedirecionamento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mensagem;
	private SeveridadeEnum severidade;

	public MensagemRedirecionamento() {

	}

	public MensagemRedirecionamento(String mensagem, SeveridadeEnum severidade) {
		super();
		this.mensagem = mensagem;
		this.severidade = severidade;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public SeveridadeEnum getSeveridade() {
		return severidade;
	}

	public void setSeveridade(SeveridadeEnum severidade) {
		this.severidade = severidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MensagemRedirecionamento [mensagem=" + mensagem + ", severidade=" + severidade + "]";
	}
}
