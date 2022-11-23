package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.RespostaFornecedor;
import br.com.wisintainer.model.RespostaOrcamento;

public class RespostaFornecedorDAO extends GenericDAO {
	public RespostaFornecedorDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public RespostaFornecedorDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public RespostaFornecedor buscarRespostaOrcamentoPorId(Integer id) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM respostafornecedor WHERE id = :id ");
		sb.setParameter("id", id);

		return getSingle(sb, RespostaFornecedor.class);
	}

	public List<RespostaFornecedor> buscarRespostasFornecedoresPorIdOrcamento(Integer id) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM respostafornecedor WHERE id_orcamento = :id AND fornecedorrespondeu = true ");
		sb.setParameter("id", id);

		return getArrayList(sb, RespostaFornecedor.class);
	}

	public void atualizarResposta(Integer idResposta) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" UPDATE respostafornecedor SET fornecedorrespondeu = true WHERE id = :id ");
		sb.setParameter("id", idResposta);

		execute(sb);
	}

	public List<RespostaOrcamento> buscarRespostasOrcamentoPorOrcamentoIdEFornecedorId(Integer formecedorId, Integer orcamentoId) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("SELECT * from respostaorcamento ro");
		sb.append("INNER JOIN itemorcamento item ON item.id = ro.id_item");
		sb.append("INNER JOIN respostafornecedor rf ON rf.id = ro.id_resposta");
		sb.append("WHERE rf.id_fornecedor = :formecedorId AND ro.id_orcamento = :orcamentoId");
		sb.setParameter("formecedorId", formecedorId);
		sb.setParameter("orcamentoId", orcamentoId);

		return getArrayList(sb, RespostaOrcamento.class);
	}

	public List<RespostaOrcamento> buscarRespostasOrcamentoPorOrcamentoId(Integer orcamentoId) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("SELECT * from respostaorcamento ro");
		sb.append("INNER JOIN itemorcamento item ON item.id = ro.id_item");
		sb.append("INNER JOIN respostafornecedor rf ON rf.id = ro.id_resposta");
		sb.append("WHERE ro.id_orcamento = :orcamentoId");
		sb.append("order by ro.id_item asc");
		sb.setParameter("orcamentoId", orcamentoId);

		return getArrayList(sb, RespostaOrcamento.class);
	}

}
