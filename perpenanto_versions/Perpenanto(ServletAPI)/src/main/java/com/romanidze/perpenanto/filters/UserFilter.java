package com.romanidze.perpenanto.filters;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(
        filterName = "UserFilter",
        description = "Фильтр пользователей",
        urlPatterns = {"/user/*"}
)
public class UserFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain){

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(true);

        if(session.getAttribute("user_id") == null){

            try {
                response.sendRedirect("/login");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            filterChain.doFilter(req, resp);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }

    }

}
