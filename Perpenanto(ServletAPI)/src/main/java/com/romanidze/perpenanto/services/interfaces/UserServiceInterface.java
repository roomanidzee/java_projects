package com.romanidze.perpenanto.services.interfaces;

import com.romanidze.perpenanto.models.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserServiceInterface {

    void loginUser(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);
    void loginAdmin(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);
    void registerUser(HttpServletRequest req, HttpServletResponse resp, TemplateEngine engine, WebContext context);
    User findById(Long id);

    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);

    List<User> getUsers();
    List<User> getUsersByCookie(HttpServletRequest req, HttpServletResponse resp);
}
