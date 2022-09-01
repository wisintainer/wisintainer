package br.com.wisintainer.controller.orcamento;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.wisintainer.bo.GerenciamentoOrcamentoBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Orcamento;

@ViewScoped
@Named("gerenciarOrcamentoMB")
public class GerenciarOrcamentoMB extends AbstractMB {

	private Orcamento orcamentoBusca;
	private List<Orcamento> orcamentosEncontrados;

	@Inject
	private GerenciamentoOrcamentoBO gerenciamentoOrcamentoBo;

	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoGerenciarorcamento()) {
			return true;
		} else {
			return false;
		}
	}

	@PostConstruct
	public void inicializar() {
		this.orcamentoBusca = new Orcamento();
		this.orcamentosEncontrados = new ArrayList<Orcamento>();
	}

	public Orcamento getOrcamentoBusca() {
		return orcamentoBusca;
	}

	public void setOrcamentoBusca(Orcamento orcamentoBusca) {
		this.orcamentoBusca = orcamentoBusca;
	}

	public void buscarOrcamentoPorPlacaVeiculo() throws Exception {
		this.orcamentosEncontrados = gerenciamentoOrcamentoBo.buscarOrcamentoPorPlacaVeiculo(orcamentoBusca.getVeiculoPlaca());
	}

	public List<Orcamento> getOrcamentosEncontrados() {
		return orcamentosEncontrados;
	}

	public void setOrcamentosEncontrados(List<Orcamento> orcamentosEncontrados) {
		this.orcamentosEncontrados = orcamentosEncontrados;
	}

}
