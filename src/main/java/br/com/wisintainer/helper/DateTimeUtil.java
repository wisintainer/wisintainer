package br.com.wisintainer.helper;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class DateTimeUtil {
	

	/**
	 * Verifica se a data informada é um feriado ou não.
	 *
	 * @param calendario
	 *            Data a ser verificada.
	 * @return Verdadeiro se for um feriado, falso caso contrário.
	 *       algum JSON ou XML.
	 */
	public static Boolean isFeriado(Calendar calendario) {
		return false;
	}

	public static Integer getAnoCorrente() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static String getAno(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		return sdf.format(dt);
	}

	public static String getMes(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM");
		return sdf.format(dt);
	}

	public static String getMesAno(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		return sdf.format(dt);
	}

	public static Integer getUltimoMesFechado(Integer anoSelecionado) {
		if (anoSelecionado < Calendar.getInstance().get(Calendar.YEAR)) {
			return 12;
		} else if (anoSelecionado > Calendar.getInstance().get(Calendar.YEAR)) {
			return 1;
		} else {
			return Calendar.getInstance().get(Calendar.MONTH) + 1;
		}
	}

	public static String getDataFormato(Date dt, String formato) {
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		return sdf.format(dt);
	}
	
	public static String getDataFormato(LocalDate dt, String formato, Locale locale) {		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy", locale);
		return dt.format(formatter);
		
	}
	
	/**
	 * Retorna dia/mes/ano de um Date
	 *
	 * @param Date
	 * @return dd/MM/yyyy
	 */
	public static String getDiaMesAnoStr(Date dt, String separador) {
		String retDataAquisicao = "";

		if (separador == null || separador.isEmpty()) {
			separador = "/";
		}

		if (dt != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dt);
			Integer dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
			Integer mes = gc.get(GregorianCalendar.MONTH) + 1;
			Integer ano = gc.get(GregorianCalendar.YEAR);

			if (dia < 10) {
				retDataAquisicao += "0" + dia.toString();
			} else {
				retDataAquisicao += dia.toString();
			}
			retDataAquisicao += separador;
			if (mes < 10) {
				retDataAquisicao += "0" + mes.toString();
			} else {
				retDataAquisicao += mes.toString();
			}
			retDataAquisicao += separador;
			retDataAquisicao += ano.toString();
		}
		return retDataAquisicao;
	}

	/**
	 * Retorna dia/mes/ano HH:MM:SS de um Date
	 *
	 * @param Date
	 * @return dd/mm/yyyy HH:MM:SS
	 */
	public static String getDiaMesAnoStrHoraMinutoSegundo(Date dt,
			String separador) {
		String retDataAquisicao = "";

		if (separador == null || separador.isEmpty()) {
			separador = "/";
		}

		if (dt != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dt);
			Integer dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
			Integer mes = gc.get(GregorianCalendar.MONTH) + 1;
			Integer ano = gc.get(GregorianCalendar.YEAR);
			Integer hora = gc.get(GregorianCalendar.HOUR_OF_DAY);
			Integer minuto = gc.get(GregorianCalendar.MINUTE);
			Integer segundo = gc.get(GregorianCalendar.SECOND);

			if (dia < 10) {
				retDataAquisicao += "0" + dia.toString();
			} else {
				retDataAquisicao += dia.toString();
			}
			retDataAquisicao += separador;
			if (mes < 10) {
				retDataAquisicao += "0" + mes.toString();
			} else {
				retDataAquisicao += mes.toString();
			}
			retDataAquisicao += separador;
			retDataAquisicao += ano.toString();
			retDataAquisicao += " ";
			if (hora < 10) {
				retDataAquisicao += "0" + hora.toString();
			} else {
				retDataAquisicao += hora.toString();
			}
			retDataAquisicao += ":";
			if (minuto < 10) {
				retDataAquisicao += "0" + minuto.toString();
			} else {
				retDataAquisicao += minuto.toString();
			}
			retDataAquisicao += ":";
			if (segundo < 10) {
				retDataAquisicao += "0" + segundo.toString();
			} else {
				retDataAquisicao += segundo.toString();
			}
		}
		return retDataAquisicao;
	}

	/**
	 * Retorna dia/mes/ano HH:MM:SS de um Date
	 *
	 * @param Date
	 * @return dd/mm/yyyy HH:MM:SS
	 * @throws ParseException
	 */
	public static Time getHoraMinuto(Date dt) throws ParseException {
		String retDataAquisicao = "";

		if (dt != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dt);
			Integer hora = gc.get(GregorianCalendar.HOUR_OF_DAY);
			Integer minuto = gc.get(GregorianCalendar.MINUTE);
			Integer segundo = gc.get(GregorianCalendar.SECOND);

			if (hora < 10) {
				retDataAquisicao += "0" + hora.toString();
			} else {
				retDataAquisicao += hora.toString();
			}
			retDataAquisicao += ":";
			if (minuto < 10) {
				retDataAquisicao += "0" + minuto.toString();
			} else {
				retDataAquisicao += minuto.toString();
			}
			retDataAquisicao += ":";
			if (segundo < 10) {
				retDataAquisicao += "0" + segundo.toString();
			} else {
				retDataAquisicao += segundo.toString();
			}
		}

		DateFormat formato = new SimpleDateFormat("HH:mm:ss");
		Time tempo = new java.sql.Time(formato.parse(retDataAquisicao)
				.getTime());

		return tempo;
	}

	/**
	 * Retorna ano/mes/dia de um Date
	 *
	 * @param Date
	 * @return yyyy/MM/dd
	 */
	public static String getDiaMesAnoStrFormatoYYYMMDD(Date dt, String separador) {
		String retDataAquisicao = "";

		if (separador == null || separador.isEmpty()) {
			separador = "/";
		}

		if (dt != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dt);
			Integer dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
			Integer mes = gc.get(GregorianCalendar.MONTH) + 1;
			Integer ano = gc.get(GregorianCalendar.YEAR);

			retDataAquisicao += ano.toString();
			retDataAquisicao += separador;
			if (mes < 10) {
				retDataAquisicao += "0" + mes.toString();
			} else {
				retDataAquisicao += mes.toString();
			}
			retDataAquisicao += separador;
			if (dia < 10) {
				retDataAquisicao += "0" + dia.toString();
			} else {
				retDataAquisicao += dia.toString();
			}

		}
		return retDataAquisicao;
	}

	/**
	 * Retorna ano/mes/dia hora:minuto:segundo de um Date
	 *
	 * @param Date
	 * @return yyyy/MM/dd hh:mm:ss
	 */
	public static String getFomatoTimeStampDb2Str(Date dt) {
		String dataStr = "";
		String tempoStr = "";
		String retorno = null;

		String separador = "-";

		if (dt != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dt);
			Integer dia = gc.get(GregorianCalendar.DAY_OF_MONTH);
			Integer mes = gc.get(GregorianCalendar.MONTH) + 1;
			Integer ano = gc.get(GregorianCalendar.YEAR);

			Integer hora = gc.get(GregorianCalendar.HOUR_OF_DAY);
			Integer minuto = gc.get(GregorianCalendar.MINUTE);
			Integer segundo = gc.get(GregorianCalendar.SECOND);

			dataStr += ano.toString();
			dataStr += separador;
			if (mes < 10) {
				dataStr += "0" + mes.toString();
			} else {
				dataStr += mes.toString();
			}
			dataStr += separador;
			if (dia < 10) {
				dataStr += "0" + dia.toString();
			} else {
				dataStr += dia.toString();
			}

			if (hora < 10) {
				tempoStr += "0" + hora.toString();
			} else {
				tempoStr += hora.toString();
			}
			tempoStr += ":";
			if (minuto < 10) {
				tempoStr += "0" + minuto.toString();
			} else {
				tempoStr += minuto.toString();
			}
			tempoStr += ":";
			if (segundo < 10) {
				tempoStr += "0" + segundo.toString();
			} else {
				tempoStr += segundo.toString();
			}

		}

		retorno = "TIMESTAMP(CAST('" + dataStr + "' AS VARCHAR(10)),'"
				+ tempoStr + "')";

		return retorno;
	}

	public static Date dataSemTempo(Date dataComTempo) {
		if (dataComTempo != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataComTempo);
			gc.set(GregorianCalendar.YEAR, gc.get(GregorianCalendar.YEAR));
			gc.set(GregorianCalendar.MONTH, gc.get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.DATE, gc.get(GregorianCalendar.DATE));
			gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
			gc.set(GregorianCalendar.MINUTE, 0);
			gc.set(GregorianCalendar.SECOND, 0);
			gc.set(GregorianCalendar.MILLISECOND, 0);

			return gc.getTime();
		} else {
			return null;
		}
	}

	public static Date dataSemTempoMeioDia(Date dataComTempo) {
		if (dataComTempo != null) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(dataComTempo);
			gc.set(GregorianCalendar.YEAR, gc.get(GregorianCalendar.YEAR));
			gc.set(GregorianCalendar.MONTH, gc.get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.DATE, gc.get(GregorianCalendar.DATE));
			gc.set(GregorianCalendar.HOUR_OF_DAY, 12);
			gc.set(GregorianCalendar.MINUTE, 0);
			gc.set(GregorianCalendar.SECOND, 0);
			gc.set(GregorianCalendar.MILLISECOND, 0);

			return gc.getTime();
		} else {
			return null;
		}
	}

	public static int calculaIntervaloDias(Date dtFinal, Date dtInicial) {

		int result = (int) ((dtFinal.getTime() - dtInicial.getTime()) / 86400000L);

		return result < 0 ? result * -1 : result;

	}

	public static Long getIdade(Date date) throws ParseException {

		return (new Date().getTime() - date.getTime()) / (31536000000L);
	}

	public static long getIdadeCompleta(Date nascimento) {
		LocalDate nascimentoLocalDate = convertToLocalDateViaInstant(nascimento);
		LocalDate hoje = LocalDate.now();
		long anosCompletos = ChronoUnit.YEARS.between(nascimentoLocalDate, hoje);		
		return anosCompletos;
	}
	
	public static Date dataPrimeiroDiaMes(Integer mes, Integer ano) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.YEAR, ano);
		gc.set(GregorianCalendar.MONTH, (mes - 1)); // Pois Janeiro e 0;
		gc.set(GregorianCalendar.DATE, 1);
		gc.set(GregorianCalendar.HOUR_OF_DAY, 0);
		gc.set(GregorianCalendar.MINUTE, 0);
		gc.set(GregorianCalendar.SECOND, 0);
		gc.set(GregorianCalendar.MILLISECOND, 0);

		return gc.getTime();
	}

	public static Date dataUltimoDiaMes(Integer mes, Integer ano) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.YEAR, ano);
		gc.set(GregorianCalendar.MONTH, (mes - 1)); // Pois Janeiro e 0;
		gc.set(GregorianCalendar.DATE, 1);
		gc.set(GregorianCalendar.HOUR_OF_DAY, 23);
		gc.set(GregorianCalendar.MINUTE, 59);
		gc.set(GregorianCalendar.SECOND, 59);
		gc.set(GregorianCalendar.MILLISECOND, 0);

		gc.set(GregorianCalendar.DATE,
				gc.getActualMaximum(Calendar.DAY_OF_MONTH));

		return gc.getTime();
	}

	/**
	 * Retorna Data de String formato dd/MM/yyyy
	 *
	 * @param data
	 * @return
	 * @throws ParseException
	 */
	public static Date getData(String data) throws ParseException {
		String dataConverter = data;

		if (data != null && !data.isEmpty()) {
			if (!data.contains("/")) {
				dataConverter = dataConverter.substring(0, 2) + "/"
						+ dataConverter.substring(2, 4) + "/"
						+ dataConverter.substring(4, 8);
			}

			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(dataConverter);
			return date;
		} else {
			return null;
		}
	}
	
	/**
	 * Formato YYYY-MM-dd HH:mm:ss SSS.
	 * 
	 * @param stringData
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateTime(String stringData) throws ParseException {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		sdf.setTimeZone(TimeZone.getDefault());
		Date date = sdf.parse(stringData);
		
		return date;
	}

	public static Integer getMinutes(Date data) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(data);

		return c.get(Calendar.MINUTE) + c.get(Calendar.HOUR_OF_DAY) * 60 + (c.get(Calendar.DAY_OF_MONTH) -1) * 60 * 24;
	}

	public static Date convertMillisToDate(Long milisegundos) {
		return new Date(milisegundos - TimeZone.getDefault().getRawOffset());
	}

	public static Date convertMinutesToDate(Integer minutos) {
		return convertMillisToDate(TimeUnit.MINUTES.toMillis(minutos));
	}

	public static Calendar getStartCalendar() {
		Calendar retorno = Calendar.getInstance(TimeZone.getDefault());
		retorno.setTimeInMillis(-TimeZone.getDefault().getRawOffset());

		return retorno;
	}

	public static Date getStartDate() {
		return getStartCalendar().getTime();
	}

	public int compare(Object d1, Object d2) {
		return 0;
	}

	public int compare(Date d1, Date d2) {
		return d1.compareTo(d2);
	}
	
	public static String getAsLocalizedString(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		return sdf.format(data);
	}
	
	public static Date getDateMeioDia(Date data)
	{
		if(data != null)
		{
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(data);
			gc.set(GregorianCalendar.YEAR, gc.get(GregorianCalendar.YEAR));
			gc.set(GregorianCalendar.MONTH, gc.get(GregorianCalendar.MONTH));
			gc.set(GregorianCalendar.DATE, gc.get(GregorianCalendar.DATE));
			gc.set(GregorianCalendar.HOUR_OF_DAY, 12);
			gc.set(GregorianCalendar.MINUTE, 0);
			gc.set(GregorianCalendar.SECOND, 0);
			gc.set(GregorianCalendar.MILLISECOND, 0);

			return gc.getTime();
		}else
		{
			return null;
		}		
	}
	
	/**
	 * Adiciona dias a uma data e retorna uma data util. Pode considerar ou não feriados e pode considerar feriados por unidade.
	 * @param data - Data para verficação 
	 * @param numeroDias - Numero de dias para interação
	 * @param lstFeriadoUnidade - Id da unidade para validar o feriado. Se passar nulo, pega apenas os feriados nacionais. Ex: '999','009' (Feriados nacionais e de curitiba)
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws Exception
	 */

	
	/**
	 * Agrega Dias a uma data, pode se adicionar Integer negativo, sendo reduzido dias
	 * @param data
	 * @param dias
	 * @return
	 */
	public static Date agregarDias(Date data, Integer dias)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(data);
		gc.add(GregorianCalendar.DATE, dias);
		
		return gc.getTime();
	}

	/**
	 * Agrega Dias a uma data, pode se adicionar Integer negativo, sendo reduzido dias
	 * @param data
	 * @param dias
	 * @return
	 */
	public static Date agregarMeses(Date data, Integer meses)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(data);
		gc.add(GregorianCalendar.MONTH, meses);
		
		return gc.getTime();
	}
	
	/**
	 * Agrega Minutos a uma data, pode se adicionar Integer negativo, sendo reduzido minutos
	 * @param data
	 * @param minutos
	 * @return
	 */
	public static Date agregarMinutos(Date data, Integer minutos)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(data);
		gc.add(GregorianCalendar.MINUTE, minutos);
		
		return gc.getTime();
	}
	
	public static Date getData(LocalDate data)
	{
		Date retorno = Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
		return retorno;
	}
	
	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	public static LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDateTime();
	}
	
	public static LocalTime convertToLocalTimeViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalTime();
	}
	
	public static String getDataFormato(LocalTime localtime, String formato) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formato);
		return localtime.format(dtf);
	}
	
	
	
	public static boolean isMesPassado(Date dataVencimento) throws Exception {
		if (dataVencimento != null) {
			GregorianCalendar gcVencimento = new GregorianCalendar();
			gcVencimento.setTime(dataVencimento);
			gcVencimento.set(GregorianCalendar.DATE, 1);

			GregorianCalendar gcDtAtual = new GregorianCalendar();
			gcDtAtual.setTime(new Date());
			gcDtAtual.set(GregorianCalendar.DATE, 1);

			if (DateTimeUtil.dataSemTempoMeioDia(gcVencimento.getTime())
					.compareTo(DateTimeUtil.dataSemTempoMeioDia(gcDtAtual.getTime())) < 0) {
				return true;
			} else {
				return false;
			}
		}
		throw new Exception("Falha ao verificar data vencimento");
	}
	
	public static boolean isDataPassada(Date dataVencimento) throws Exception {
		if (dataVencimento != null) {
			GregorianCalendar gcVencimento = new GregorianCalendar();
			gcVencimento.setTime(dataVencimento);

			GregorianCalendar gcDtAtual = new GregorianCalendar();
			gcDtAtual.setTime(new Date());

			if (DateTimeUtil.dataSemTempoMeioDia(gcVencimento.getTime())
					.compareTo(DateTimeUtil.dataSemTempoMeioDia(gcDtAtual.getTime())) < 0) {
				return true;
			} else {
				return false;
			}
		}
		throw new Exception("Falha ao verificar data vencimento");
	}
	
	
}