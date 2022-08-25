package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.ResposataFornecedor;

public class ResposataFornecedorDAO extends GenericDAO {
	public ResposataFornecedorDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public ResposataFornecedorDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public ResposataFornecedor buscarRespostaOrcamentoPorId(Integer id) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM respostafornecedor WHERE id = :id ");
		sb.setParameter("id", id);

		return getSingle(sb, ResposataFornecedor.class);
	}

	public void atualizarResposta(Integer idResposta) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" UPDATE respostafornecedor SET fornecedorrespondeu = true WHERE id = :id ");
		sb.setParameter("id", idResposta);

		execute(sb);
	}
}
