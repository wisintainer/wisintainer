package br.com.wisintainer.model;

import java.util.Map;

import javax.el.ELException;
import javax.enterprise.inject.spi.CDI;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Application {

	private static String requestBaseURL; 

	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}
	
	public static boolean getPermissaoBuscarFornecedor() {
		return true;
	}
	
	public static boolean getPermissaoCriarorcamento() {
		return true;
	}

	public static ExternalContext getExternalContext() {
		FacesContext context = getContext();

		if (context != null) {
			return getContext().getExternalContext();
		} else {
			return null;
		}
	}

	public static HttpServletRequest getRequest() {
		ExternalContext externalContext = getExternalContext();

		if (externalContext != null) {
			return  (HttpServletRequest) externalContext.getRequest();
		} else {
			return null;
		}
	}

	public static HttpServletResponse getResponse() {
		ExternalContext externalContext = getExternalContext();

		if (externalContext != null) {
			return  (HttpServletResponse) externalContext.getResponse();
		} else {
			return null;
		}
	}

	public static String getRequestBaseURL() {
		final HttpServletRequest request = getRequest();

		if (request != null) {
			final String url = request.getRequestURL().toString();

			requestBaseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath() + "/";
		}

		return requestBaseURL;
	}

	public static ServletContext getServletContext() {
		ExternalContext externalContext = getExternalContext();

		if (externalContext != null) {
			return ((ServletContext) externalContext.getContext());
		} else {
			return null;
		}
	}
	
	public static String getContextName()
	{
		final HttpServletRequest request = getRequest();
		return request.getContextPath();
	}
	
	public static void adicionarParametroSessao(String chave, Object objeto)
	{
		Map<String, Object> sessionMap = getExternalContext().getSessionMap();
		sessionMap.put(chave, objeto);
	}
	
	public static void removerParametroSessao(String chave)
	{
		Map<String, Object> sessionMap = getExternalContext().getSessionMap();
		sessionMap.remove(chave);
	}
	
	public static <R> R recuperaValorSessao(String chave, Class<R> classeDeRetorno)
	{
		Map<String, Object> sessionMap = getExternalContext().getSessionMap();
		if(sessionMap.get(chave) != null)
		{
			return classeDeRetorno.cast(sessionMap.get(chave));
		}
		return null;
	}
	
}
