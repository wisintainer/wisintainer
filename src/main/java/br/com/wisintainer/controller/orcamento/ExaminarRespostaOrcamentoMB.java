package br.com.wisintainer.controller.orcamento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.wisintainer.bo.ResposataFornecedorBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
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
		carregarRespsotas();
	}

	public void carregarRespsotas() throws NumberFormatException, Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idFornecedor = request.getParameter("idfornecedor");
		String idOrcamento = request.getParameter("idrorcamento");
		abrirOrcamentoDeUmFornecedor(Integer.parseInt(idFornecedor), Integer.parseInt(idOrcamento));
	}

	@Inject
	private ResposataFornecedorBO respostaFornecedorBo;

	private List<RespostaOrcamento> respostas;

	public List<RespostaOrcamento> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<RespostaOrcamento> respostas) {
		this.respostas = respostas;
	}

	public void abrirOrcamentoDeUmFornecedor(Integer formecedorId, Integer orcamentoId) throws Exception {
		this.respostas = respostaFornecedorBo.buscarRespostasOrcamentoPorOrcamentoId(formecedorId, orcamentoId);
	}
}
