package br.com.wisintainer.dao;

import java.util.List;

import org.hibernate.Session;

import br.com.wisintainer.helper.HibernateUtil;
import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;
import br.com.wisintainer.model.Aprovacao;
import br.com.wisintainer.model.Oficina;

public class OficinaDAO extends GenericDAO {
	public OficinaDAO() {
		super(HibernateUtil.getSessionMysqlFactory());
	}

	public OficinaDAO(Session session) {
		super(HibernateUtil.getSessionMysqlFactory(), session);
	}

	public List<Oficina> buscarOficinaPorNome(String nome) throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" SELECT * FROM oficina WHERE nome like :nome ");
		sb.setParameterLike("nome", nome);

		return getArrayList(sb, Oficina.class);
	}
	
	public List<Oficina> buscar20Oficinas() throws Exception {
		SQLBuilder sb = new SQLBuilder(Mode.SQL);
		sb.append(" select * from oficina order by id asc limit 20  ");

		return getArrayList(sb, Oficina.class);
	}

}
