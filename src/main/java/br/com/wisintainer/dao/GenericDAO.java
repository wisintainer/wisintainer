package br.com.wisintainer.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import br.com.wisintainer.helper.SQLBuilder;
import br.com.wisintainer.helper.SQLBuilder.Mode;

public class GenericDAO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SessionFactory factory;

	private Session session;

	private boolean daoControlaSessao;

	// Se deseja que o hibernate controle a transacao
	public GenericDAO(SessionFactory sessionFactory) {
		setFactory(sessionFactory);
		setDaoControlaSessao(true);
	}

	// Se irá controlar transacao (begin, commit, rollback)
	public GenericDAO(SessionFactory sessionFactory, Session session) {
		setFactory(sessionFactory);
		setSession(session);
		setDaoControlaSessao(false);
	}

	public SessionFactory getFactory() {
		return factory;
	}

	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public boolean isDaoControlaSessao() {
		return daoControlaSessao;
	}

	public void setDaoControlaSessao(boolean daoControlaSessao) {
		this.daoControlaSessao = daoControlaSessao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public <R> ArrayList<R> getArrayList(SQLBuilder sb, Class<R> classeDeRetorno, Integer maxResults) throws Exception {
		return getArrayListExec(sb, classeDeRetorno, maxResults);
	}

	public <R> ArrayList<R> getArrayList(SQLBuilder sb, Class<R> classeDeRetorno) throws Exception {
		return getArrayListExec(sb, classeDeRetorno, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <R> ArrayList<R> getArrayListExec(SQLBuilder sb, Class<R> classeDeRetorno, Integer maxResults)
			throws Exception {

		// Abre conexao se 'daoControlaSessao'
		if (daoControlaSessao) {
			setSession(getFactory().openSession());
		}
		// Cria a query
		Query query = null;
		if (sb.getMode().equals(Mode.SQL)) {
			query = getSession().createSQLQuery(sb.toString());
			if (!classeDeRetorno.getName().contains("java.lang") && !classeDeRetorno.getName().contains("java.math")
					&& !classeDeRetorno.getName().contains("java.util")) {
				((NativeQuery) query).addEntity(classeDeRetorno);
			}
		} else {
			query = getSession().createQuery(sb.toString());
		}
		// Seta parametros
		sb.setParameters(query);

		List<? extends Object> entities = null;
		if (query != null) {

			try {

				if (maxResults != null) {
					query.setMaxResults(maxResults);
				}

				entities = query.list();

			} catch (Exception e) {
				throw e;
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}
			}

		}

		return (ArrayList<R>) entities;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <R> R getSingle(SQLBuilder sb, Class<R> classeDeRetorno, Boolean... ativaMaxResult) throws Exception {

		// Abre conexao se 'daoControlaSessao'
		if (daoControlaSessao) {
			setSession(getFactory().openSession());
		}
		// Cria a query
		Query query = null;
		if (sb.getMode().equals(Mode.SQL)) {
			query = getSession().createSQLQuery(sb.toString());
			if (!classeDeRetorno.getName().contains("java.lang") && !classeDeRetorno.getName().contains("java.math")
					&& !classeDeRetorno.getName().contains("java.util"))
				((NativeQuery) query).addEntity(classeDeRetorno);

		} else {
			query = getSession().createQuery(sb.toString());
		}
		// Seta parametros
		sb.setParameters(query);

		// Setado para evitar que o hibernate sete LIMIT 1 em um group by ou sum,
		// ocorrendo erro de sintaxe
		if (ativaMaxResult == null) {
			query.setMaxResults(1);
		}

		R entity = null;
		if (query != null) {
			try {
				entity = (R) query.uniqueResult();

			} catch (NoResultException nre) {
				return null;
			} catch (Exception e) {
				throw e;
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}
			}
		}
		return classeDeRetorno.cast(entity);
	}

	public Boolean saveOrUpdate(Object entity) throws Exception {
		boolean sucesso = false;

		if (entity != null) {
			// Abre conexao se 'daoControlaSessao'
			if (daoControlaSessao) {
				setSession(getFactory().openSession());
			}

			try {

				if (daoControlaSessao) {
					getSession().getTransaction().begin();
				}
				getSession().saveOrUpdate(entity); // Se possuir id é update, se não é insert
				if (daoControlaSessao) {
					getSession().getTransaction().commit();
				}
				sucesso = true;

			} catch (Exception e) {
				if (daoControlaSessao) {
					getSession().getTransaction().rollback();
				}
				throw new Exception(e);
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}

			}

		} else {
			throw new Exception("Entidade esta nula.");
		}

		return sucesso;
	}

	public Boolean save(Object entity) throws Exception {
		boolean sucesso = false;

		if (entity != null) {
			// Abre conexao se 'daoControlaSessao'
			if (daoControlaSessao) {
				setSession(getFactory().openSession());
			}

			try {

				if (daoControlaSessao) {
					getSession().getTransaction().begin();
				}
				getSession().save(entity);
				if (daoControlaSessao) {
					getSession().getTransaction().commit();
				}
				sucesso = true;

			} catch (Exception e) {
				if (daoControlaSessao) {
					getSession().getTransaction().rollback();
				}
				throw new Exception(e);
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}

			}

		} else {
			throw new Exception("Entidade esta nula.");
		}

		return sucesso;
	}

	public Boolean update(Object entity) throws Exception {
		boolean sucesso = false;

		if (entity != null) {
			// Abre conexao se 'daoControlaSessao'
			if (daoControlaSessao) {
				setSession(getFactory().openSession());
			}

			try {

				if (daoControlaSessao) {
					getSession().getTransaction().begin();
				}
				getSession().update(entity);
				if (daoControlaSessao) {
					getSession().getTransaction().commit();
				}
				sucesso = true;

			} catch (Exception e) {
				if (daoControlaSessao) {
					getSession().getTransaction().rollback();
				}
				throw new Exception(e);
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}

			}

		} else {
			throw new Exception("Entidade esta nula.");
		}

		return sucesso;
	}

	public Boolean delete(Object entity) throws Exception {
		boolean sucesso = false;

		if (entity != null) {

			// Abre conexao se 'daoControlaSessao'
			if (daoControlaSessao) {
				setSession(getFactory().openSession());
			}

			try {

				if (daoControlaSessao) {
					getSession().getTransaction().begin();
				}

				getSession().delete(entity);

				if (daoControlaSessao) {
					getSession().getTransaction().commit();
				}
				sucesso = true;

			} catch (Exception e) {
				if (daoControlaSessao) {
					getSession().getTransaction().rollback();
				}
				throw e;
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}

			}
		}
		return sucesso;
	}

	@SuppressWarnings("rawtypes")
	protected Integer execute(SQLBuilder sb) throws Exception {
		int qtdLinhas = 0;

		// Abre conexao se 'daoControlaSessao'
		if (daoControlaSessao) {
			setSession(getFactory().openSession());
		}
		// Cria a query
		Query query = null;
		if (sb.getMode().equals(Mode.SQL)) {
			query = getSession().createSQLQuery(sb.toString());
		} else {
			query = getSession().createQuery(sb.toString());
		}
		// Seta parametros
		sb.setParameters(query);

		if (query != null) {
			try {

				if (daoControlaSessao) {
					getSession().getTransaction().begin();
				}

				qtdLinhas = query.executeUpdate();

				if (daoControlaSessao) {
					getSession().getTransaction().commit();
				}

			} catch (Exception e) {
				if (daoControlaSessao) {
					getSession().getTransaction().rollback();
				}
				throw e;
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}

			}
		}

		return qtdLinhas;
	}

	public Object saveReturningSaved(Object entity) throws Exception {
		boolean sucesso = false;
		Object retorno = new Object();

		if (entity != null) {
			// Abre conexao se 'daoControlaSessao'
			if (daoControlaSessao) {
				setSession(getFactory().openSession());
			}

			try {

				if (daoControlaSessao) {
					getSession().getTransaction().begin();
				}
				retorno = getSession().save(entity);
				if (daoControlaSessao) {
					getSession().getTransaction().commit();
				}
				sucesso = true;

			} catch (Exception e) {
				if (daoControlaSessao) {
					getSession().getTransaction().rollback();
				}
				throw new Exception(e);
			} finally {
				if (daoControlaSessao) {
					getSession().close();
				}

			}

		} else {
			throw new Exception("Entidade esta nula.");
		}

		return retorno;
	}
}
