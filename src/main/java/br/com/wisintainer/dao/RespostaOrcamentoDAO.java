package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.RespostaOrcamento;

public class RespostaOrcamentoDAO extends GenericDAO {
	public RespostaOrcamentoDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public RespostaOrcamentoDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public List<RespostaOrcamento> buscarTodasAsRespostasDeOrcamento() throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM respostaorcamento ");

		return getArrayList(sb, RespostaOrcamento.class);
	}

}
