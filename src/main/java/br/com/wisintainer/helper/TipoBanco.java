package br.com.wisintainer.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TipoBanco {
	
	public enum TiposBanco {
		   MYSQL, SQLSERVER, DB2, POSTGRESQL
		}
	
	TiposBanco banco() default TiposBanco.MYSQL;

}
