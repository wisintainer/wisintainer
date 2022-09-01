package br.com.wisintainer.controller.orcamento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.bo.OrcamentoBO;
import br.com.wisintainer.bo.ResposataFornecedorBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.DetalhamentoResposta;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.Orcamento;
import br.com.wisintainer.model.RespostaFornecedor;
import br.com.wisintainer.model.RespostaOrcamento;

@ViewScoped
@Named("detalheOrcamentoMB")
public class DetalheOrcamentoMB extends AbstractMB {
	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoGerenciarorcamento()) {
			return true;
		} else {
			return false;
		}
	}

	@Inject
	private OrcamentoBO orcamentoBo;

	@Inject
	private FornecedorBO fornecedorBo;

	private List<Fornecedor> fornecedoresQueResponderam;

	private Orcamento orcamento;

	@PostConstruct
	public void inicializar() throws NumberFormatException, Exception {
		this.orcamento = new Orcamento();
		this.fornecedoresQueResponderam = new ArrayList<Fornecedor>();
		buscarOrcamento();
		buscarFornecedoresQueResponderamOorcamento();
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<Fornecedor> getFornecedoresQueResponderam() {
		return fornecedoresQueResponderam;
	}

	public void setFornecedoresQueResponderam(List<Fornecedor> fornecedoresQueResponderam) {
		this.fornecedoresQueResponderam = fornecedoresQueResponderam;
	}

	public void buscarOrcamento() throws NumberFormatException, Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String idOrcamento = request.getParameter("orcamento");

		this.orcamento = orcamentoBo.buscarOrcamentoPorId(Integer.parseInt(idOrcamento));
	}

	public void buscarFornecedoresQueResponderamOorcamento() throws Exception {
		if (orcamento.getId() != null) {
			this.fornecedoresQueResponderam = fornecedorBo.buscarFornecedoresQueResponderamOorcamento(orcamento.getId());
		}

	}

}
