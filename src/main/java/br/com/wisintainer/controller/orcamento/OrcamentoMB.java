package br.com.wisintainer.controller.orcamento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Orcamento;

@ViewScoped
@Named("orcamentoMB")
public class OrcamentoMB extends AbstractMB {

	private Orcamento orcamento;
	private ItemOrcamento itemOrcamento = new ItemOrcamento();
	private List<ItemOrcamento> itensOrcamento;
	private String produtoServico;
	private Integer quantidade;
	private List<Fornecedor> fornecedoresBuscados;
	private List<Fornecedor> fornecedoresSelecionados;
	private String nomeFornecedorParaBuscar;
	private Fornecedor fornecedorSelecionadoParaAdicionar;

	@Inject
	private FornecedorBO fornecedorBO;

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
		this.itemOrcamento.setProdutoServico(produtoServico);
		this.itemOrcamento.setQuantidade(quantidade);
		this.itensOrcamento.add(itemOrcamento);
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

	public void buscarFornecedoresPorNome() throws Exception {
		this.fornecedoresBuscados = new ArrayList<Fornecedor>();
		this.fornecedoresBuscados = fornecedorBO.buscarFornecedoresPorNome(nomeFornecedorParaBuscar);
	}

	public void adicionarFornecedor() {
		this.fornecedoresSelecionados.add(fornecedorSelecionadoParaAdicionar);
	}

}
