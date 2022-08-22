package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.FornecedorDAO;
import br.com.wisintainer.model.Fornecedor;

public class FornecedorBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FornecedorDAO fornecedorDao;

	public List<Fornecedor> buscarTodosFornecedores() throws Exception {
		return fornecedorDao.buscarTodosFornecedoresSqlNativo();
	}
	
	public List<Fornecedor> buscarFornecedoresPorNome(String nome) throws Exception {
		return fornecedorDao.buscarFornecedoresPorNome(nome);
	}

}
