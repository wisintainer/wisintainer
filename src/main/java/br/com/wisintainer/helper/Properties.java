package br.com.wisintainer.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.PropertyConfigurator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import br.com.wisintainer.helper.StringUtil;
import br.com.wisintainer.helper.Reader;

public class Properties extends TreeMap<String, Object> {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 710649834820224213L;

	private String separator = ".";

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	private Properties() {
		try {
			loadMain();
			load();
		} catch (Exception excecao) {
			Logger.getLogger().error(excecao.getMessage(), excecao);
		}
	}

	private static class Singleton {
		private static final Properties INSTANCE = new Properties();
	}

	public static Properties getInstance() {
		return Singleton.INSTANCE;
	}

	public Object get(String chave, Object padrao) {
		if (get(chave) != null) {
			return get(chave);
		} else {
			return padrao;
		}
	}

	private void loadMain() throws UnsupportedEncodingException, IOException {
		String json = Reader.getContent(
				new File(
						Router.getResource(),
						"configuration/geral.json"
						)
				);

		this.fromJson(json);
	}

	private void load() throws Exception {
		String json = null;

		String ambiente = getAsString("ambiente");
		String identificador = getAsString("identificador");
		if (
				StringUtils.isEmpty(ambiente)
				|| !ambiente.matches("^dev|hmg|prd$")
				) {
			throw new Exception("Ambiente desconhecido.");
		} else if (
				StringUtils.isEmpty(identificador)
				|| !identificador.matches("^wisintainer.*$")
				) {
			throw new Exception("Contexto desconhecido.");
		} else {
			if (ambiente.equals("dev")) {
				put("isDev", true);
				put("isHmg", false);
				put("isPrd", false);
			} else if (ambiente.equals("hmg")) {
				put("isDev", false);
				put("isHmg", true);
				put("isPrd", false);
			} else if (ambiente.equals("prd")) {
				put("isDev", false);
				put("isHmg", false);
				put("isPrd", true);
			}

			json = Reader.getContent(
					new File(
							Router.getResource(),
							"configuration/" + ambiente + "/" + identificador + ".json"
							)
					);

			this.fromJson(json);

			if (get("sistema.identificador.singular") == null) {
				throw new Exception("Sistema desconhecido.");
			}

			java.util.Properties p = new java.util.Properties();
			p.load(new FileInputStream(new File(
					Router.getResource(),
					"configuration/" + ambiente + "/log4j." + identificador + ".properties"
					).getAbsolutePath()));
			PropertyConfigurator.configure(p);

		}
	}

	public void reload() {
		try {
			if (
					Properties.getInstance().getAsBoolean("isDev")
					|| Properties.getInstance().getAsBoolean("isHmg")
					) {
				this.load();
			}
		} catch (Exception excecao) {
			Logger.getLogger().error(excecao.getMessage(), excecao);
		}
	}

	public Object get(String key) {
		return (Object) super.get(key);
	}

	public <R> R get(String key, Class<R> classeDeRetorno) {
		return (R) Properties.getInstance().get(key, classeDeRetorno);
	}

	public String getAsString(String key) {
		return (String) get(key);
	}

	public Boolean getAsBoolean(String key) {
		return (Boolean) get(key);
	}

	public Number getAsNumber(String key) {
		return (Number) get(key);
	}

	public Short getAsShort(String key) {
		return (Short) get(key);
	}

	public Integer getAsInteger(String key) {
		return Integer.parseInt(get(key).toString());
	}

	public Double getAsDouble(String key) {
		return (Double) get(key);
	}

	public BigDecimal getAsBigDecimal(String key) {
		return (BigDecimal) get(key);
	}

	public String getAsLowerString(String key) {
		return ((String) get(key)).toLowerCase();
	}

	public String getAsProperString(String key) {
		return StringUtil.properCase(((String) get(key)));
	}

	public String getAsUpperString(String key) {
		return ((String) get(key)).toUpperCase();
	}

	public String toJson(String key) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

		return mapper.writeValueAsString(get(key));
	}

	@SuppressWarnings("unchecked")
	public TreeMap<String, Object> putLinkedTreeMap(String key, LinkedTreeMap<String, Object> ltm) {
		if (MapUtils.isNotEmpty(ltm)) {
			for (Map.Entry<String, Object> entry : ltm.entrySet()) {
				String newKey;
				if (StringUtils.isEmpty(key)) {
					newKey = entry.getKey();
				} else {
					newKey = key + getSeparator() + entry.getKey();
				}

				if (LinkedTreeMap.class.isAssignableFrom(entry.getValue().getClass())) {
					putLinkedTreeMap(
							newKey,
							(LinkedTreeMap<String, Object>) entry.getValue()
							);
				} else {
					put(
							newKey,
							entry.getValue()
							);
				}
			}

			return this;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public TreeMap<String, Object> fromJson(String json) {
		LinkedTreeMap<String, Object> ltm = new Gson().fromJson(json, LinkedTreeMap.class);
		return putLinkedTreeMap(null, ltm);
	}
}