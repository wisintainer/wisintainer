package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Fornecedor;

public class FornecedorDAO extends GenericDAO {

	public FornecedorDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public FornecedorDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public List<Fornecedor> buscarTodosFornecedoresSqlNativo() throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM fornecedor ");

		return getArrayList(sb, Fornecedor.class);
	}

	public List<Fornecedor> buscarTodosFornecedores() throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.HQL);
		sb.append("FROM Fornecedor");

		return getArrayList(sb, Fornecedor.class);
	}

	public Fornecedor buscarFornecedoresPorCnpj(String cnpj) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.HQL);
		sb.append("FROM Fornecedor WHERE f where f.cnpj = :cnpj");
		sb.setParameter("cnpj", cnpj);

		return getSingle(sb, Fornecedor.class);
	}
}
