package org.registrator.community.service.impl;

import org.registrator.community.entity.User;
import org.registrator.community.enumeration.UserStatus;
import org.registrator.community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("authenticationProvider")
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(LimitLoginAuthenticationProvider.class);

	@Autowired
	private PasswordEncoder userPasswordEncoder;

    @Autowired
    private MessageSource messageSource;

	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		setPasswordEncoder(userPasswordEncoder);
		super.setUserDetailsService(userDetailsService);
	}

	@Override
	public Authentication authenticate(Authentication authentication) {

        User user = null;
        try {
            user = userService.findUserByLogin(authentication.getName());
            Authentication auth = super.authenticate(authentication);
            if(user.getStatus() != UserStatus.ACTIVE){
                throw new DisabledException("");
            }
            userService.resetFailAttempts(authentication.getName());
            logger.debug("{} is authenticated successfully", authentication.getName());
            return auth;
        } catch (LockedException e) {
            logger.warn("{} is locked at : {}", authentication.getName(), user.getLastModified());
            throw new LockedException(user.getLastModified() + messageSource.getMessage("label.locked", null, LocaleContextHolder.getLocale()));
        } catch (BadCredentialsException e) {
            userService.updateFailAttempts(authentication.getName());
            logger.info("{} has entered wrong credentials", authentication.getName());
            throw new BadCredentialsException(messageSource.getMessage("label.badCredentials", null, LocaleContextHolder.getLocale()));
        } catch (AccountExpiredException e) {
            logger.warn("{} account expired", authentication.getName());
            throw new AccountExpiredException(messageSource.getMessage("label.expired", null, LocaleContextHolder.getLocale()));
        }
        catch (DisabledException e) {
            if (user.getStatus() == UserStatus.BLOCK) {
                logger.info("User {} with status {} try to login into the system", user.getLogin(), user.getStatus());
                throw new DisabledException(messageSource.getMessage("label.blocked", null, LocaleContextHolder.getLocale()));
            }
            if (user.getStatus() == UserStatus.NOTCOMFIRMED) {
                logger.info("User {} with status {} try to login into the system", user.getLogin(), user.getStatus());
                throw new DisabledException(messageSource.getMessage("label.nonConfirmed", null, LocaleContextHolder.getLocale()));
            }
            if (user.getStatus() == UserStatus.INACTIVE) {
                logger.info("User {} with status {} try to login into the system", user.getLogin(), user.getStatus());
                throw new DisabledException(messageSource.getMessage("label.inactive", null, LocaleContextHolder.getLocale()));
            }
            else
            {
                logger.error("User {} with status {} try to login into the system", user.getLogin(), user.getStatus());
                throw new DisabledException(messageSource.getMessage("label.unknownStatus", null, LocaleContextHolder.getLocale()));
            }
        }
        catch (Exception e) {
			logger.error("Something went wrong while {} was trying to authenticate", authentication.getName());
			throw new BadCredentialsException(messageSource.getMessage("label.authenticationError", null, LocaleContextHolder.getLocale()));
		}
    }
}
