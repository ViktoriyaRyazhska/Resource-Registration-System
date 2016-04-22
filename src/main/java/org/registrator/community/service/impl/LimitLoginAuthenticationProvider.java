package org.registrator.community.service.impl;

import java.sql.Timestamp;

import org.registrator.community.entity.User;
import org.registrator.community.enumeration.UserStatus;
import org.registrator.community.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.LocaleContextResolver;

@Component("authenticationProvider")
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	UserService userService;

	@Autowired
	private Logger logger;

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

    //@Transactional
	@Override
	public Authentication authenticate(Authentication authentication) {

        User user = null;
		try {
			user = userService.findUserByLogin(authentication.getName());
			boolean isAccountNonExpired = (user.getAccountNonExpired() == 1);
			Authentication auth = super.authenticate(authentication);

			/*if (!isAccountNonExpired){
                System.out.println("AccountExpiredException");
                throw new AccountExpiredException(messageSource.getMessage("label.expired", null, LocaleContextHolder.getLocale()));
            }*/

            if (user.getStatus() == UserStatus.BLOCK) {
                System.out.println("UserStatus.BLOCK");
                throw new DisabledException(messageSource.getMessage("label.blocked", null, LocaleContextHolder.getLocale()));
            }
            if (user.getStatus() == UserStatus.NOTCOMFIRMED) {
                throw new DisabledException(messageSource.getMessage("label.nonConfirmed", null, LocaleContextHolder.getLocale()));
            }
            if (user.getStatus() == UserStatus.INACTIVE) {
                throw new DisabledException(messageSource.getMessage("label.inactive", null, LocaleContextHolder.getLocale()));
            }

			userService.resetFailAttempts(authentication.getName());
			logger.debug(authentication.getName() + " is authentificated successfully");
			return auth;

		} catch (LockedException e) {
			String error = "";
			user = userService.findUserByLogin(authentication.getName());

            if (user != null) {
                Timestamp lastAttempts = user.getLastModified();
                error = lastAttempts + messageSource.getMessage("label.locked", null, LocaleContextHolder.getLocale());
                logger.error(authentication.getName() + " is locked at : "+user.getLastModified());
            } else {
                error = e.getMessage();
            }
			throw new LockedException(error);
		} catch (BadCredentialsException e) {
            logger.error(authentication.getName() + " has entered wrong credentials");
            userService.updateFailAttempts(authentication.getName());
			System.out.println("user.getAccountNonLocked() = " + user.getAccountNonLocked());
            if (user != null && user.getAccountNonLocked() == 0) {
                throw new LockedException(messageSource.getMessage("label.locked", null, LocaleContextHolder.getLocale()));
            }
            throw new BadCredentialsException(messageSource.getMessage("label.badCredentials", null, LocaleContextHolder.getLocale()));

        } catch (Exception e) {
			logger.error("Something went wrong while "+authentication.getName()+" was trying to authentificate");
			throw new BadCredentialsException("Authentication error");
		}

	}

}
