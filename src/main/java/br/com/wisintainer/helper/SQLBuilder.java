package br.com.wisintainer.helper;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.hibernate.type.CharacterType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

/**
 * 
 * @author s15603
 *
 */
public class SQLBuilder implements Serializable {

	/**
	 * Serial.
	 */
	private static final long serialVersionUID = 2695979276347180081L;

	/**
	 * 
	 * @author s15603
	 *
	 */
	public enum Mode {
		COMMON,
		SQL,
		HQL
	};

	/**
	 * 
	 */
	private String prefix;

	/**
	 * 
	 */
	private String sufix;

	/**
	 * 
	 */
	private java.lang.StringBuilder sb;

	/**
	 * 
	 */
	private Mode mode;
		
	/**
	 * 
	 */
	private HashMap<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * 
	 * @param mode
	 */
	public SQLBuilder(Mode mode) {
		setSb(new java.lang.StringBuilder());
		setMode(mode);
	}

	/**
	 * Este construtor foi desabilitado para que seja definido um Mode.
	 */
//	public SQLBuilder() {
//		this(Mode.COMMON);
//	}

	/**
	 * 
	 * @return
	 */
	public java.lang.StringBuilder getSb() {
		return sb;
	}

	/**
	 * 
	 * @param sb
	 */
	public void setSb(java.lang.StringBuilder sb) {
		this.sb = sb;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * 
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 
	 * @return
	 */
	public String getSufix() {
		return sufix;
	}

	/**
	 * 
	 * @param sufix
	 */
	public void setSufix(String sufix) {
		this.sufix = sufix;
	}

	/**
	 * 
	 * @return
	 */
	public Mode getMode() {
		return mode;
	}

	/**
	 * 
	 * @param mode
	 */
	public void setMode(Mode mode) {
		this.mode = mode;

		if (mode.equals(Mode.COMMON)) {
			setPrefix("");
			setSufix("");
		} else if (mode.equals(Mode.SQL)) {
			setPrefix("");
			setSufix("	");
		} else if (mode.equals(Mode.HQL)) {
			setPrefix("");
			setSufix("	");
		}
	}

	public HashMap<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * 
	 * @return
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public StringBuilder append(String str) {
		return getSb().append(getPrefix()).append(str).append(getSufix());
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public StringBuilder append(SQLBuilder str) {
		return getSb().append(getPrefix()).append(str).append(getSufix());
	}
	
	/**
	 * 
	 * @return
	 */
	public int length() {
		return getSb().length();
	}
	
	/**
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		getSb().setLength(length);
	}

	/**
	 * 
	 * @param regex
	 * @param replacement
	 */
	public void replaceAll(String regex, String replacement) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(getSb());
		setSb(
			new java.lang.StringBuilder(
				m.replaceAll(replacement)
			)
		);
	}

	/**
	 * 
	 * @param text
	 * @param replacement
	 */
	public void replace(String text, String replacement) {
		setSb(
			new java.lang.StringBuilder(
				getSb().toString().replace(text, replacement)
			)
		);
	}
	
	/**
	 * 
	 * @param query
	 */
	@SuppressWarnings("unchecked")
	public void setParameters(@SuppressWarnings("rawtypes") Query query) {
		if (!getParameters().isEmpty()) {
			
			for (Map.Entry<String, Object> entry : getParameters().entrySet()) {
			    String key = entry.getKey();
			    Object value = entry.getValue();

			    if (value != null && Collection.class.isAssignableFrom(value.getClass())) 
			    {
					query.setParameterList(key, (Collection<?>) value);
				}else if (value != null && Date.class.isAssignableFrom(value.getClass())) 
			    {
					query.setParameter(key, value, TemporalType.TIMESTAMP);
				}else {
					query.setParameter(key,value);
				}			    
			}	
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, Object value) {
		setParameter(name, value, null);
	}
	
	/**
	 * Define um parameter para buscas pelo LIKE do SQL.
	 * Os curingas devem ser informados no parâmetro value.
	 * 
	 * @param name
	 * @param value
	 */
	public void setParameterLike(String name, String value) {
		setParameter(name, "%" + value + "%", StringType.INSTANCE);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @param type
	 */
	public void setParameter(String name, Object value, Type type) {
		if (type != null) {
			setParameterDirectly(name, value, type);
		} else {
			getParameters().put(name, value);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void setParameterDirectly(String name, Object value, Type type) throws IllegalArgumentException {
		if (name == null) {
			throw new IllegalArgumentException("Invalid parameter.");
		} else if (value == null) {
			throw new IllegalArgumentException("Invalid value for parameter with name '" + name + "'.");
		}
		
		if (Collection.class.isAssignableFrom(value.getClass())) {
			if (
				type != null
				&& (
					type.equals(StringType.INSTANCE)
					|| type.equals(CharacterType.INSTANCE)
				)
			) {
				String joined = "";
				Iterator<String> iterator = ((Collection<String>) value).iterator();
				joined += "'" + iterator.next() + "'";
				
				while (iterator.hasNext()) {
					joined += ", '" + iterator.next() + "'";
				}
				
				replace(":" + name, joined);
			} else {
				replace(":" + name, StringUtils.join((Collection<String>) value, ", "));
			}
		} else if (
			type != null
			&& (
				type.equals(StringType.INSTANCE)
				|| type.equals(CharacterType.INSTANCE)
			)
		) {
			replace(":" + name, "'" + value.toString() + "'");
		} else {
			replace(":" + name, value.toString());
		}
	}

	@Override
	public String toString() {
		return getSb().toString();
	}
}
