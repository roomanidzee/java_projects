package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.ProductToUser;
import com.romanidze.perpenanto.models.temp.TempProductToUser;
import com.romanidze.perpenanto.services.implementations.ProductServiceImpl;
import com.romanidze.perpenanto.services.implementations.ProductToUserServiceImpl;
import com.romanidze.perpenanto.services.implementations.UserServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ProductServiceInterface;
import com.romanidze.perpenanto.services.interfaces.ProductToUserServiceInterface;
import com.romanidze.perpenanto.services.interfaces.UserServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 05.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@WebServlet(
        name = "ProductToUserAdminController",
        description = "Товары пользователей",
        urlPatterns = {"/admin/products_to_user"}
)
public class ProductToUserAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ProductToUserServiceInterface productToUserService = new ProductToUserServiceImpl(req.getServletContext());

        List<TempProductToUser> productToUsers = productToUserService.getProductToUserByCookie(req, resp);
        context.setVariable("products_to_users", productToUsers);

        try{
            engine.process("admin/product_to_user_admin.html", context, resp.getWriter());
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        Long userId = Long.valueOf(req.getParameter("user_id"));
        Long productId = Long.valueOf(req.getParameter("product_id"));

        String action = req.getParameter("form_action");

        ProductToUserServiceInterface productToUserService = new ProductToUserServiceImpl(req.getServletContext());
        UserServiceInterface userService = new UserServiceImpl(req.getServletContext());
        ProductServiceInterface productService = new ProductServiceImpl(req.getServletContext());

        ProductToUser productToUser = ProductToUser.builder()
                                                   .id(id)
                                                   .user(userService.findById(userId))
                                                   .products(Collections.singletonList(productService.findById(productId)))
                                                   .build();

        switch(action){

            case "add":
                productToUserService.addProductToUser(productToUser);
                break;

            case "update":
                productToUserService.updateProductToUser(productToUser);
                break;

            case "delete":
                productToUserService.deleteProductToUser(productToUser.getId());
                break;

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<TempProductToUser> productToUsers = productToUserService.getProducts();
        context.setVariable("products_to_users", productToUsers);

        try{
            engine.process("admin/product_to_user_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/products_to_user");
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
