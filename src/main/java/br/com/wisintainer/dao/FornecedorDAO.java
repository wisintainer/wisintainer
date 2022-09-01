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
		sb.append(" SELECT * FROM fornecedor LIMIT 8 ");

		return getArrayList(sb, Fornecedor.class);
	}

	public List<Fornecedor> buscarFornecedoresPorNome(String nome) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM fornecedor WHERE nome like :nome ");
		sb.setParameterLike("nome", nome);

		return getArrayList(sb, Fornecedor.class);
	}

	public Fornecedor buscarFornecedorPorId(Integer id) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("SELECT * FROM fornecedor WHERE id = :id ");
		sb.setParameter("id", id);

		return getSingle(sb, Fornecedor.class);
	}

	public List<Fornecedor> buscarTodosFornecedores() throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.HQL);
		sb.append("FROM Fornecedor");

		return getArrayList(sb, Fornecedor.class);
	}

	public Fornecedor buscarFornecedorPorCnpj(String cnpj) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("FROM Fornecedor WHERE f where f.cnpj = :cnpj");
		sb.setParameter("cnpj", cnpj);

		return getSingle(sb, Fornecedor.class);
	}

	public List<Fornecedor> buscarFornecedoresPorCnpj(String cnpj) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("SELECT * FROM fornecedor WHERE cnpj = :cnpj");
		sb.setParameter("cnpj", cnpj);

		return getArrayList(sb, Fornecedor.class);
	}

	public List<Fornecedor> buscarFornecedoresPorNomeeCnpj(String nome, String cnpj) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("SELECT * FROM fornecedor WHERE cnpj = :cnpj AND nome like :nome");
		sb.setParameterLike("nome", nome);
		sb.setParameter("cnpj", cnpj);

		return getArrayList(sb, Fornecedor.class);
	}

	public List<Fornecedor> buscarFornecedoresQueResponderamOorcamento(Integer idOrcamento) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("SELECT F.* FROM fornecedor F");
		sb.append("INNER JOIN respostafornecedor rf on rf.id_fornecedor = F.id");
		sb.append("WHERE rf.id_orcamento = :id AND fornecedorrespondeu = true");

		sb.setParameter("id", idOrcamento);

		return getArrayList(sb, Fornecedor.class);
	}
}
