package br.com.wisintainer.controller.orcamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.bo.OficinaBO;
import br.com.wisintainer.bo.OrcamentoBO;
import br.com.wisintainer.bo.ResposataFornecedorBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.helper.SendEmail;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Oficina;
import br.com.wisintainer.model.Orcamento;

@ViewScoped
@Named("orcamentoMB")
public class OrcamentoMB extends AbstractMB {

	private Orcamento orcamento;
	private ItemOrcamento itemOrcamento = new ItemOrcamento();
	private ItemOrcamento itemOrcamentoExcluir = new ItemOrcamento();
	private List<ItemOrcamento> itensOrcamento;
	private String produtoServico;
	private Integer quantidade;
	private List<Fornecedor> fornecedoresBuscados;
	private List<Oficina> oficinasBuscadas;
	private List<Fornecedor> fornecedoresSelecionados;
	private List<Oficina> oficinasSelecionadas;
	private String nomeFornecedorParaBuscar;
	private String nomeOficinaParaBuscar;
	private Fornecedor fornecedorSelecionadoParaAdicionar;
	private Fornecedor fornecedorSelecionadoParaRemover;
	private Oficina oficinaSelecionadaParaAdicionar;
	private Oficina oficinaSelecionadaParaRemover;
	private boolean skip;
	private Integer numeroItem;

	@Inject
	private FornecedorBO fornecedorBO;

	@Inject
	private OficinaBO oficinaBO;

	@Inject
	private OrcamentoBO orcamentoBO;

	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoCriarorcamento()) {
			return true;
		} else {
			return false;
		}
	}

	@PostConstruct
	private void inicializar() {
		this.orcamento = new Orcamento();
		this.itemOrcamento = new ItemOrcamento();
		this.itensOrcamento = new ArrayList<ItemOrcamento>();
		this.fornecedoresSelecionados = new ArrayList<Fornecedor>();
		this.oficinasSelecionadas = new ArrayList<Oficina>();
		this.numeroItem = 0;
		this.orcamento.setDataCriacao(new Date());

	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public List<ItemOrcamento> getItensOrcamento() {
		return itensOrcamento;
	}

	public void setItensOrcamento(List<ItemOrcamento> itensOrcamento) {
		this.itensOrcamento = itensOrcamento;
	}

	public ItemOrcamento getItemOrcamento() {
		return itemOrcamento;
	}

	public void setItemOrcamento(ItemOrcamento itemOrcamento) {
		this.itemOrcamento = itemOrcamento;
	}

	public void adicionarItem() {
		this.itemOrcamento = new ItemOrcamento();
		this.itemOrcamento.setProdutoServico(produtoServico);
		this.itemOrcamento.setQuantidade(quantidade);
		this.itensOrcamento.add(itemOrcamento);
		numeroItem = numeroItem + 1;
	}

	public void removerItem() {
		if (this.itemOrcamentoExcluir != null) {
			this.itensOrcamento.remove(itemOrcamentoExcluir);
			FacesMessage msg = new FacesMessage("Informação: ");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail(itemOrcamentoExcluir.getProdutoServico() + " Excluído");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} else {
			FacesMessage msg = new FacesMessage("Erro: ");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setDetail("Erro ao excluir: \"+ \"Objeto Nulo");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public String getProdutoServico() {
		return produtoServico;
	}

	public void setProdutoServico(String produtoServico) {
		this.produtoServico = produtoServico;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public List<Fornecedor> getFornecedoresBuscados() {
		return fornecedoresBuscados;
	}

	public void setFornecedoresBuscados(List<Fornecedor> fornecedoresBuscados) {
		this.fornecedoresBuscados = fornecedoresBuscados;
	}

	public String getNomeFornecedorParaBuscar() {
		return nomeFornecedorParaBuscar;
	}

	public void setNomeFornecedorParaBuscar(String nomeFornecedorParaBuscar) {
		this.nomeFornecedorParaBuscar = nomeFornecedorParaBuscar;
	}

	public Fornecedor getFornecedorSelecionadoParaAdicionar() {
		return fornecedorSelecionadoParaAdicionar;
	}

	public void setFornecedorSelecionadoParaAdicionar(Fornecedor fornecedorSelecionadoParaAdicionar) {
		this.fornecedorSelecionadoParaAdicionar = fornecedorSelecionadoParaAdicionar;
	}

	public List<Fornecedor> getFornecedoresSelecionados() {
		return fornecedoresSelecionados;
	}

	public void setFornecedoresSelecionados(List<Fornecedor> fornecedoresSelecionados) {
		this.fornecedoresSelecionados = fornecedoresSelecionados;
	}

	public List<Oficina> getOficinasBuscadas() {
		return oficinasBuscadas;
	}

	public void setOficinasBuscadas(List<Oficina> oficinasBuscadas) {
		this.oficinasBuscadas = oficinasBuscadas;
	}

	public void buscarFornecedoresPorNome() throws Exception {
		this.fornecedoresBuscados = new ArrayList<Fornecedor>();
		this.fornecedoresBuscados = fornecedorBO.buscarFornecedoresPorNome(nomeFornecedorParaBuscar);
	}

	public void buscarOficinasPorNome() throws Exception {
		this.oficinasBuscadas = new ArrayList<Oficina>();
		this.oficinasBuscadas = oficinaBO.buscarOficinaPorNome(nomeOficinaParaBuscar);
	}

	public void adicionarFornecedor() {
		if (fornecedorSelecionadoParaAdicionar != null) {
			this.fornecedoresSelecionados.add(fornecedorSelecionadoParaAdicionar);
		}

	}

	public void removerFornecedor() {
		if (fornecedorSelecionadoParaRemover != null) {
			this.fornecedoresSelecionados.remove(fornecedorSelecionadoParaRemover);
		}

	}

	public void adicionarOficina() {
		if (oficinaSelecionadaParaAdicionar != null) {
			this.oficinasSelecionadas.add(oficinaSelecionadaParaAdicionar);
		}

	}

	public void removerOficina() {
		if (oficinaSelecionadaParaRemover != null) {
			this.oficinasSelecionadas.remove(oficinaSelecionadaParaRemover);
		}

	}

	public Oficina getOficinaSelecionadaParaAdicionar() {
		return oficinaSelecionadaParaAdicionar;
	}

	public void setOficinaSelecionadaParaAdicionar(Oficina oficinaSelecionadaParaAdicionar) {
		this.oficinaSelecionadaParaAdicionar = oficinaSelecionadaParaAdicionar;
	}

	public ItemOrcamento getItemOrcamentoExcluir() {
		return itemOrcamentoExcluir;
	}

	public void setItemOrcamentoExcluir(ItemOrcamento itemOrcamentoExcluir) {
		this.itemOrcamentoExcluir = itemOrcamentoExcluir;
	}

	public Integer getNumeroItem() {
		return numeroItem;
	}

	public void setNumeroItem(Integer numeroItem) {
		this.numeroItem = numeroItem;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String getNomeOficinaParaBuscar() {
		return nomeOficinaParaBuscar;
	}

	public void setNomeOficinaParaBuscar(String nomeOficinaParaBuscar) {
		this.nomeOficinaParaBuscar = nomeOficinaParaBuscar;
	}

	public List<Oficina> getOficinasSelecionadas() {
		return oficinasSelecionadas;
	}

	public void setOficinasSelecionadas(List<Oficina> oficinasSelecionadas) {
		this.oficinasSelecionadas = oficinasSelecionadas;
	}

	public Fornecedor getFornecedorSelecionadoParaRemover() {
		return fornecedorSelecionadoParaRemover;
	}

	public void setFornecedorSelecionadoParaRemover(Fornecedor fornecedorSelecionadoParaRemover) {
		this.fornecedorSelecionadoParaRemover = fornecedorSelecionadoParaRemover;
	}

	public Oficina getOficinaSelecionadaParaRemover() {
		return oficinaSelecionadaParaRemover;
	}

	public void setOficinaSelecionadaParaRemover(Oficina oficinaSelecionadaParaRemover) {
		this.oficinaSelecionadaParaRemover = oficinaSelecionadaParaRemover;
	}

	public String onFlowProcess(FlowEvent event) {
		if (skip) {
			skip = false; // reset in case user goes back
			return "confirm";
		} else {
			return event.getNewStep();
		}
	}

	public void salvarSolicitacaoDeOrcamento() throws Exception {
		try {
			orcamento.setItensOrcamento(itensOrcamento);
			orcamento.setStatus(0);

			Oficina of = new Oficina();
			of = oficinasSelecionadas.get(0);

			orcamento.setEntrega_empresa(of.getNome());
			orcamento.setEntrega_responsavel(of.getResponsavel());
			orcamento.setEntrega_endereco(of.getEndereco());
			orcamento.setEntrega_numero(of.getNumero());
			orcamento.setEntrega_bairro(of.getBairro());
			orcamento.setEntrega_cidade(of.getCidade());
			orcamento.setEntrega_estado(of.getEstado());
			orcamento.setEntrega_cep(of.getCep());
			orcamento.setEntrega_telefone(of.getTelefone());
			orcamento.setEntrega_email(of.getEmail());

			orcamentoBO.salvarSolicitacaoDeOrcamento(orcamento, this.fornecedoresSelecionados);
			FacesMessage msg = new FacesMessage("Informação: ");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Solicitações de Orçamentos enviadas!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
