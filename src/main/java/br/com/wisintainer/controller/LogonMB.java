package br.com.wisintainer.controller;

import com.github.adminfaces.template.session.AdminSession;

import br.com.wisintainer.model.Application;

import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;

import java.io.IOException;
import java.io.Serializable;

import static br.com.wisintainer.helper.Utils.addDetailMessage;

/**
 * Created by rmpestano on 12/20/14.
 *
 * This is just a login example.
 *
 * AdminSession uses isLoggedIn to determine if user must be redirect to login
 * page or not. By default AdminSession isLoggedIn always resolves to true so it
 * will not try to redirect user.
 *
 * If you already have your authorization mechanism which controls when user
 * must be redirect to initial page or logon you can skip this class.
 */
@Named
@SessionScoped
@Specializes
public class LogonMB extends AdminSession implements Serializable {

	private String currentUser;
	private String email;
	private String password;
	private boolean remember;

	public void login() throws IOException {
		if (email.equals("adasc") && password.equals("adasc")) {
			currentUser = email;
			addDetailMessage("Acesso liberado <b>" + email + "</b>");
			Faces.getExternalContext().getFlash().setKeepMessages(true);
			Faces.redirect("index.xhtml");
		} else {
			addDetailMessage("Erro <b>" + email + "</b>");
			Faces.getExternalContext().getFlash().setKeepMessages(true);
			Faces.redirect("login.xhtml");
		}

	}

	@Override
	public boolean isLoggedIn() {

		return currentUser != null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRemember() {
		return remember;
	}

	public void setRemember(boolean remember) {
		this.remember = remember;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
}
