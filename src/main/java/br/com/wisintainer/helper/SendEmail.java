package br.com.wisintainer.helper;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {
	private String user = br.com.wisintainer.helper.Properties.getInstance()
			.getAsString("private.administrador.email.username");

	private String passwordEmail = br.com.wisintainer.helper.Properties.getInstance()
			.getAsString("private.administrador.email.password");

	private String emailReply = br.com.wisintainer.helper.Properties.getInstance()
			.getAsString("private.administrador.email.no-reply");

	private String env = br.com.wisintainer.helper.Properties.getInstance().getAsString("ambiente");

	public void sendEmailAttachment(String subject, String to, File file, String body) {
		Thread newThread = new Thread(() -> {
			try {
				sendEmail(subject, to, body, file);
			} catch (Exception e) {
				Logger.getLogger().error(e.getMessage(), e);
			}
		});
		newThread.start();

	}

	public void sendEmail(String subject, String to, String body) {
		Thread newThread = new Thread(() -> {
			try {
				sendEmail(subject, to, body, null);
			} catch (Exception e) {
				Logger.getLogger().error(e.getMessage(), e);
			}
		});
		newThread.start();

	}

	public void sendEmail(String subject, String to, String body, boolean enviaDeVerdade) {
		Thread newThread = new Thread(() -> {
			try {
				sendEmail(subject, to, body, null, true);
			} catch (Exception e) {
				Logger.getLogger().error(e.getMessage(), e);
			}
		});
		newThread.start();

	}

	private Boolean sendEmail(String subject, String to, String body, File file) {
		if (!env.equals("prd"))
			to = br.com.wisintainer.helper.Properties.getInstance()
					.getAsString("private.administrador.email.mantenedor");

		Properties props = new Properties();
		props.put("mail.smtp.host", br.com.wisintainer.helper.Properties.getInstance()
				.getAsString("private.administrador.email.smtp-host")); // SMTP Host
		props.put("mail.smtp.port", br.com.wisintainer.helper.Properties.getInstance()
				.getAsString("private.administrador.email.smtp-port")); // TLS Port
		props.put("mail.smtp.auth",
				br.com.wisintainer.helper.Properties.getInstance().getAsString("private.administrador.email.auth")); // enable
																														// authentication
		props.put("mail.smtp.starttls.enable",
				br.com.wisintainer.helper.Properties.getInstance().getAsString("private.administrador.email.starttls")); // enable
																															// STARTTLS

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, passwordEmail);
			}
		};

		Session session = Session.getInstance(props, auth);

		try {
			MimeMessage msg = new MimeMessage(session);

			msg.addHeader("Content-type", "text/html; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(emailReply));
			msg.setReplyTo(InternetAddress.parse(emailReply, false));
			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			MimeBodyPart bodyEmail = new MimeBodyPart();
			bodyEmail.setContent(body, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart("related");

			multipart.addBodyPart(bodyEmail);

			if (file != null) {
				String filename = file.getName();
				DataSource source = new FileDataSource(file);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(filename);
				multipart.addBodyPart(messageBodyPart2);
			}
			msg.setContent(multipart);

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (file != null)
				file.delete();
		}
		return true;
	}

	private Boolean sendEmail(String subject, String to, String body, File file, boolean enviaDeVerdade) {

		Properties props = new Properties();
		props.put("mail.smtp.host", br.com.wisintainer.helper.Properties.getInstance()
				.getAsString("private.administrador.email.smtp-host")); // SMTP Host
		
		props.put("mail.smtp.port", br.com.wisintainer.helper.Properties.getInstance()
				.getAsString("private.administrador.email.smtp-port")); // SMTP PORT
		
		props.put("mail.smtp.auth",
				br.com.wisintainer.helper.Properties.getInstance().getAsString("private.administrador.email.auth")); // enable
																														// authentication
		props.put("mail.smtp.starttls.enable",
				br.com.wisintainer.helper.Properties.getInstance().getAsString("private.administrador.email.starttls")); // enable
																															// STARTTLS
		
		props.put("mail.smtp.ssl.enable",
				br.com.wisintainer.helper.Properties.getInstance().getAsString("private.administrador.email.ssl")); // enable
																															// STARTTLS

		// create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, passwordEmail);
			}
		};

		Session session = Session.getInstance(props, auth);

		try {
			MimeMessage msg = new MimeMessage(session);

			msg.addHeader("Content-type", "text/html; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");
			msg.setFrom(new InternetAddress(emailReply));
			msg.setReplyTo(InternetAddress.parse(emailReply, false));
			msg.setSubject(subject, "UTF-8");

			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));

			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			MimeBodyPart bodyEmail = new MimeBodyPart();
			bodyEmail.setContent(body, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart("related");

			multipart.addBodyPart(bodyEmail);

			if (file != null) {
				String filename = file.getName();
				DataSource source = new FileDataSource(file);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(filename);
				multipart.addBodyPart(messageBodyPart2);
			}
			msg.setContent(multipart);

			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (file != null)
				file.delete();
		}
		return true;
	}

}
