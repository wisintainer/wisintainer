package br.com.wisintainer.helper;

import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.reflections.Reflections;

import br.com.wisintainer.helper.Logger;
import br.com.wisintainer.helper.Properties;
import br.com.wisintainer.helper.Router;
import br.com.wisintainer.helper.TipoBanco;
import br.com.wisintainer.helper.TipoBanco.TiposBanco;

public class HibernateUtil {
	private static SessionFactory sessionFactoryMySql;
	private static ServiceRegistry serviceRegistryMySql;
	static {
		try {

			File base = new File(Router.getResource(), "configuration/database/");
			Properties properties = Properties.getInstance();

			configurarMysql(properties, base);

		} catch (Exception excecao) {
			Logger.getLogger().error("", excecao);
		}
	}

	private static void configurarMysql(Properties properties, File base) throws Exception {

		Configuration configurationMySql = new Configuration().configure(new File(base, "/mysql/mysql.cfg.xml"));

		configurationMySql.setProperty("hibernate.default_schema",
				properties.getAsString("private.database.mysql-cfg.hibernate-default_schema"));

		configurationMySql.setProperty("hibernate.connection.url",
				properties.getAsString("private.database.mysql-cfg.hibernate-connection-url"));

		configurationMySql.setProperty("hibernate.connection.username",
				properties.getAsString("private.database.mysql-cfg.hibernate-connection-username"));

		configurationMySql.setProperty("hibernate.connection.password",
				properties.getAsString("private.database.mysql-cfg.hibernate-connection-password"));

		configurationMySql.setProperty("hibernate.connection.clientProgramName",
				properties.getAsString("private.database.mysql-cfg.hibernate-connection-clientProgramName"));

		configurationMySql.setProperty("hibernate.c3p0.preferredTestQuery",
				properties.getAsString("private.database.mysql-cfg.hibernate-c3p0-preferredTestQuery"));

		// Mapeamento das entidades
		adicionarEntidadesAnotadas(configurationMySql, TiposBanco.MYSQL);

		serviceRegistryMySql = new StandardServiceRegistryBuilder().applySettings(configurationMySql.getProperties())
				.build();
		sessionFactoryMySql = configurationMySql.buildSessionFactory(serviceRegistryMySql);
	}

	private static void adicionarEntidadesAnotadas(Configuration cfg, TiposBanco tipoBanco) throws Exception {
		Reflections reflections = new Reflections("br.com.wisintainer.model");
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);

		Logger.getLogger().info("*********************************************************");
		Logger.getLogger().info("Adicionado mapeamento para entidades para o banco: " + tipoBanco);
		Logger.getLogger().info("---------------------------------------------------------");
		for (Class<?> clazz : classes) {

			Annotation[] anotacoes = clazz.getAnnotations();
			Boolean encontradoTipo = false;
			for (Annotation a : anotacoes) {

				TipoBanco anotacao = null;
				if (a.annotationType().toString().contains("TipoBanco")) {
					anotacao = (TipoBanco) a;
					encontradoTipo = true;
				}
				if (anotacao != null && anotacao.banco().equals(tipoBanco)) {
					Logger.getLogger().info("Banco: " + anotacao.banco() + " Classe: " + clazz.getName());
					cfg.addAnnotatedClass(clazz);
				}
			}
			// Se possuir anotacao @Entity e não tiver a anotação @TipoBanco
			if (!encontradoTipo) {
				throw new Exception(
						"Classe: " + clazz.getName() + " não possui anotação @TipoBanco. Necessário adicionar.");
			}
		}
		Logger.getLogger().info("*********************************************************");
	}

	public static SessionFactory getSessionMysqlFactory() {
		return sessionFactoryMySql;
	}

}
