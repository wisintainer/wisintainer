package br.com.wisintainer.controller.fornecedor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.bo.FornecedorBO;
import br.com.wisintainer.controller.AbstractMB;

@ViewScoped
@Named("fornecedorMB")
public class FornecedorMB extends AbstractMB {

	private List<Fornecedor> fornecedores;
	private Fornecedor novoFornecedor;
	private Fornecedor buscaFornecedor;

	@Inject
	private FornecedorBO fornecedorBO;

	private Fornecedor fornecedorSelecionado;

	private boolean temFornecedorSelecionado;

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
		this.novoFornecedor = new Fornecedor();
		this.buscaFornecedor = new Fornecedor();
		this.fornecedorSelecionado = new Fornecedor();
		this.temFornecedorSelecionado = false;
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

	public Fornecedor getNovoFornecedor() {
		return novoFornecedor;
	}

	public void setNovoFornecedor(Fornecedor novoFornecedor) {
		this.novoFornecedor = novoFornecedor;
	}

	public Fornecedor getBuscaFornecedor() {
		return buscaFornecedor;
	}

	public void setBuscaFornecedor(Fornecedor buscaFornecedor) {
		this.buscaFornecedor = buscaFornecedor;
	}

	public boolean isTemFornecedorSelecionado() {
		return temFornecedorSelecionado;
	}

	public void setTemFornecedorSelecionado(boolean temFornecedorSelecionado) {
		this.temFornecedorSelecionado = temFornecedorSelecionado;
	}

	public void salvarFornecedor() throws Exception {
		try {
			if (validarCadastroNovoFornecedor()) {
				fornecedorBO.salvarFornecedor(novoFornecedor);
				limparNovoFornecedor();
				FacesMessage msg = new FacesMessage("Informação: ");
				msg.setSeverity(FacesMessage.SEVERITY_INFO);
				msg.setDetail("Fornecedor Salvo com Sucesso!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				PrimeFaces.current().executeScript("PF('wVnovoFornecedor').hide()");
			} else {
				limparNovoFornecedor();
				FacesMessage msg = new FacesMessage("Fornecedor não salvo... ");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				msg.setDetail("Nome, CNPJ e E-mail são obrigatórios!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void limparNovoFornecedor() {
		this.novoFornecedor = new Fornecedor();
	}

	public void limparFornecedorSelecionado() {
		this.fornecedorSelecionado = new Fornecedor();
	}

	public boolean validarCadastroNovoFornecedor() {
		if (!this.novoFornecedor.getNome().isEmpty() && !this.novoFornecedor.getCnpj().isEmpty()
				&& !this.novoFornecedor.getEmail().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public void buscarFornecedoresPorParametro() throws Exception {
		if (!this.buscaFornecedor.getNome().isEmpty() && this.buscaFornecedor.getCnpj().isEmpty()) {
			this.fornecedores = fornecedorBO.buscarFornecedoresPorNome(this.buscaFornecedor.getNome());

			FacesMessage msg = new FacesMessage("Informação ");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Busca Realizada");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (this.buscaFornecedor.getNome().isEmpty() && !this.buscaFornecedor.getCnpj().isEmpty()) {
			this.fornecedores = fornecedorBO.buscarFornecedoresPorCnpj(this.buscaFornecedor.getCnpj());

			FacesMessage msg = new FacesMessage("Informação ");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Busca Realizada");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (!this.buscaFornecedor.getNome().isEmpty() && !this.buscaFornecedor.getCnpj().isEmpty()) {
			this.fornecedores = fornecedorBO.buscarFornecedoresPorNomeeCnpj(this.buscaFornecedor.getNome(),
					this.buscaFornecedor.getCnpj());

			FacesMessage msg = new FacesMessage("Informação ");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Busca Realizada");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		if (this.buscaFornecedor.getNome().isEmpty() && this.buscaFornecedor.getCnpj().isEmpty()) {
			FacesMessage msg = new FacesMessage("Não é possível buscar ");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			msg.setDetail("Digite Nome ou CNPJ para buscar Fornecedores");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public void editarFornecedor() throws Exception {
		if (this.fornecedorSelecionado.getId() == null) {
			FacesMessage msg = new FacesMessage("Não é possível editar:");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Selecione um Fornecedor para editar");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			PrimeFaces.current().executeScript("PF('wVeditarFornecedor').hide()");
			return;
		}
		try {
			fornecedorBO.atualizarFornecedor(fornecedorSelecionado);
			buscarTodosFornecedores();
			FacesMessage msg = new FacesMessage("Informação:");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Fornecedor Atualizado com Sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			PrimeFaces.current().executeScript("PF('wVeditarFornecedor').hide()");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void verificaSeTemFornecedorSelecionadoParaEditar() {
		if (this.fornecedorSelecionado.getId() != null) {
			this.temFornecedorSelecionado = true;
		}
	}

}
