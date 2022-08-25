package br.com.wisintainer.dao;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;

public class OrcamentoDAO extends GenericDAO {
	public OrcamentoDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public OrcamentoDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}
}
