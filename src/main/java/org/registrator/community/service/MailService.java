package org.registrator.community.service;

import java.util.List;
import java.util.Map;

public interface MailService {
	
	void sendComfirmEMail(String recepientEmail, String recepientName, String login, String token, String url);
	
	public void sendRecoveryPasswordMail(String recepientEmail, String token ,String url);

	void sendResetedPasswordMail(String recepientEmail, String recepientName, String login, String password);
	
	void sendBatchResetedPasswordMail(List<Map<String, Object>> listTemplateVariables);

}
