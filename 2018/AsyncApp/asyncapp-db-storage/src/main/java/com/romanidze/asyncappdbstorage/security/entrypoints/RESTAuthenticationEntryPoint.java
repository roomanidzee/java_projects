package com.romanidze.asyncappdbstorage.security.entrypoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanidze.asyncappdbstorage.security.errors.Error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component("restAuthenticationEntryPoint")
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Autowired
    public RESTAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException ex)
                                                                                                     throws IOException{

        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setCharacterEncoding("UTF-8");

        StringBuilder sb = new StringBuilder();

        sb.append("У вас нет доступа к URL: ").append(req.getRequestURL());

        Error error = new Error(HttpStatus.UNAUTHORIZED);
        error.setMessage(sb.toString());
        error.setDebugMessage(ex.getMessage());

        String jsonResponse = this.objectMapper.writeValueAsString(error);

        PrintWriter pw = resp.getWriter();
        pw.println(jsonResponse);

    }
}
