package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.RespostaOrcamentoDAO;
import br.com.wisintainer.model.RespostaOrcamento;

public class RespostaOrcamentoBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RespostaOrcamentoDAO respostaOrcamentoDao;

	public void salvarRespostaOrcamento(RespostaOrcamento resposta) throws Exception {
		respostaOrcamentoDao.save(resposta);
	}

	public List<RespostaOrcamento> buscarTodasAsRespostasDeOrcamento() throws Exception {
		return respostaOrcamentoDao.buscarTodasAsRespostasDeOrcamento();
	}

}
