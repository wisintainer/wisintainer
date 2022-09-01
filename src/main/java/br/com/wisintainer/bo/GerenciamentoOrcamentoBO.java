package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.OrcamentoDAO;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.Orcamento;

public class GerenciamentoOrcamentoBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	OrcamentoDAO orcamentoDao;

	public List<Orcamento> buscarOrcamentoPorPlacaVeiculo(String placa) throws Exception {
		return orcamentoDao.buscarOrcamentoPorPlacaDoVeiculo(placa);
	}

}
