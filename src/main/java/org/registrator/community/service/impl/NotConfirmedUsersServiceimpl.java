package org.registrator.community.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.registrator.community.dao.AddressRepository;
import org.registrator.community.dao.PassportRepository;
import org.registrator.community.dao.UserRepository;
import org.registrator.community.dao.VerificationTokenRepository;
import org.registrator.community.dto.json.UsersDataNotConfJson;
import org.registrator.community.entity.Address;
import org.registrator.community.entity.PassportInfo;
import org.registrator.community.entity.Role;
import org.registrator.community.entity.User;
import org.registrator.community.entity.VerificationToken;
import org.registrator.community.enumeration.RoleType;
import org.registrator.community.enumeration.TokenType;
import org.registrator.community.enumeration.UserStatus;
import org.registrator.community.service.NotConfirmedUsersSerice;
import org.registrator.community.service.MailService;
import org.registrator.community.service.UserService;
import org.registrator.community.service.VerificationTokenService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotConfirmedUsersServiceimpl implements NotConfirmedUsersSerice {
	
    @Autowired
    private Logger logger;
    
    @Autowired
	UserService userService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private PassportRepository passportRepository;
	
	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private VerificationTokenService verificationTokenService;
	
	@Autowired
    private VerificationTokenRepository verificationTokenRepository;
	
	
	
	/*@Autowired
	private PasswordEncoder  userPasswordEncoder;*/
	
	@Override
	public void sendConfirmEmailFirstTime(String login, String baseLink) {
		User user = userService.findUserByLogin(login);
		if(user != null){
			VerificationToken verifacationToken = verificationTokenService.saveEmailConfirmationToken(user.getLogin(), user.getEmail(), new Date(), baseLink);
			mailService.sendComfirmEMail(user.getEmail(), user.getFirstName(),verifacationToken.getToken(),baseLink);
		}	
	}
	
	@Override
    public String sendConfirmEmailAgain(List<User> userList) {

        for (User user: userList){
            
            VerificationToken verifacationToken = verificationTokenService.findVerificationTokenByLoginAndTokenType(user.getLogin(), TokenType.CONFIRM_EMAIL);
            if (verifacationToken!=null){
                logger.info("Send email to "+ user.getEmail());
                mailService.sendComfirmEMail(user.getEmail(), user.getFirstName(),verifacationToken.getToken(),verifacationToken.getBaseLink());
            }else{
                logger.warn("no verifacationToken found");
                return "msg.notconfirmedusers.unsucsesfullysent";
            }

        }
        return "msg.notconfirmedusers.sucsesfullysent";              
    }
	
	
	@Transactional
	@Override
	public String actionsWithNotConfirmedUsers(UsersDataNotConfJson usersDataNotConfJson){
	    logger.info("Recieved data: " + usersDataNotConfJson);
	    if (usersDataNotConfJson.getActions()==null || usersDataNotConfJson.getLogins()==null) {
            logger.warn("Empty usersDataNotConfJson file");
            return "msg.batchops.wrongInput";
        }
	    
	    List<String> loginList = new ArrayList<String>();
	    String logins = usersDataNotConfJson.getLogins();
	    Collections.addAll(loginList, logins.split(","));
	    logger.debug("Loking for users with logins: "+logins);
        List<User> userList = userRepository.findUsersByLoginList(loginList);
        if (userList.isEmpty()){
            logger.warn("no such users found");
            return "msg.notconfirmedusers.nosuchusersfound";
        }
        logger.debug("users found");
        
        for (User user: userList){
            if (user.getStatus() != UserStatus.NOTCOMFIRMED) {
                logger.debug("Try to delete users wich are not in status NOTCOMFIRMED");
                return "msg.notconfirmedusers.onlynotconf";
            }
        }
        
        logger.debug("check logged user RoleType");
        User loggetUser = userService.getLoggedUser();
	    RoleType roleType = loggetUser.getRole().getType();
	    logger.debug("logged user RoleType: "+ roleType);
	    
	    if (roleType!=RoleType.ADMIN && roleType!=RoleType.COMMISSIONER){
	        logger.warn("try to perform action by forbitten user:"+ roleType);
	        return "msg.notconfirmedusers.onlyadminorcommisioner";
	    }else if(roleType == RoleType.COMMISSIONER){
	        logger.debug("check whether users over which operation is performing  belongs to the same community");
	        for (User user: userList){
	            if(user.getTerritorialCommunity()!=loggetUser.getTerritorialCommunity()){
	                logger.debug("");
	                return "msg.notconfirmedusers.commistryothercommunity";
	            }
	        }
	    }
 
        switch(usersDataNotConfJson.getActions()){
        case DELETE:{
            logger.info("Run Action DELETE");
            deleteListVerificationToken(loginList);
            return deleteNotConfirmedUsers(userList);
        }
        case SENDEMAILAGAIN:
            logger.info("Run Action SENDEMAILAGAIN");
            return sendConfirmEmailAgain(userList);
        }
	        
	    return  "msg.batchops.wrongInput";
	}
	
	 /**
     * Method, which delete users only if user status = NOTCOMFIRMED
     *
     * @return String message
     */
    @Transactional
    @Override
    public String deleteNotConfirmedUsers(List<User> userList) {
        
        List<PassportInfo> passportInfoList = new ArrayList<PassportInfo>();
        List<Address> addressList = new ArrayList<Address>();
                
        for (User user: userList){
            passportInfoList.addAll(user.getPassport());
            addressList.addAll(user.getAddress());
        }

        
        logger.info("start delete operations");
        
        passportRepository.delete(passportInfoList);
        logger.info("pasports succesfuly deleted");
        
        addressRepository.delete(addressList);
        logger.info("addresses succesfuly deleted");
        
        userRepository.delete(userList);
        logger.info("users succesfuly deleted");
        
        return "msg.notconfirmedusers.sucsesfulydeleted";
    }
    
    @Transactional
    void deleteListVerificationToken(List<String> loginList){
        
        
        logger.debug("Looking for verifacationTokens with logins: "+ loginList);
        List<VerificationToken> verifacationTokenList = verificationTokenService.findVerificationTokensByLoginsAndTokenType(loginList, TokenType.CONFIRM_EMAIL);
        if (verifacationTokenList.isEmpty()){
            logger.warn("no such VerificationToken found in database");
        }else{
            logger.debug("VerificationTokens found");
        }

        if (!verifacationTokenList.isEmpty()){
            logger.debug("start delete operations");
            verificationTokenRepository.delete(verifacationTokenList);
            logger.debug("VerificationTokens successfully deleted");
        }        
        
    }
	
	@Transactional
	@Override
	public Boolean confirmEmail(String token){
		
			VerificationToken verToken = verificationTokenService.findVerificationTokenByTokenAndTokenType(token, TokenType.CONFIRM_EMAIL);
			if (verToken == null) return false;
			User user = userService.findUserByEmail(verToken.getUserEmail());
			if (user == null) return false;
			user.setStatus(UserStatus.INACTIVE);
			userService.updateUser(user);
			verificationTokenService.deleteVerificationToken(verToken);
			return true;

	}

}
