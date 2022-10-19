package br.com.wisintainer.dao;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;

public class OrdemDeCompraAprovacaoDAO extends GenericDAO {

	public OrdemDeCompraAprovacaoDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public OrdemDeCompraAprovacaoDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}
}
