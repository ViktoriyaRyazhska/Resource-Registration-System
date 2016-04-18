package org.registrator.community.service;

import org.registrator.community.entity.SmtpParameters;

public interface MailService {
	
	void sendComfirmEMail(String recepientEmail, String recepientName, String token, String url);
	
	public void sendRecoveryPasswordMail(String recepientEmail, String recepientName,String token ,String url);

	void sendResetedPasswordMail(String recepientEmail, String recepientName, String login, String password);

    boolean testConnection(SmtpParameters parameters);
}
