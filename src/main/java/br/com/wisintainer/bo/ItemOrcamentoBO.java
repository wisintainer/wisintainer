package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.ItemOrcamentoDAO;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Orcamento;

public class ItemOrcamentoBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ItemOrcamentoDAO itemOrcamentoDao;

	public List<ItemOrcamento> buscarItensPorOrcamento(Integer idOrcamento) throws Exception {
		return itemOrcamentoDao.buscarItensPorOrcamento(idOrcamento);
	}

	public ItemOrcamento buscarItensPorId(Integer idItem) throws Exception {
		return itemOrcamentoDao.buscarItensPorId(idItem);
	}

}
