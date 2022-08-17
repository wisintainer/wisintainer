package br.com.wisintainer.helper;

import java.util.ArrayList;
import java.util.Arrays;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public enum SeveridadeEnum {
	INFO(FacesMessage.SEVERITY_INFO, "Informação"),
	WARN(FacesMessage.SEVERITY_WARN, "Alerta"),
	ERROR(FacesMessage.SEVERITY_ERROR, "Erro"),
	FATAL(FacesMessage.SEVERITY_FATAL, "Erro Grave");
		
	private Severity serveridade;
	private String sumario;

	private SeveridadeEnum(final Severity serveridade, final String sumario) {
		this.serveridade = serveridade;
		this.sumario = sumario;
	}	

	public Severity getServeridade() {
		return serveridade;
	}

	public void setServeridade(Severity serveridade) {
		this.serveridade = serveridade;
	}

	public String getSumario() {
		return sumario;
	}

	public void setSumario(String sumario) {
		this.sumario = sumario;
	}
	
	public static ArrayList<SeveridadeEnum> getLst() {		
		return new ArrayList<SeveridadeEnum>(Arrays.asList(SeveridadeEnum.values()));		
	}
}
