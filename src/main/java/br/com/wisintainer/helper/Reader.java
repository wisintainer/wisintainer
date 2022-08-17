package br.com.wisintainer.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Reader {
	public static String getContent(File arquivo) throws IOException {
		FileInputStream fis = new FileInputStream(arquivo);
	    byte[] conteudo = new byte[(int)arquivo.length()];
	    fis.read(conteudo);
	    fis.close();
	    
	    return new String(conteudo, "UTF-8");
	}
	
	public static String getContent(String caminhoArquivo) throws IOException {
		File arquivo = new File(caminhoArquivo);
		
		return getContent(arquivo);
	}
	
	public static byte[] getBytesContent(String caminhoArquivo) throws IOException {
		File arquivo = new File(caminhoArquivo);
		
		byte[] retorno = new byte[(int) arquivo.length()];
		
		FileInputStream fileInputStream = new FileInputStream(arquivo);
		
		fileInputStream.read(retorno);
	    fileInputStream.close();
		
		return retorno;
	}
}
