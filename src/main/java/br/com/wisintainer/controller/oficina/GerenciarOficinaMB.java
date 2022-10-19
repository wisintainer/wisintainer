package br.com.wisintainer.controller.oficina;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.wisintainer.bo.OficinaBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.Oficina;

@ViewScoped
@Named("gerenciarOficinaMB")
public class GerenciarOficinaMB extends AbstractMB {
	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoBuscarFornecedor()) {
			return true;
		} else {
			return false;
		}
	}

	@Inject
	private OficinaBO oficinaBO;

	private String nomeParaBuscar;

	private List<Oficina> oficinas;

	private Oficina novaOficina;

	private Oficina oficinaSelecionadaParaEditar;

	@PostConstruct
	public void inializar() throws Exception {
		this.oficinas = new ArrayList<Oficina>();
		preencher20Oficinas();
		this.novaOficina = new Oficina();
		this.oficinaSelecionadaParaEditar = new Oficina();
	}

	public String getNomeParaBuscar() {
		return nomeParaBuscar;
	}

	public void setNomeParaBuscar(String nomeParaBuscar) {
		this.nomeParaBuscar = nomeParaBuscar;
	}

	public List<Oficina> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<Oficina> oficinas) {
		this.oficinas = oficinas;
	}

	public Oficina getNovaOficina() {
		return novaOficina;
	}

	public void setNovaOficina(Oficina novaOficina) {
		this.novaOficina = novaOficina;
	}

	public Oficina getOficinaSelecionadaParaEditar() {
		return oficinaSelecionadaParaEditar;
	}

	public void setOficinaSelecionadaParaEditar(Oficina oficinaSelecionadaParaEditar) {
		this.oficinaSelecionadaParaEditar = oficinaSelecionadaParaEditar;
	}

	public void salvarOficina() throws Exception {
		try {
			oficinaBO.salvarOficina(novaOficina);
			limparNovaOficina();
			preencher20Oficinas();
			FacesMessage msg = new FacesMessage("Informação: ");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Oficina Salva com Sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void editarOficina() throws Exception {
		try {
			oficinaBO.editarOficina(oficinaSelecionadaParaEditar);
			preencher20Oficinas();
			FacesMessage msg = new FacesMessage("Informação: ");
			msg.setSeverity(FacesMessage.SEVERITY_INFO);
			msg.setDetail("Oficina Atualizada com Sucesso!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void limparNovaOficina() throws Exception {
		this.novaOficina = new Oficina();
	}

	public void limparOficinaSelecionada() throws Exception {
		this.oficinaSelecionadaParaEditar = new Oficina();
	}

	public void buscarOficinasPorNome() throws Exception {
		this.oficinas = oficinaBO.buscarOficinaPorNome(nomeParaBuscar);
	}

	public void preencher20Oficinas() throws Exception {
		this.oficinas = oficinaBO.buscar20Oficinas();
	}

}
