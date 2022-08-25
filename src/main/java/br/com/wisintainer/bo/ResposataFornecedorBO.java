package br.com.wisintainer.bo;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.wisintainer.dao.ResposataFornecedorDAO;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Orcamento;
import br.com.wisintainer.model.ResposataFornecedor;

public class ResposataFornecedorBO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ResposataFornecedorDAO resposataFornecedorDao;

	// Salva na tabela respostafornecedor somente para gerar o id da resposta para
	// poder enviar ao fornecedor por e-mail.
	public Integer salvarPreResposta(Orcamento orcamento, Fornecedor fornecedor) throws Exception {

		ResposataFornecedor resposta = new ResposataFornecedor();
		resposta.setId_fornecedor(fornecedor.getId());
		resposta.setId_orcamento(orcamento.getId());

		return (Integer) resposataFornecedorDao.saveReturningSaved(resposta);
	}

	public ResposataFornecedor buscarRespostaOrcamentoPorId(Integer idResposta) throws Exception {
		return resposataFornecedorDao.buscarRespostaOrcamentoPorId(idResposta);
	}

	public void atualizarResposta(Integer idResposta) throws Exception {
		resposataFornecedorDao.atualizarResposta(idResposta);
	}

}