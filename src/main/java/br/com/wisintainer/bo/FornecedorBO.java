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

	public Fornecedor buscarFornecedorPorId(Integer id) throws Exception {
		return fornecedorDao.buscarFornecedorPorId(id);
	}

	public Fornecedor buscarFornecedorPorCnpj(String cnpj) throws Exception {
		return fornecedorDao.buscarFornecedorPorCnpj(cnpj);
	}

	public List<Fornecedor> buscarFornecedoresPorCnpj(String cnpj) throws Exception {
		return fornecedorDao.buscarFornecedoresPorCnpj(cnpj);
	}

	public void salvarFornecedor(Fornecedor fornecedor) throws Exception {
		fornecedorDao.save(fornecedor);
	}

	public List<Fornecedor> buscarFornecedoresPorNomeeCnpj(String nome, String cnpj) throws Exception {
		return fornecedorDao.buscarFornecedoresPorNomeeCnpj(nome, cnpj);
	}

	public void atualizarFornecedor(Fornecedor fornecedor) throws Exception {
		fornecedorDao.update(fornecedor);
	}

	public List<Fornecedor> buscarFornecedoresQueResponderamOorcamento(Integer idOrcamento) throws Exception {
		return fornecedorDao.buscarFornecedoresQueResponderamOorcamento(idOrcamento);
	}
}
