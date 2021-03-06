package org.registrator.community.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.registrator.community.config.root.SpringRootConfig;
import org.registrator.community.config.root.TestingConfiguration;
import org.registrator.community.dao.VerificationTokenRepository;
import org.registrator.community.entity.VerificationToken;
import org.registrator.community.enumeration.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@ActiveProfiles("testing")
@ContextConfiguration(classes = { TestingConfiguration.class, SpringRootConfig.class })
public class VerificationTokenServiceIT extends AbstractTestNGSpringContextTests {
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;
	@Autowired
	private VerificationTokenService verificationTokenService;
	
	private static final Logger logger = LoggerFactory.getLogger(VerificationTokenService.class);
	private Date date = new Date();
	private List<VerificationToken> cTokenList = new ArrayList<VerificationToken>();
	private static final int DESIRED_RESOURCES = 10;

	// DataProviders
	@DataProvider(name = "ProviderForTokenFormation")
	public Object[][] formEmailAndLoginStrings() {
		logger.debug("Generating email strings");
		Object[][] tmp = new Object[DESIRED_RESOURCES][2];
		String emailMask = "tokenEmail#%03d@gmail.com";
		String login = "login%d";
		for (int i = 0; i < tmp.length; i++) {
			tmp[i][0] = String.format(emailMask, i);
			tmp[i][1] = String.format(login, i);
		}
		return tmp;
	}

	@DataProvider(name = "ProviderForSearchTypeOperations")
	public Object[][] formSearchData() {
		logger.debug("Generating data for findBy type operations");
		Object[][] tmp = new Object[cTokenList.size()][2];

		Date expired = new Date(0);
		boolean isExpired = false;
		for (int i = 0; i < tmp.length; i++) {
			VerificationToken tokInList = cTokenList.get(i),
					tok = verificationTokenRepository.findVerificationTokenByToken(tokInList.getToken());
			isExpired = false;
			if (i % 2 == 0) {
				verificationTokenRepository.delete(tok);

				tok.setExpiryDate(expired);
				tokInList.setExpiryDate(expired);

				isExpired = true;
				tok = verificationTokenRepository.save(tok);
			}
			tmp[i] = new Object[] { tok, isExpired };
		}
		return tmp;
	}
	
	// Tests

	@Test(priority=1)
	public void createHashForPasswordToken() {
		logger.debug("Start");
		for (int i = 0; i < DESIRED_RESOURCES; i++) {
			String generated = verificationTokenService.createHashForPasswordToken();
			Assert.assertNotNull(generated);
		}
		logger.debug("End");
	}

	@Test(dataProvider = "ProviderForTokenFormation", priority=2)
	public void savePasswordVerificationToken(String email, String login) {
		logger.debug("Start");
		VerificationToken actual = verificationTokenService.savePasswordVerificationToken(email,login, date),
				expected = new VerificationToken(actual.getToken(), 
				        login, email, actual.getExpiryDate(),
						TokenType.RECOVER_PASSWORD);

		Assert.assertEquals(expected.getUserEmail(), actual.getUserEmail());
		Assert.assertEquals(expected.getExpiryDate(), actual.getExpiryDate());

		List<VerificationToken> extraCheck = verificationTokenRepository.findTokensByEmail(actual.getUserEmail());
		Assert.assertNotNull(extraCheck);

		cTokenList.add(actual);
		logger.debug("End");
	}

	@Test(dataProvider = "ProviderForSearchTypeOperations", priority=3)
	public void isExistValidVerificationToken(VerificationToken tok, boolean isExpired) {
		logger.debug("Start");
		boolean manualCheck = tok.getExpiryDate().getTime() > System.currentTimeMillis(),
				formedCheck = verificationTokenService.isExistValidVerificationToken(tok.getToken());
		Assert.assertEquals(formedCheck, !isExpired);
		Assert.assertEquals(manualCheck, formedCheck);
		logger.debug("End");
	}

	@Test(dataProvider = "ProviderForSearchTypeOperations", priority=4)
	public void findVerificationTokenByTokenAndTokenType(VerificationToken expected, boolean isExpired) {
		logger.debug("Start");
		VerificationToken actual = verificationTokenService.findVerificationTokenByTokenAndTokenType(expected.getToken(),
				expected.getTokenType());

		Assert.assertEquals(actual.getToken(), expected.getToken());
		Assert.assertEquals(actual.getUserEmail(), expected.getUserEmail());
		Assert.assertEquals(actual.getExpiryDate().getTime(), expected.getExpiryDate().getTime());
		Assert.assertEquals(actual.getId(), expected.getId());
		logger.debug("End");
	}

	@Test(priority=5)
	public void deletePasswordVerificationTokenByEmailAndByTokenName() {
		logger.debug("Start");
		int listSize = cTokenList.size();
		long repSize = verificationTokenRepository.count();
		
		logger.info("Size before cleanUp: "+repSize);
		for(int i = 0; i< listSize; i++){
			VerificationToken tok = verificationTokenRepository.findVerificationTokenByToken(cTokenList.get(i).getToken());
			if(i%2==0){
				verificationTokenService.deletePasswordVerificationTokenByLogin(tok.getUserLogin());
			}else{
				verificationTokenService.deleteVerificationToken(tok);
			}
			tok = verificationTokenRepository.findVerificationTokenByToken(cTokenList.get(i).getToken());
			Assert.assertNull(tok);
		}
		Assert.assertEquals(verificationTokenRepository.count(), repSize-listSize);
		logger.info("Size after cleanUp: "+verificationTokenRepository.count());
		cTokenList.clear();
		logger.debug("End");
	}
}
