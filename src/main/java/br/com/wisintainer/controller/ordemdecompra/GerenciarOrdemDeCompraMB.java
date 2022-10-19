package br.com.wisintainer.controller.ordemdecompra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import br.com.wisintainer.bo.OrdemDeCompraBO;
import br.com.wisintainer.controller.AbstractMB;
import br.com.wisintainer.model.Application;
import br.com.wisintainer.model.OrdemDeCompra;

@ViewScoped
@Named("gerenciarOrdemDeCompraMB")
public class GerenciarOrdemDeCompraMB extends AbstractMB {

	private Integer numeroOrdemDeCompraParaBuscar;
	private String placaParaBuscar;
	private List<OrdemDeCompra> ordensDeCompra;
	private OrdemDeCompra ordemSelecionada;

	private Integer opcaoDeBusca;

	@Inject
	private OrdemDeCompraBO ordemDeCompraBO;

	@Override
	public Boolean getPermission() {
		if (Application.getPermissaoGerenciarorcamento()) {
			return true;
		} else {
			return false;
		}
	}

	@PostConstruct
	public void inicializar() throws NumberFormatException, Exception {
		this.ordensDeCompra = new ArrayList<OrdemDeCompra>();
		this.opcaoDeBusca = 0;
	}

	public void buscarOrdemDeCompra() throws Exception {
		if (this.opcaoDeBusca == 0) {
			this.ordensDeCompra = ordemDeCompraBO.buscarOrdemDeCompraPorId(numeroOrdemDeCompraParaBuscar);
		}

		if (this.opcaoDeBusca == 1) {
			this.ordensDeCompra = ordemDeCompraBO.buscarOrdemDeCompraPorPlaca(placaParaBuscar);
		}

	}

	public Integer getNumeroOrdemDeCompraParaBuscar() {
		return numeroOrdemDeCompraParaBuscar;
	}

	public void setNumeroOrdemDeCompraParaBuscar(Integer numeroOrdemDeCompraParaBuscar) {
		this.numeroOrdemDeCompraParaBuscar = numeroOrdemDeCompraParaBuscar;
	}

	public List<OrdemDeCompra> getOrdensDeCompra() {
		return ordensDeCompra;
	}

	public void setOrdensDeCompra(List<OrdemDeCompra> ordensDeCompra) {
		this.ordensDeCompra = ordensDeCompra;
	}

	public OrdemDeCompra getOrdemSelecionada() {
		return ordemSelecionada;
	}

	public void setOrdemSelecionada(OrdemDeCompra ordemSelecionada) {
		this.ordemSelecionada = ordemSelecionada;
	}

	public Integer getOpcaoDeBusca() {
		return opcaoDeBusca;
	}

	public void setOpcaoDeBusca(Integer opcaoDeBusca) {
		this.opcaoDeBusca = opcaoDeBusca;
	}

	public String getPlacaParaBuscar() {
		return placaParaBuscar;
	}

	public void setPlacaParaBuscar(String placaParaBuscar) {
		this.placaParaBuscar = placaParaBuscar;
	}

	public void abrirOrdemDeCompra() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "fileName=hab" + "oc" + ".pdf");
		response.setContentLength(ordemSelecionada.getOrdemdecompra().length);
		facesContext.responseComplete();
		ServletOutputStream os2 = response.getOutputStream();
		os2.write(ordemSelecionada.getOrdemdecompra(), 0, ordemSelecionada.getOrdemdecompra().length);
		os2.flush();
		os2.close();
	}

}
