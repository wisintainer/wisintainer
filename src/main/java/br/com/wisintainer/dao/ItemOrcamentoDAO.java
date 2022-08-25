package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Orcamento;

public class ItemOrcamentoDAO extends GenericDAO {
	public ItemOrcamentoDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public ItemOrcamentoDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public List<ItemOrcamento> buscarItensPorOrcamento(Integer idOrcamento) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM itemorcamento WHERE orcamento_id = :id_do_orcamento ");
		sb.setParameter("id_do_orcamento", idOrcamento);

		return getArrayList(sb, ItemOrcamento.class);
	}
}
