package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.OrdemDeCompraAprovacaoDAO;
import br.com.wisintainer.dao.OrdemDeCompraDAO;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.OrdemDeCompra;
import br.com.wisintainer.model.OrdemDeCompraAprovacoes;

public class OrdemDeCompraBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private OrdemDeCompraDAO ordemDeCompraDAO;

	@Inject
	private OrdemDeCompraAprovacaoDAO ordemDeCompraAprovacoesDAO;

	public void salvarOrdemDeCompra(OrdemDeCompra ordemDeCompra, List<Aprovacao> aprovacoes) throws Exception {
		try {
			Integer codigo = (Integer) ordemDeCompraDAO.saveReturningSaved(ordemDeCompra);

			for (Aprovacao ap : aprovacoes) {
				OrdemDeCompraAprovacoes oca = new OrdemDeCompraAprovacoes();
				oca.setOrdemdecompra_id(codigo);
				oca.setAprovacao_id(ap.getId());

				ordemDeCompraAprovacoesDAO.save(oca);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Integer retornaIdProx() throws Exception {
		if (ordemDeCompraDAO.retornaIdProx() == null) {
			return 1;
		} else {
			return ordemDeCompraDAO.retornaIdProx() + 1;
		}

	}

	public List<OrdemDeCompra> buscarOrdemDeCompraPorId(Integer id) throws Exception {
		return ordemDeCompraDAO.buscarOrdemDeCompraPorId(id);
	}

	public List<OrdemDeCompra> buscarOrdemDeCompraPorPlaca(String placa) throws Exception {
		return ordemDeCompraDAO.buscarOrdemDeCompraPorPlaca(placa);
	}

}
