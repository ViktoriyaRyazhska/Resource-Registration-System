package org.registrator.community.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.registrator.community.dao.VerificationTokenRepository;
import org.registrator.community.entity.VerificationToken;
import org.registrator.community.enumeration.TokenType;
import org.registrator.community.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{
	
	public static final int PASSWORD_TOKEN_EXPIRY_TIME = 1000*60*60*24;
	public static final int EMAIL_TOKEN_EXPIRY_TIME = 1000*60*60*24*7;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public boolean deletePasswordVerificationTokenByLogin(String login) {
	    //TODO: test
		VerificationToken passwordResetToken = verificationTokenRepository.findTokenByLogin(login);
		if(passwordResetToken != null){
			verificationTokenRepository.delete(passwordResetToken);
			return true;
		}
		return false;
	}

	@Override
	public VerificationToken savePasswordVerificationToken(String userEmail, String login, Date nowTime) {
	    //TODO: test
		String token = createHashForPasswordToken();
		nowTime.setTime(nowTime.getTime()+PASSWORD_TOKEN_EXPIRY_TIME);
		VerificationToken passwordVerificationToken = new VerificationToken(token, login, userEmail,nowTime,TokenType.RECOVER_PASSWORD);
		deletePasswordVerificationTokenByLogin(login);
		verificationTokenRepository.save(passwordVerificationToken);
		return passwordVerificationToken;
	}
	
	@Override
	public VerificationToken saveEmailConfirmationToken(String login, String userEmail, Date nowTime, String baseLink) {
	    //TODO: test
		String token = createHashForPasswordToken();
		nowTime.setTime(nowTime.getTime()+EMAIL_TOKEN_EXPIRY_TIME);
		VerificationToken emailVerificationToken = new VerificationToken(token, login, userEmail,nowTime,TokenType.CONFIRM_EMAIL, baseLink);
		deletePasswordVerificationTokenByLogin(login);
		verificationTokenRepository.save(emailVerificationToken);
		return emailVerificationToken;
	}

	@Override
	public String createHashForPasswordToken() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	@Override
	public VerificationToken findVerificationTokenByTokenAndTokenType(String token,
			TokenType type) {
		return verificationTokenRepository.findVerificationTokenByTokenAndTokenType(token, type);
	}
	
	@Override
	public void deleteVerificationTokenList(List<VerificationToken> verifacationTokenList){
	    verificationTokenRepository.delete(verifacationTokenList);
	}
	
	
	@Override
    public VerificationToken findVerificationTokenByLoginAndTokenType(String login, TokenType type) {
        return verificationTokenRepository.findVerificationTokenByLoginAndTokenType(login, type);
    }

	@Override
	public boolean isExistValidVerificationToken(String token) {
		VerificationToken verToken = verificationTokenRepository.findVerificationTokenByToken(token);
		if(verToken != null){
			return (verToken.getExpiryDate().getTime()> System.currentTimeMillis());
		}
		return false;
	}

	@Override
	public void deleteVerificationToken(VerificationToken verificationToken) {
		verificationTokenRepository.delete(verificationToken);
	}
	
	@Override
	public List<VerificationToken> findVerificationTokensByLoginsAndTokenType(List<String> loginList, TokenType type){
        return verificationTokenRepository.findVerificationTokensByLoginsAndTokenType(loginList, type);
        
	    
	}
}
