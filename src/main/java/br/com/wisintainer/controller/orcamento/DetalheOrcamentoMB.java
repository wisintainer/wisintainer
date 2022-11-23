package br.com.wisintainer.controller.orcamento;

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
import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.bo.ItemOrcamentoBO;
import br.com.wisintainer.bo.OrcamentoBO;
import br.com.wisintainer.bo.ResposataFornecedorBO;
import br.com.wisintainer.bo.RespostaOrcamentoBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.DetalhamentoResposta;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ItemOrcamento;
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

	@Inject
	private AprovacaoBO aprovacaoBO;

	@Inject
	private RespostaOrcamentoBO respostaOrcamentoBO;

	@Inject
	private ItemOrcamentoBO itemOrcamentoBO;

	private List<Fornecedor> fornecedoresQueResponderam;

	private List<RespostaOrcamento> respostas;

	private Orcamento orcamento;

	private RespostaOrcamento respostaParaAprovar;

	@PostConstruct
	public void inicializar() throws NumberFormatException, Exception {
		this.orcamento = new Orcamento();
		this.fornecedoresQueResponderam = new ArrayList<Fornecedor>();
		buscarOrcamento();
		buscarFornecedoresQueResponderamOorcamento();
		buscarRespostas();
		this.respostaParaAprovar = new RespostaOrcamento();
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

	public List<RespostaOrcamento> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<RespostaOrcamento> respostas) {
		this.respostas = respostas;
	}

	public RespostaOrcamento getRespostaParaAprovar() {
		return respostaParaAprovar;
	}

	public void setRespostaParaAprovar(RespostaOrcamento respostaParaAprovar) {
		this.respostaParaAprovar = respostaParaAprovar;
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

	public void buscarRespostas() throws Exception {
		if (orcamento.getId() != null) {
			this.respostas = fornecedorBo.buscarRespostas(orcamento.getId());
		}

		for (RespostaOrcamento resp : respostas) {
			ItemOrcamento io = itemOrcamentoBO.buscarItensPorId(resp.getId_item());
			resp.setCor(io.getCor());
		}

	}

	public void aprova() throws Exception {
		try {
			Aprovacao aprovacao = new Aprovacao();
			aprovacao.setItem_id(respostaParaAprovar.getId_item());
			aprovacao.setOrcamento_id(respostaParaAprovar.getId_orcamento());
			aprovacao.setResposta_orcamento_id(respostaParaAprovar.getId());
			aprovacao.setFornecedor_id(respostaParaAprovar.getId_fornecedor());
			aprovacao.setFornecedor_nome(respostaParaAprovar.getNomeFornecedor());
			aprovacao.setItem_nome(respostaParaAprovar.getNome_item());
			aprovacao.setItem_quantidade(respostaParaAprovar.getQuantidade());
			aprovacao.setItem_tememestoque(respostaParaAprovar.isTememestoque());
			aprovacao.setItem_valor(respostaParaAprovar.getValor());

			if (respostaParaAprovar.isAprovado()) {
				respostaOrcamentoBO.colocarRespostaComoReprovada(respostaParaAprovar.getId());
				buscarRespostas();
			} else {
				respostaOrcamentoBO.colocarRespostaComoAprovada(respostaParaAprovar.getId());
				buscarRespostas();
			}
			if (aprovacaoBO.jaexisteaprovacao(respostaParaAprovar)) {
				aprovacaoBO.retirarDaAprovacao(respostaParaAprovar);
			} else {
				aprovacaoBO.salvarAprovacao(aprovacao);
			}

		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Erro na aprovação");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setDetail(e.getStackTrace().toString());
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void aprovarSelecionados() throws Exception {
		List<Aprovacao> aprovacoes = new ArrayList<Aprovacao>();

		for (RespostaOrcamento resposta : respostas) {
			if (resposta.isAprovado()) {
				Aprovacao aprovacao = new Aprovacao();
				aprovacao.setItem_id(resposta.getId_item());
				aprovacao.setOrcamento_id(resposta.getId_orcamento());
				aprovacao.setResposta_orcamento_id(resposta.getId());
				aprovacao.setFornecedor_id(resposta.getId_fornecedor());
				aprovacao.setFornecedor_nome(resposta.getNomeFornecedor());
				aprovacao.setItem_nome(resposta.getNome_item());
				aprovacao.setItem_quantidade(resposta.getQuantidade());
				aprovacao.setItem_tememestoque(resposta.isTememestoque());
				aprovacao.setItem_valor(resposta.getValor());
				respostaOrcamentoBO.colocarRespostaComoAprovada(resposta.getId());

				aprovacoes.add(aprovacao);
			}

		}

		if (!aprovacoes.isEmpty()) {
			try {
				for (Aprovacao aprovacao : aprovacoes) {
					aprovacaoBO.salvarAprovacao(aprovacao);
				}
				FacesMessage msg = new FacesMessage("Aprovação Realizada!");
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				msg.setDetail("O(s) Item(s) agora constam aprovados no orçamento");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			FacesMessage msg = new FacesMessage("Erro na aprovação");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setDetail("Selecione pelo menos um item");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

}
