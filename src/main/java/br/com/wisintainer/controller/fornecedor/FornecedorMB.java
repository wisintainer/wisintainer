package br.com.wisintainer.controller.fornecedor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.controller.AbstractMB;

@ViewScoped
@Named("fornecedorMB")
public class FornecedorMB extends AbstractMB {

	private List<Fornecedor> fornecedores;

	@Inject
	private FornecedorBO fornecedorBO;

	private Fornecedor fornecedorSelecionado = new Fornecedor();

	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoBuscarFornecedor()) {
			return true;
		} else {
			return false;
		}
	}

	@PostConstruct
	public void inicializar() throws Exception {
		buscarTodosFornecedores();
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public void buscarTodosFornecedores() throws Exception {
		this.fornecedores = fornecedorBO.buscarTodosFornecedores();
	}

	public Fornecedor getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

}
