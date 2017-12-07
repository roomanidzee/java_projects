package com.romanidze.perpenanto.controllers;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "IndexController",
        description = "Стартовая страница",
        urlPatterns = {"/index"}
)
public class IndexController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());

        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("price1", "1500 Р");
        context.setVariable("price2", "4000 Р");
        context.setVariable("price3", "300 Р");

        try{
            engine.process("index.html", context, resp.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
