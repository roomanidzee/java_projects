package com.romanidze.asyncappdbstorage.security.filters;

import com.romanidze.asyncappdbstorage.security.authentication.JWTTokenAuthentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component("restAuthenticationFilter")
public class JWTTokenAuthenticationFilter implements Filter {

    @Value("${jwt.header}")
    private String header;

    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        String token = request.getHeader(this.header);
        JWTTokenAuthentication authentication;

        if (token == null) {
            authentication = new JWTTokenAuthentication(null);
            authentication.setAuthenticated(false);
        } else {
            authentication = new JWTTokenAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {}

}
