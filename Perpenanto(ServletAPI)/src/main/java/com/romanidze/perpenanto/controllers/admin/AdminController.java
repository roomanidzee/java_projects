package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(
        name = "AdminController",
        description = "Страница входа для администратора",
        urlPatterns = {"/admin"}
)
public class AdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        try{
            engine.process("admin/admin_page.html", context, resp.getWriter());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
