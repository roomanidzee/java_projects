package com.romanidze.perpenanto.controllers;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.services.implementations.ProfileServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ProfileServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "ProfileController",
        description = "Профиль пользователя",
        urlPatterns = {"/user/profile"}
)
public class ProfileController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());

        WebContext context = new WebContext(req, resp, req.getServletContext());

        ProfileServiceInterface profileService = new ProfileServiceImpl(req.getServletContext());
        profileService.showProfile(req, resp, engine, context);

    }

}
