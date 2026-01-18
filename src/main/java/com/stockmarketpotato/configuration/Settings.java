package com.stockmarketpotato.configuration;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Settings {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private boolean mailEnabled;
	private String mailHost = "";
	private String mailPassword;
	private String mailSmtpAuth = "true";
	private String mailSmtpPort = "465";
	private String mailSmtpSslEnable = "true";
	private String mailTransportProtocol = "smtp";
	private String mailUsername;
	
	public Long getId() {
		return id;
	}
	public String getMailHost() {
		return mailHost;
	}
	public String getMailPassword() {
		return mailPassword;
	}
	public String getMailSmtpAuth() {
		return mailSmtpAuth;
	}
	public String getMailSmtpPort() {
		return mailSmtpPort;
	}
	public String getMailSmtpSslEnable() {
		return mailSmtpSslEnable;
	}
	public String getMailTransportProtocol() {
		return mailTransportProtocol;
	}
	public String getMailUsername() {
		return mailUsername;
	}
	public boolean isMailEnabled() {
		return mailEnabled;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setMailEnabled(boolean mailEnabled) {
		this.mailEnabled = mailEnabled;
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}
	public void setMailSmtpAuth(String mailSmtpAuth) {
		this.mailSmtpAuth = mailSmtpAuth;
	}
	public void setMailSmtpPort(String mailSmtpPort) {
		this.mailSmtpPort = mailSmtpPort;
	}
	public void setMailSmtpSslEnable(String mailSmtpSslEnable) {
		this.mailSmtpSslEnable = mailSmtpSslEnable;
	}
	public void setMailTransportProtocol(String mailTransportProtocol) {
		this.mailTransportProtocol = mailTransportProtocol;
	}
	public void setMailUsername(String mailUsername) {
		this.mailUsername = mailUsername;
	}
	@Override
	public String toString() {
		return "Settings [id=" + id + ", " +

				"mailSmtpSslEnable='" + mailSmtpSslEnable + "'," +
				"mailHost='" + mailHost + "'," +
				"mailSmtpPort='" + mailSmtpPort + "'," +
				"mailTransportProtocol='" + mailTransportProtocol + "'," +
				"mailSmtpAuth='" + mailSmtpAuth + "'," +
				"mailUsername='" + mailUsername + "'," +
				"mailEnabled='" + mailEnabled  + "']";
	}

}
