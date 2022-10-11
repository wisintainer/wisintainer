package br.com.wisintainer.controller.orcamento;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.wisintainer.bo.AprovacaoBO;
import br.com.wisintainer.bo.OrcamentoBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.Orcamento;

@ViewScoped
@Named("visualizacaoOrcamentoMB")
public class VisualizacaoOrcamentoMB extends AbstractMB {

	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoCriarorcamento()) {
			return true;
		} else {
			return false;
		}
	}

	@Inject
	private OrcamentoBO orcamentoBO;

	@Inject
	private AprovacaoBO aprovacaoBO;

	private Orcamento orcamento;

	private List<Aprovacao> aprovaoesDoOrcamento;

	private Aprovacao aprovacaoParaRemover;

	@PostConstruct
	public void inicializar() throws NumberFormatException, Exception {
		carregarOrcamento();
		this.aprovaoesDoOrcamento = new ArrayList<Aprovacao>();
		carregarItensAprovadosDoOrcamento();
		this.aprovacaoParaRemover = new Aprovacao();
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<Aprovacao> getAprovaoesDoOrcamento() {
		return aprovaoesDoOrcamento;
	}

	public void setAprovaoesDoOrcamento(List<Aprovacao> aprovaoesDoOrcamento) {
		this.aprovaoesDoOrcamento = aprovaoesDoOrcamento;
	}

	public Aprovacao getAprovacaoParaRemover() {
		return aprovacaoParaRemover;
	}

	public void setAprovacaoParaRemover(Aprovacao aprovacaoParaRemover) {
		this.aprovacaoParaRemover = aprovacaoParaRemover;
	}

	public void carregarOrcamento() throws NumberFormatException, Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idOrcamento = request.getParameter("orcamento");

		this.orcamento = orcamentoBO.buscarOrcamentoPorId(Integer.parseInt(idOrcamento));
	}

	public void carregarItensAprovadosDoOrcamento() throws Exception {
		this.aprovaoesDoOrcamento = aprovacaoBO.buscarAprovacoesPorOrcamento(this.orcamento.getId());
	}

	public String valorTotalAprovadoOrcamento() {
		Double total = 0.0;

		for (Aprovacao itens : aprovaoesDoOrcamento) {
			total = total + (itens.getItem_valor() * itens.getItem_quantidade());
		}

		DecimalFormat decimal = new DecimalFormat("###,###,###,##0.00");
		return decimal.format(total);
	}

	public void removerAprovacaoDoOrcamento() throws Exception {
		if (this.aprovacaoParaRemover != null) {
			try {
				aprovacaoBO.removerDaAprovacao(aprovacaoParaRemover);
				carregarItensAprovadosDoOrcamento();
				FacesMessage msg = new FacesMessage("Item removido do Orçamento!");
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (Exception e) {
				e.printStackTrace();
				FacesMessage msg = new FacesMessage("Erro ao remover item do Orçamento!");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} else {
			System.out.println("AprovacaoParaRemover está null");
		}

	}

}
