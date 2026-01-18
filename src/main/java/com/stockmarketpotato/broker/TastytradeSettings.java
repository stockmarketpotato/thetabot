package com.stockmarketpotato.broker;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TastytradeSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String apiLogin;
	private String apiPassword;
	private String certApiLogin;
	private String certApiPassword;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getApiLogin() {
		return apiLogin;
	}

	public void setApiLogin(String apiLogin) {
		this.apiLogin = apiLogin;
	}

	public String getApiPassword() {
		return apiPassword;
	}

	public void setApiPassword(String apiPassword) {
		this.apiPassword = apiPassword;
	}

	@Override
	public String toString() {
		return "Settings [id=" + id + ", " + "apiLogin=" + apiLogin + "']";
	}

	public String getCertApiLogin() {
		return certApiLogin;
	}

	public void setCertApiLogin(String certApiLogin) {
		this.certApiLogin = certApiLogin;
	}

	public String getCertApiPassword() {
		return certApiPassword;
	}

	public void setCertApiPassword(String certApiPassword) {
		this.certApiPassword = certApiPassword;
	}

}
