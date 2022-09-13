package br.com.wisintainer.controller.orcamento;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.RepositoryIdHelper;
import org.omnifaces.util.Faces;

import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.bo.ItemOrcamentoBO;
import br.com.wisintainer.bo.ResposataFornecedorBO;
import br.com.wisintainer.bo.RespostaOrcamentoBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Orcamento;
import br.com.wisintainer.model.RespostaFornecedor;
import br.com.wisintainer.model.RespostaOrcamento;

@ViewScoped
@Named("respostaOrcamentoMB")
public class RespostaOrcamentoMB extends AbstractMB {

	private RespostaFornecedor resposta;
	private Fornecedor fornecedor;
	private List<ItemOrcamento> itensOrcamento;

	@Inject
	private ResposataFornecedorBO respostaFornecedorBO;

	@Inject
	private FornecedorBO fornecedorBO;

	@Inject
	private ItemOrcamentoBO itemOrcamentoBO;

	@Inject
	private RespostaOrcamentoBO respostaOrcamentoBO;

	private boolean envioDoOrcamentoLiberado;

	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoRespondercamento()) {
			return true;
		} else {
			return false;
		}
	}

	@PostConstruct
	private void inicializar() throws Exception {
		this.resposta = new RespostaFornecedor();
		this.fornecedor = new Fornecedor();
		this.itensOrcamento = new ArrayList<ItemOrcamento>();
		this.envioDoOrcamentoLiberado = false;

		buscarRespostaOrcamentoPorId(decodificarIdResposta(extrairDadosRequisicao()));
	}

	public RespostaFornecedor getResposta() {
		return resposta;
	}

	public void setResposta(RespostaFornecedor resposta) {
		this.resposta = resposta;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public List<ItemOrcamento> getItensOrcamento() {
		return itensOrcamento;
	}

	public void setItensOrcamento(List<ItemOrcamento> itensOrcamento) {
		this.itensOrcamento = itensOrcamento;
	}

	public boolean isEnvioDoOrcamentoLiberado() {
		return envioDoOrcamentoLiberado;
	}

	public void setEnvioDoOrcamentoLiberado(boolean envioDoOrcamentoLiberado) {
		this.envioDoOrcamentoLiberado = envioDoOrcamentoLiberado;
	}

	public void buscarRespostaOrcamentoPorId(String idResposta) throws Exception {
		this.resposta = respostaFornecedorBO.buscarRespostaOrcamentoPorId(Integer.parseInt(idResposta));
		this.fornecedor = fornecedorBO.buscarFornecedorPorId(resposta.getId_fornecedor());
		this.itensOrcamento = itemOrcamentoBO.buscarItensPorOrcamento(resposta.getId_orcamento());
	}

	public String extrairDadosRequisicao() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		if (request.getParameter("identify") != null && !request.getParameter("identify").equals("")) {
			return request.getParameter("identify");
		}
		return null;
	}

	public String decodificarIdResposta(String idRespostaCriptografado) {
		byte[] decodedBytes = Base64.getDecoder().decode(idRespostaCriptografado);
		return new String(decodedBytes);
	}

	public void validaSePodeEnviar() {
		// Se houver regra implementar aqui
		this.envioDoOrcamentoLiberado = true;
	}

	public void salvarRespostaOrcamento() throws Exception {
		try {
			for (ItemOrcamento item : this.itensOrcamento) {
				RespostaOrcamento respostaOrcamento = new RespostaOrcamento();
				respostaOrcamento.setId_item(item.getId());
				respostaOrcamento.setId_orcamento(item.getOrcamento_id());
				respostaOrcamento.setId_resposta(this.resposta.getId());
				respostaOrcamento.setTememestoque(item.isTememestoque());
				respostaOrcamento.setValor(item.getValor());
				respostaOrcamento.setNome_item(item.getProdutoServico());
				respostaOrcamento.setQuantidade(item.getQuantidade());
				respostaOrcamento.setId_fornecedor(this.resposta.getId_fornecedor());
				respostaOrcamento.setNomeFornecedor(fornecedorBO.buscarFornecedorPorId(this.resposta.getId_fornecedor()).getNome());
				respostaOrcamento.setData_resposta(new Date());

				validaSePodeEnviar();
				if (envioDoOrcamentoLiberado) {
					respostaOrcamentoBO.salvarRespostaOrcamento(respostaOrcamento);
					this.resposta.setFornecedorrespondeu(true);
					respostaFornecedorBO.atualizarResposta(this.resposta.getId());
				}
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Informação: ");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setDetail("Erro ao enviar o orçamento");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}

	}
}
