package org.registrator.community.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.support.membermodification.MemberModifier;
import org.registrator.community.entity.Address;
import org.registrator.community.entity.PassportInfo;
import org.registrator.community.entity.User;
import org.registrator.community.entity.VerificationToken;
import org.registrator.community.enumeration.TokenType;
import org.registrator.community.service.impl.NotConfirmedUsersServiceimpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;




public class NotConfirmedUsersServiceTest {
    
    //test data
    private final String LOGIN = "testLogin";
    private final String BASELINK = "testBaseLink";
    private final String EMAIL = "test@ukr.net";
    private final String TOKEN = "testToken";
    private User user = new User();
    private List<User> userList = new ArrayList<User>();
    private VerificationToken verificationToken = new VerificationToken();
    
    
    @InjectMocks
    NotConfirmedUsersService notConfirmedUsersService = new NotConfirmedUsersServiceimpl();
   
    @Mock
    private UserService userService;
    
    @Mock
    private MailService mailService;
    
    @Mock
    private VerificationTokenService verificationTokenService;
    
    @Mock
    private PassportService passportService;
    
    @Mock
    private AddressService addressService;
    
    private Logger logger;
    
    @BeforeMethod
    public void init() throws IllegalArgumentException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        
        // inject logger into tested service
        logger = LoggerFactory.getLogger("");
        MemberModifier
            .field(NotConfirmedUsersServiceimpl.class, "logger")
            .set(notConfirmedUsersService, logger);
        user.setEmail(EMAIL);
        user.setLogin(LOGIN);

    }
    
    @Test
    public void sendConfirmEmailFirstTime(){
        verificationToken.setToken(TOKEN);

        when(userService.findUserByLogin(LOGIN)).thenReturn(user);
        when(verificationTokenService.saveEmailConfirmationToken(anyString(), anyString(), any(Date.class), anyString())).thenReturn(verificationToken);
        notConfirmedUsersService.sendConfirmEmailFirstTime(LOGIN,BASELINK);
        verify(verificationTokenService).saveEmailConfirmationToken(anyString(), anyString(), any(Date.class), anyString());
        verify(mailService).sendComfirmEMail(anyString(), anyString(), anyString(), anyString());
        
    }
    
    @Test
    public void sendConfirmEmailAgain(){
        userList.add(new User("login1","email1@ukr.net"));
        userList.add(new User("login2","email2@ukr.net"));
        when(verificationTokenService.findVerificationTokenByLoginAndTokenType(anyString(), eq(TokenType.CONFIRM_EMAIL))).thenReturn(verificationToken);
        notConfirmedUsersService.sendConfirmEmailAgain(userList);
        verify(mailService, times(2)).sendComfirmEMail(anyString(), anyString(), anyString(), anyString());
    }
    @Test
    public void actionsWithNotConfirmedUsers(){
        
    }
    
    @Test
    public void deleteNotConfirmedUsers(){ 
        notConfirmedUsersService.deleteNotConfirmedUsers(userList);
        verify(passportService).delete(anyListOf(PassportInfo.class));
        verify(addressService).delete(anyListOf(Address.class));
        verify(userService).delete(anyListOf(User.class));
        
    }
    
    @Test
    public void deleteListVerificationToken(){
        
        
    }
    
    @Test
    public void confirmEmail(){
        boolean actual = notConfirmedUsersService.confirmEmail(TOKEN);
        Assert.assertEquals(actual, false);
        
        when(verificationTokenService.findVerificationTokenByTokenAndTokenType(anyString(), eq(TokenType.CONFIRM_EMAIL))).thenReturn(verificationToken);
        actual = notConfirmedUsersService.confirmEmail(TOKEN);
        Assert.assertEquals(actual, false);
        
        when(userService.findUserByEmail(anyString())).thenReturn(user);
        actual = notConfirmedUsersService.confirmEmail(TOKEN);
        Assert.assertEquals(actual, true);
    }
    
}
