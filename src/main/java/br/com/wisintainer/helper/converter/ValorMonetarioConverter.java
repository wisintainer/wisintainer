package br.com.wisintainer.helper.converter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "valorMonetarioConverter")
public class ValorMonetarioConverter extends javax.faces.convert.BigDecimalConverter {
	@Override
	public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
			throws ConverterException {

		if (value == null) {
			return null;
		}

		if (value.trim().length() == 0) {
			return null;
		}

		try {

			String valor;
			if (value.contains(" ")) {
				valor = value.substring(value.indexOf(" "));
			} else {
				valor = value;
			}

			valor = valor.replaceAll("\\.", "").replaceAll(",", ".");

			return new java.math.BigDecimal(valor);

		} catch (final Exception e) {

			final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Conversão",
					"Falha na conversão do número " + value.trim());

			throw new ConverterException(msg, e);

		}
	}

	@Override
	public String getAsString(final FacesContext context, final UIComponent component, final Object value)
			throws ConverterException {

		if (value == null) {
			return "";
		} else if (value instanceof String) {
			return (String) value;
		} else {

			final DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

			return df.format(value);
		}
	}
}
