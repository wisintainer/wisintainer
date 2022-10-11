package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.AprovacaoDAO;
import br.com.wisintainer.model.Aprovacao;

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

	public List<Aprovacao> buscarAprovacoesPorOrcamento(Integer idOrcamento) throws Exception {
		return aprovacaoDao.buscarAprovacoesPorOrcamento(idOrcamento);
	}

	public void removerDaAprovacao(Aprovacao aprovacao) throws Exception {
		aprovacaoDao.delete(aprovacao);
	}
}
