package br.com.wisintainer.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import java.util.ArrayList;

public class StringUtil {

	public static int compareTrimmedWithoutSpecialChars(String string1, String string2) {
		if (string1 != null && string2 != null) {
			return compare(
				removerCaracteresEspeciais(string1.trim()),
				removerCaracteresEspeciais(string2.trim())
			);
		} else {
			return compare(string1, string2);
		}
	}

	public static int compareTrimmed(String string1, String string2) {
		if (string1 != null && string2 != null) {
			return compare(string1.trim(), string2.trim());
		} else {
			return compare(string1, string2);
		}
	}

	public static int compare(String string1, String string2) {
		if (
			string1 == null
			&& string2 == null
		) {
			return 0;
		} else if (
			string1 == null
			&& string2 != null
		) {
			return -1;
		} else if (
			string1 != null
			&& string2 == null
		) {
			return 1;
		} else {
			return string1.compareTo(string2);
		}
	}

	/**
	 * Obtém uma parte de uma string do inicio-ésimo caractere até o fim-ésimo.
	 *
	 * Ex.: subString("123456789", 3, 6) => "3456".
	 *
	 * @param alvo
	 *            String alvo a ter retirada uma substring.
	 * @param inicio
	 *            Posição inicial da substring, começa com 1.
	 * @param fim
	 *            Posição final da substring, posição do último caractere a ser
	 *            considerado.
	 * @return Substring da string alvo.
	 */
	public static String subString(String alvo, Integer inicio, Integer fim) {
		if (inicio <= alvo.length() && fim <= alvo.length()) {
			return alvo.substring(inicio - 1, fim);
		} else if (inicio <= alvo.length()) {
			return alvo.substring(inicio - 1, alvo.length());
		} else if (fim <= alvo.length()) {
			return "";
		} else {
			return "";
		}
	}
	/**
	 * Transforma um número inteiro em string e preenche os espaços a esquerda,
	 * de um dado tamanho informado, com zeros.
	 *
	 * Ex.: zeroPad(123, 6) => "000123".
	 *
	 * @param alvo
	 *            Inteiro alvo a ser transformado em string e ter zeros
	 *            acrescentados à esquerda.
	 * @param tamanho
	 *            Tamanho final da string de saída.
	 * @return String feita a partir do número inteiro informado preenchida com
	 *         zeros à esquerda quando possível.
	 */
	public static String zeroPad(Integer alvo, Integer tamanho) {
		return StringUtils.leftPad(alvo.toString(), tamanho, '0');
	}

	/**
	 * Preenche uma string com espaços a esquerda de uma string com zeros.
	 *
	 * Ex.: zeroPad("123", 6) => "000123".
	 *
	 * @param alvo
	 *            String alvo a ter zeros acrescentados à esquerda.
	 * @param tamanho
	 *            Tamanho final da string de saída.
	 * @return String preenchida com zeros à esquerda quando possível.
	 */
	public static String zeroPad(String alvo, Integer tamanho) {
		return StringUtils.leftPad(alvo, tamanho, '0');
	}

	/**
	 * Preenche uma string com espaços a sua direita a partir de um tamanho
	 * informado.
	 *
	 * Ex.: rightSpacePad("Abc", 6) => "Abc   ".
	 *
	 * @param alvo
	 * @param tamanho
	 * @return
	 */
	public static String rightSpacePad(String alvo, Integer tamanho) {
		return StringUtils.rightPad(alvo, tamanho, ' ');
	}

	public static String leftSpacePad(String alvo, Integer tamanho) {
		return StringUtils.leftPad(alvo, tamanho, ' ');
	}

	public static String zeroPadAndCut(String alvo, Integer tamanho) {
		return subString(zeroPad(alvo, tamanho), 1, tamanho);
	}

	public static String zeroPadAndCut(Integer alvo, Integer tamanho) {
		return subString(zeroPad(alvo, tamanho), 1, tamanho);
	}

	public static String rightSpacePadAndCut(String alvo, Integer tamanho) {
		return subString(rightSpacePad(alvo, tamanho), 1, tamanho);
	}

	public static String leftSpacePadAndCut(String alvo, Integer tamanho) {
		return subString(leftSpacePad(alvo, tamanho), 1, tamanho);
	}

	public static String removerAcentosDB2(String texto){
		String[] substituir = {"Á","É","Í","Ó","Ú","Ü","Ã","Õ","Ç","Â","Ê","Ô","À","á","é","í","ó","ú","ü","ã","õ","ç","â","ê","ô","à"};
		String[] substituto = {"A","E","I","O","U","U","A","O","C","A","E","O","A","a","e","i","o","u","u","a","o","c","a","e","o","a"};
		String retorno = texto;
		for(int i = 0; i < substituir.length; i++){
			retorno = " REPLACE("+ retorno + ", '" + substituir[i] + "','" + substituto[i] + "') ";
		}
		return retorno;
	}
	
	public static String removerAcentos(final String texto) {
		String retorno = Normalizer.normalize(texto, Normalizer.Form.NFD);
		retorno = retorno.replaceAll("[^\\p{ASCII}]", "");

		return retorno;
	}

	public static String removerCaracteresEspeciais(String texto) {

		if (texto != null && !texto.isEmpty()) {
			texto = texto.replaceAll("[^a-zA-Z0-9 ]", "");
		}

		return texto;
	}

	public static String removeCaracteresEspeciaisEEspaco(String texto) {

		if (texto != null && !texto.isEmpty()) {
			texto = texto.replaceAll("[^a-zA-Z0-9]", "");
		}

		return texto.replace(" ", "");
	}

	public static String properCase(final String msg) {
		String ret = "";
		final String[] palavras = msg.trim().toLowerCase().split(" ");
		String palavra = "";
		String letra = "";

		for (final String palavra2 : palavras) {

			palavra = palavra2;

			if (palavra.length() > 2) {
				letra = palavra.substring(0, 1).toUpperCase();
				palavra = letra + palavra.substring(1);
			}

			ret += palavra + " ";
		}

		return ret.trim();
	}

	/**
	 * CEP - resultado: 81580-200 format("#####-###", "81580200"); CPF -
	 * resultado 012.345.699-01 format("###.###.###-##", "01234569905"); CNPJ -
	 * resultado: 01.234.569/9052-34 format("##.###.###/####-##",
	 * "01234569905234");
	 */
	public static String format(String pattern, Object value) {
		MaskFormatter mask;
		try {
			mask = new MaskFormatter(pattern);
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(value);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static String apenasNumeros(String texto) {

		if (texto != null && !texto.isEmpty()) {
			texto = texto.replaceAll("[^0-9 ]", "");
		}

		return texto.replace(" ", "");
	}

	public static Boolean isEmpty(String texto) {
		if (texto == null || texto.trim().isEmpty()) {
			return true;
		}

		return false;
	}

	public static Boolean hasTrueMinimum(String texto, Integer minimo) {
		if (isEmpty(texto)) {
			return false;
		} else if (texto.trim().length() < minimo) {
			return false;
		}

		return true;
	}

	public static Boolean isValidLenght(String texto, Integer minimo, Integer maximo) {
		if (isEmpty(texto)) {
			return false;
		} else if (
			texto.length() < minimo
			|| texto.length() > maximo
		) {
			return false;
		}

		return true;
	}

	public static String primeiraPalavra(String texto) {
		try {
			return texto.substring(0, texto.indexOf(" "));
		} catch (Exception excecao) {
			return texto;
		}
	}

	public static String coalesce(String texto) {
		return coalesce(texto, "sem informação");
	}

	public static String coalesce(String texto, String padrao) {
		if (texto == null) {
			return padrao;
		} else {
			return texto;
		}
	}

	public static Boolean equalsIgnoreCase(String first, String second) {
		if (first != null && second != null) {
			return equals(first.toUpperCase(), second.toUpperCase(), true);
		} else {
			if (first == null && second == null) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static Boolean equals(String first, String second, Boolean ignoreCase) {
		if (
			first == null
			|| second == null
		) {
			if (first == null && second == null) {
				return true;
			} else {
				return false;
			}
		} else {
			if (ignoreCase) {
				if (first.equalsIgnoreCase(second)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (first.equals(second)) {
					return true;
				} else {
					return false;
				}
			}
		}
	}

	/**
	 * Truca texto até o limite estabelecido adicionando a string '...' ao final. O metodo ja considera qtdMaxima -3 para adicionar os 3 pontos.
	 * @param qtdMaxima
	 * @param texto
	 * @return
	 */
	public static String truncarTexto(String texto,Integer qtdMaxima)
	{
		if(texto == null)
		{
			return "...";
		}
		
		if(texto.length() > qtdMaxima)
		{
			return subString(texto, 1, qtdMaxima-3) + "...";
		}else
		{
			return texto;
		}
	}
	
	/**
	 * Truca texto até o limite estabelecido adicionando a string '...' ao final. O metodo ja considera qtdMaxima -3 para adicionar os 3 pontos.
	 * @param qtdMaxima
	 * @param texto
	 * @return
	 */
	public static String trucarTextoSemReticencias(String texto,Integer qtdMaxima)
	{
		if(texto == null)
		{
			return "...";
		}
		return subString(texto, 1, qtdMaxima);
	}
	
	public static String montaTelefoneDDD(String ddd, String telefone)
	{
		String retDDD = "00";
		String retFone = "";
		if(StringUtils.isNoneBlank(ddd) && ddd.trim().length() == 2)
		{
			retDDD = removeCaracteresEspeciaisEEspaco(ddd);
		}
		
		if(StringUtils.isNoneBlank(telefone))
		{
			retFone = removeCaracteresEspeciaisEEspaco(telefone);
		}
		return retDDD + retFone;
	}
	
	public static boolean isJSONObject(Object texto) {
		try {
			new JSONObject(texto.toString());
		} catch(JSONException e) {
			return false;
		}
		
		return true;
	}
	
	public static String encodeURLValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
	
    public static String firstAndLastName(String value) {
        String[] vetorString = value.split(" ");
        ArrayList<String> arr = new ArrayList<String>();

        for (int i = vetorString.length - 1; i >= 0; i--) {
            arr.add(vetorString[i]);
        }

        return arr.get(arr.size() - 1) + arr.get(0);
    }

    public static String lastName(String value) {
        String[] vetorString = value.split(" ");
        ArrayList<String> arr = new ArrayList<String>();

        for (int i = vetorString.length - 1; i >= 0; i--) {
            arr.add(vetorString[i]);
        }

        return arr.get(0);
    }

    public static String firstName(String value) {
        String[] vetorString = value.split(" ");
        ArrayList<String> arr = new ArrayList<String>();

        for (int i = vetorString.length - 1; i >= 0; i--) {
            arr.add(vetorString[i]);
        }

        return arr.get(arr.size() - 1);
    }
}