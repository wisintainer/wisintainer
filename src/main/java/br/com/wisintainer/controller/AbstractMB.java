package br.com.wisintainer.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.PrimeFaces;


import br.com.wisintainer.helper.Logger;

import br.com.wisintainer.helper.ConstantesEnum;
import br.com.wisintainer.helper.DateTimeUtil;
import br.com.wisintainer.helper.StringUtil;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Messager;
import br.com.wisintainer.model.MensagemRedirecionamento;


abstract public class AbstractMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractMB() {

	}

	@PostConstruct
	public void validacao() {
		validatePermission();
		trataMensagens();
	}

	public Boolean isUrl(String url) {
		return getFinalUrl().equals(url);
	}

	/**
	 * Considerando uma organização no qual uma página está, por exemplo, em:
	 * WebContent/public/page/pasta/pagina.xhtml o caminho passado para este método
	 * deve ser: /public/page/pasta/pagina.jsf.
	 *
	 * Se for necessário utilizar um <p:commandButton a sintaxe do mesmo deve ser
	 * parecida com:
	 *
	 * <code>
	 *      <p:commandButton
	 *			id="voltarCommandButton"
	 * 			value="Voltar"
	 *			icon="fa fas fa-arrow-left"
	 *			action="#{nomeDoBean.redirecionar('/public/page/pasta/pagina.jsf')}"
	 *			ajax="false"
	 * 		/>
	 *  </code>
	 *
	 * @param caminho
	 * @return
	 */
	public String redirect(String caminho) {
		if (caminho.contains("?")) {
			return caminho + "&faces-redirect=true";
		} else {
			return caminho + "?faces-redirect=true";
		}
	}

	/**
	 * Considerando uma organização no qual uma página está, por exemplo, em:
	 * 'WebContent/public/page/pasta/pagina.xhtml' no projeto 'ScaWeb' o caminho
	 * passado para este método deve ser: /public/page/pasta/pagina.jsf.
	 *
	 * Este redirecionamento é diferente do método 'redirecionar' porque ele força o
	 * redirecionamento, mesmo em métodos que não retornam string.
	 */
	public void forceRedirect(String caminho) {
		try {
			Application.getExternalContext().redirect(Application.getContextName() + caminho);
		} catch (Exception excecao) {
			Logger.getLogger().error(excecao.getMessage(), excecao);
		}
	}

	public void addCallBack(final String key, final Object value) {
		getContext().ajax().addCallbackParam(key, value);
	}

	protected PrimeFaces getContext() {
		return PrimeFaces.current();
	}

	protected Map<String, String> getParameters() {
		return Application.getExternalContext().getRequestParameterMap();
	}

	protected String getFinalUrl() {
		HttpServletRequest request = ((HttpServletRequest) Application.getExternalContext().getRequest());

		return request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/"));
	}

	protected void executeOnContext(String comando) {
		getContext().executeScript(comando);
	}

	/**
	 * Deve retornar verdadeiro ou falso informando se o usuário possui ou não
	 * permissão de acesso ao Bean.
	 *
	 * @return
	 */
	abstract public Boolean getPermission();

	public void validatePermission() {
		if (BooleanUtils.isNotTrue(getPermission())) {
			forceRedirect("/public/page/error/permissao.jsf");

			Logger.getLogger().info("O usuário não possui permissão de acesso ao bean " + this.toString() + ".");
		}
	}

	public boolean filterByDate(Object value, Object filter, Locale locale) {

		if (filter == null || filter.toString().trim().isEmpty()) {
			return true;
		}

		if (value == null) {
			return false;
		}

		if (value.toString().length() < 10) {
			return false;
		}

		try {
			Date dtFilter = DateTimeUtil.getData(filter.toString());
			return DateUtils.truncatedEquals(dtFilter, (Date) value, Calendar.DATE);
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}

		return false;
	}

	public void redirecionarParaErro(String msgErro) {
		try {

			String txt = "";
			if (StringUtils.isNoneBlank(msgErro)) {
				txt = StringUtil.encodeURLValue(msgErro);
			}

			StringBuilder sb = new StringBuilder();
			sb.append("/public/page/error/erro.jsf?");
			sb.append(ConstantesEnum.PARAMETRO_URL_MENSAGEM + "=" + txt);
			forceRedirect(sb.toString());

		} catch (Exception e) {
			Logger.getLogger().error("Erro", e);
			Messager.addError("Ocorreu um erro no tratamento de uma falha");
		}
	}

	public void redirecionarParaRecarga(String url, ArrayList<MensagemRedirecionamento> lstMensagens) {
		try {

			if (CollectionUtils.isNotEmpty(lstMensagens)) {
				Application.adicionarParametroSessao(ConstantesEnum.PARAMETRO_SESSAO_MENSAGEM.toString(), lstMensagens);
			}
			forceRedirect(url);

		} catch (Exception e) {
			Logger.getLogger().error("Erro", e);
			Messager.addError("Ocorreu um erro ao redirecionar");
		}
	}

	public void trataMensagens() {
		try {
			@SuppressWarnings("unchecked")
			ArrayList<MensagemRedirecionamento> msgs = Application
					.recuperaValorSessao(ConstantesEnum.PARAMETRO_SESSAO_MENSAGEM.toString(), ArrayList.class);
			if (CollectionUtils.isNotEmpty(msgs)) {
				for (MensagemRedirecionamento mensagem : msgs) {
					Messager.addMessage(mensagem.getSeveridade().getServeridade(),
							mensagem.getSeveridade().getSumario(), mensagem.getMensagem());
				}
				Application.removerParametroSessao(ConstantesEnum.PARAMETRO_SESSAO_MENSAGEM.toString());
			}
		} catch (Exception e) {
			Logger.getLogger().error("Erro", e);
			Messager.addError("Ocorreu um erro ao redirecionar");
		}
	}

	public void abrirDialog(String widgetVar) {
		getContext().executeScript("PF('" + widgetVar + "').show();");
	}

	public void fecharDialog(String widgetVar) {
		getContext().executeScript("PF('" + widgetVar + "').hide();");
	}

	public void atualizarCampos(List<String> formUpdates) {
		if (CollectionUtils.isNotEmpty(formUpdates)) {
			getContext().ajax().update(formUpdates);
		}
	}
}
