package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.Fornecedor;
import br.com.wisintainer.model.OrdemDeCompra;

public class OrdemDeCompraDAO extends GenericDAO {
	public OrdemDeCompraDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public OrdemDeCompraDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public Integer retornaIdProx() throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append("SELECT MAX(id) FROM ordemdecompra");

		return getSingle(sb, Integer.class);
	}

	public List<OrdemDeCompra> buscarOrdemDeCompraPorId(Integer id) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM ordemdecompra WHERE id = :id ");
		sb.setParameter("id", id);

		return getArrayList(sb, OrdemDeCompra.class);
	}

	public List<OrdemDeCompra> buscarOrdemDeCompraPorPlaca(String placa) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT oc.id, oc.data_envio, oc.ordemdecompra FROM ordemdecompra oc\r\n"
				+ "INNER JOIN ordemdecompra_aprovacoes oca ON oc.id = oca.ordemdecompra_id\r\n"
				+ "INNER JOIN aprovacao ap ON ap.id = oca.aprovacao_id \r\n" + "INNER JOIN orcamento orc ON orc.id = ap.orcamento_id\r\n"
				+ "WHERE orc.veiculoPlaca = :placa ");
		sb.setParameter("placa", placa);

		return getArrayList(sb, OrdemDeCompra.class);
	}

}
