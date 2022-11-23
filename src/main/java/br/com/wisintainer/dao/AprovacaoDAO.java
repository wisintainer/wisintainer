package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.RespostaOrcamento;

public class AprovacaoDAO extends GenericDAO {
	public AprovacaoDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public AprovacaoDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public List<Aprovacao> buscarAprovacoesPorOrcamento(Integer idOrcamento) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM aprovacao WHERE orcamento_id = :idOrcamento ");
		sb.setParameter("idOrcamento", idOrcamento);

		return getArrayList(sb, Aprovacao.class);
	}

	public List<Integer> buscarTodosFornecedoresDaAprovacoesPorOrcamento(Integer idOrcamento) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT DISTINCT fornecedor_id FROM aprovacao WHERE orcamento_id = :idOrcamento ");
		sb.setParameter("idOrcamento", idOrcamento);

		return getArrayList(sb, Integer.class);
	}

	public List<Aprovacao> buscarAprovacoesPorOrcamentoEFornecedor(Integer idOrcamento, Integer idFornecedor) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM aprovacao WHERE orcamento_id = :idOrcamento AND fornecedor_id = :idFornecedor ");
		sb.setParameter("idOrcamento", idOrcamento);
		sb.setParameter("idFornecedor", idFornecedor);

		return getArrayList(sb, Aprovacao.class);
	}

	public List<Aprovacao> jaexisteaprovacao(RespostaOrcamento respostaOrcamento) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM aprovacao WHERE resposta_orcamento_id = :id ");
		sb.setParameter("id", respostaOrcamento.getId());

		return getArrayList(sb, Aprovacao.class);
	}

	public void retirarDaAprovacao(RespostaOrcamento respostaOrcamento) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" DELETE FROM aprovacao WHERE resposta_orcamento_id = :id ");
		sb.setParameter("id", respostaOrcamento.getId());
		execute(sb);
	}
}
