package org.registrator.community.controller.filter;

import java.io.IOException;

import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.filter.OncePerRequestFilter;

//public class LocaleConfigurerFilter extends OncePerRequestFilter {
public class LocaleConfigurerFilter extends RequestContextFilter {

    private static final Logger logger = LoggerFactory.getLogger(LocaleConfigurerFilter.class);

    private CookieLocaleResolver cookieLocaleResolver;


    @Override
    protected void initFilterBean() throws ServletException {

        try {
            cookieLocaleResolver = new CookieLocaleResolver();
        } catch (Exception e) {
            logger.error("Could not initialize CookieLocaleResolver.");
        }

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        Locale locale;
        if (cookieLocaleResolver!=null) {
            locale = cookieLocaleResolver.resolveLocale(request);
            if (locale!=null){
/*                System.out.println("DEBUG FILTER LOCALE OLD: "+locale);
                System.out.println("DEBUG FILTER LOCALE NEW: "+newLocale);
                if (locale != newLocale){
                    System.out.println("DEBUG FILTER LOCALE CALL: "+locale);
                    if (locale != null){
                        //t.updateLocale(newLocale);
                    }
                }
                locale = newLocale;
                //System.out.println("DEBUG FILTER LOCALE SLR: "+locale);*/
                logger.debug("DEBUG FILTER LOCALE: In filter cookie: Locale {}.", locale);
                LocaleContextHolder.setLocale(locale);
            } else {
                logger.info("Could not resolve locale CookieLocaleResolver.");
            }
        } else {
            logger.error("Could not get CookieLocaleResolver.");
        }

        chain.doFilter(request, response);

    }

}
