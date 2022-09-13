package br.com.wisintainer.controller.orcamento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.wisintainer.bo.OrcamentoBO;
import br.com.wisintainer.bo.ResposataFornecedorBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Orcamento;
import br.com.wisintainer.model.RespostaFornecedor;
import br.com.wisintainer.model.RespostaOrcamento;

@ViewScoped
@Named("examinarRespostaOrcamentoMB")
public class ExaminarRespostaOrcamentoMB extends AbstractMB {
	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoExaminarRespostaOrcamento()) {
			return true;
		} else {
			return false;
		}
	}

	@PostConstruct
	public void inicializar() throws NumberFormatException, Exception {
		this.respostas = new ArrayList<RespostaOrcamento>();
		this.respostasSelecionadas = new ArrayList<RespostaOrcamento>();
		carregarRespsotas();
		preencherOrcamento(this.respostas.get(0).getId_orcamento());
	}

	public void carregarRespsotas() throws NumberFormatException, Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idFornecedor = request.getParameter("idfornecedor");
		String idOrcamento = request.getParameter("idrorcamento");
		abrirOrcamentoDeUmFornecedor(Integer.parseInt(idFornecedor), Integer.parseInt(idOrcamento));
	}

	@Inject
	private ResposataFornecedorBO respostaFornecedorBo;

	@Inject
	private OrcamentoBO orcamentoBO;

	private List<RespostaOrcamento> respostas;

	private Orcamento orcamento;

	private List<RespostaOrcamento> respostasSelecionadas;

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void preencherOrcamento(Integer idOrcamento) throws Exception {
		this.orcamento = orcamentoBO.buscarOrcamentoPorId(idOrcamento);
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<RespostaOrcamento> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<RespostaOrcamento> respostas) {
		this.respostas = respostas;
	}

	public List<RespostaOrcamento> getRespostasSelecionadas() {
		return respostasSelecionadas;
	}

	public void setRespostasSelecionadas(List<RespostaOrcamento> respostasSelecionadas) {
		this.respostasSelecionadas = respostasSelecionadas;
	}

	public void abrirOrcamentoDeUmFornecedor(Integer formecedorId, Integer orcamentoId) throws Exception {
		this.respostas = respostaFornecedorBo.buscarRespostasOrcamentoPorOrcamentoId(formecedorId, orcamentoId);
	}
}
