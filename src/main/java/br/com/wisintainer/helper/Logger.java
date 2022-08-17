package br.com.wisintainer.helper;

public class Logger {
	private static org.apache.logging.log4j.Logger LOGGER;
	
	final public static org.apache.logging.log4j.Logger getLogger() {
		try {
			if (LOGGER == null) {
				LOGGER = org.apache.logging.log4j.LogManager.getLogger("scaweb");
			}
		} catch (Exception excecao) {
			excecao.printStackTrace();
		}
		
		return LOGGER;
	}
}