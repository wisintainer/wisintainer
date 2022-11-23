package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.AprovacaoDAO;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.RespostaOrcamento;

public class AprovacaoBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AprovacaoDAO aprovacaoDao;

	public void salvarAprovacao(Aprovacao aprovacao) throws Exception {
		aprovacaoDao.save(aprovacao);
	}

	public void removerDaAprovacao(Aprovacao aprovacao) throws Exception {
		aprovacaoDao.delete(aprovacao);
	}

	public void retirarDaAprovacao(RespostaOrcamento respostaOrcamento) throws Exception {
		aprovacaoDao.retirarDaAprovacao(respostaOrcamento);
	}

	public List<Aprovacao> buscarAprovacoesPorOrcamento(Integer idOrcamento) throws Exception {
		return aprovacaoDao.buscarAprovacoesPorOrcamento(idOrcamento);
	}

	public List<Integer> buscarTodosFornecedoresDaAprovacoesPorOrcamento(Integer idOrcamento) throws Exception {
		return aprovacaoDao.buscarTodosFornecedoresDaAprovacoesPorOrcamento(idOrcamento);
	}

	public List<Aprovacao> buscarAprovacoesPorOrcamentoEFornecedor(Integer idOrcamento, Integer idFornecedor) throws Exception {
		return aprovacaoDao.buscarAprovacoesPorOrcamentoEFornecedor(idOrcamento, idFornecedor);
	}

	public boolean jaexisteaprovacao(RespostaOrcamento respostaOrcamento) throws Exception {
		if (aprovacaoDao.jaexisteaprovacao(respostaOrcamento).isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
}
