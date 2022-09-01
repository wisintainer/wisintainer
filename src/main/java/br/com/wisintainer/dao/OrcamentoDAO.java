package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.ItemOrcamento;
import br.com.wisintainer.model.Orcamento;

public class OrcamentoDAO extends GenericDAO {
	public OrcamentoDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public OrcamentoDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public List<Orcamento> buscarOrcamentoPorPlacaDoVeiculo(String placaVeiculo) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);

		sb.append(
				"SELECT * FROM orcamento orc WHERE orc.veiculoPlaca= :placa");

		sb.setParameter("placa", placaVeiculo);

		return getArrayList(sb, Orcamento.class);
	}

	public Orcamento buscarOrcamentoPorId(Integer id) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);

		sb.append(
				" SELECT * FROM orcamento orc WHERE orc.id= :id ");

		sb.setParameter("id", id);

		return getSingle(sb, Orcamento.class);
	}
}
