package br.com.wisintainer.model;

import javax.faces.application.FacesMessage;

public class Messager {
	
	public static void addMessage(FacesMessage.Severity severidade, String sumario, String detalhes, String identificador) {
		Application.getContext().addMessage(
			identificador,
			new FacesMessage(
				severidade,
				sumario,
				detalhes
			)
		);
	}
	
	public static void addMessage(FacesMessage.Severity severidade, String sumario, String detalhes) {
		Application.getContext().addMessage(
			null,
			new FacesMessage(
				severidade,
				sumario,
				detalhes
			)
		);
	}
	
	public static void addInfo(String detalhes) {
		addMessage(FacesMessage.SEVERITY_INFO, "Informação", detalhes);
	}
	
	public static void addInfo(String detalhes, String identificador) {
		addMessage(FacesMessage.SEVERITY_INFO, "Informação", detalhes, identificador);
	}
	
	public static void addWarning(String detalhes) {
		addMessage(FacesMessage.SEVERITY_WARN, "Alerta", detalhes);
	}
	
	public static void addWarning(String detalhes, String identificador) {
		addMessage(FacesMessage.SEVERITY_WARN, "Alerta", detalhes, identificador);
	}
	
	public static void addError(String detalhes) {
		addMessage(FacesMessage.SEVERITY_ERROR, "Erro", detalhes);
	}
	
	public static void addError(String detalhes, String identificador) {
		addMessage(FacesMessage.SEVERITY_ERROR, "Erro", detalhes, identificador);
	}
	
	public static void addFatal(String detalhes) {
		addMessage(FacesMessage.SEVERITY_FATAL, "Erro Grave", detalhes);
	}
	
	public static void addFatal(String detalhes, String identificador) {
		addMessage(FacesMessage.SEVERITY_FATAL, "Erro Grave", detalhes, identificador);
	}
}
