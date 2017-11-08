package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.User;
import com.romanidze.perpenanto.services.implementations.UserServiceImpl;
import com.romanidze.perpenanto.services.interfaces.UserServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 05.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@WebServlet(
        name = "UserAdminController",
        description = "Пользователи сервиса",
        urlPatterns = {"/admin/security"}
)
public class UserAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        UserServiceInterface userService = new UserServiceImpl(req.getServletContext());

        List<User> users = userService.getUsers();
        context.setVariable("users", users);

        try{
            engine.process("admin/users_admin.html", context, resp.getWriter());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        String action = req.getParameter("form_action");

        User user = User.builder()
                        .id(id)
                        .emailOrUsername(username)
                        .password(password)
                        .build();

        UserServiceInterface userService = new UserServiceImpl(req.getServletContext());

        switch(action){

            case "add":
                userService.addUser(user);
                break;

            case "update":
                userService.updateUser(user);
                break;

            case "delete":
                userService.deleteUser(user.getId());
                break;

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<User> users = userService.getUsers();
        context.setVariable("users", users);

        try{
            engine.process("admin/users_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/security");
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

}
