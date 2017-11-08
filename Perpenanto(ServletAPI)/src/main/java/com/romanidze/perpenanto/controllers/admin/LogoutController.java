package com.romanidze.perpenanto.controllers.admin;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(
        name = "AdminLogoutController",
        description = "Выход со страницы администратора",
        urlPatterns = {"/admin_logout"}
)
public class LogoutController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

        HttpSession session = req.getSession(true);
        session.removeAttribute("admin_id");

        try {
            resp.sendRedirect("/admin_login");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
