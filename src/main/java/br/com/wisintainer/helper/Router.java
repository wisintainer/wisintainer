package br.com.wisintainer.helper;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
	
	private static Boolean isOnline;
	
	/*
	 * Identação hierárquica proposital;
	 */
	private static File root;
		private static File metaInf;
		private static File webContent;
			private static File publico;
			private static File webInf;
				private static File classes;
					private static File project;
					private static File resource;
						private static File template;
	
	private Router() {
	}
		
	public static File getRoot() throws UnsupportedEncodingException {
		if (root == null) {
			URL url = Router.class.getResource("/");
			String raiz = URLDecoder.decode(url.getFile(), "UTF-8");
			
			root = (new File(raiz)).getParentFile().getParentFile();
		}
		
		return root;
	}
	
	public static Boolean isOnline() throws UnsupportedEncodingException {
		if (isOnline == null) {
			/*
			 * Está online se o Root enxerga o WEB-INF diretamente.
			 * 	Caso contrário deve enxergar o WebContent.
			 */
			isOnline = (new File(getRoot(), "WEB-INF")).exists();
		}
		
		return isOnline;
	}
	
	public static File getWebInf() throws UnsupportedEncodingException {
		if (webInf == null) {
			if (isOnline()) {
				webInf = new File(getRoot(), "WEB-INF");
			} else {
				webInf = new File(getRoot(), "WebContent/WEB-INF");
			}
		}
		
		return webInf;
	}
	
	public static File getMetaInf() throws UnsupportedEncodingException {
		if (metaInf == null) {
			if (isOnline()) {
				metaInf = new File(getRoot(), "META-INF");
			} else {
				metaInf = new File(getRoot(), "WebContent/META-INF");
			}
		}
		
		return metaInf;
	}
	
	public static File getPublic() throws UnsupportedEncodingException {
		if (publico == null) {
			if (isOnline()) {
				publico = new File(getRoot(), "public");
			} else {
				publico = new File(getRoot(), "WebContent/public");
			}
		}
		
		return publico;
	}
	
	public static File getWebContent() throws UnsupportedEncodingException {
		if (webContent == null) {
			webContent = new File(getRoot(), "WebContent");
		}
		
		return webContent;
	}
	
	public static File getClasses() throws UnsupportedEncodingException {
		if (classes == null) {
			if (isOnline()) {
				classes =  new File(getWebInf(), "classes");
			} else {
				classes =  new File(getRoot(), "src");
			}
		}
		
		return classes;
	}
	
	public static File getProjectSrc() throws UnsupportedEncodingException {
		if (project == null) {
			if (isOnline()) {
				project = new File(getClasses(), "br/com/sescpr/scaweb");
			} else {
				project = new File(getClasses(), "src/br/com/sescpr/scaweb");
			}
		}
		
		return project;
	}
	
	public static File getResource() throws UnsupportedEncodingException {
		if (resource == null) {
			if (isOnline()) {
				resource = getClasses();
			} else {
				resource = new File(getRoot(), "resource");
			}
		}
		
		return resource;
	}
	
	public static File getTemplate() throws UnsupportedEncodingException {
		if (template == null) {
			template = new File(getResource(), "template");
		}
		
		return template;
	}
	
	public static String defineVersion() throws UnsupportedEncodingException {
        String version = "000";
        if (getRoot() != null) {
            File root = getRoot();
            File parent = root.getParentFile();
            String aux = root.getAbsolutePath().replace(parent.getAbsolutePath(), "");

            Pattern pattern = Pattern.compile(".*##([0-9]+).*");
            Matcher matcher = pattern.matcher(aux);
            if (matcher.find()) {
                version = matcher.group(1);
            }
        }

        return version;
    }
}