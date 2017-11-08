package com.romanidze.perpenanto.controllers.admin;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.models.Product;
import com.romanidze.perpenanto.services.implementations.ProductServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ProductServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(
        name = "ProductAdminController",
        description = "Товары сервиса",
        urlPatterns = {"/admin/products"}
)
public class ProductAdminController extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ProductServiceInterface productService = new ProductServiceImpl(req.getServletContext());

        List<Product> products = productService.getProductsByCookie(req, resp);
        context.setVariable("products", products);

        try{
            engine.process("admin/products_admin.html", context, resp.getWriter());
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp){

        Long id = Long.valueOf(req.getParameter("id"));
        String title = req.getParameter("title");
        Integer price = Integer.valueOf(req.getParameter("price"));
        String description = req.getParameter("description");
        String photoLink = req.getParameter("photo_link");

        String action = req.getParameter("form_action");

        Product product = Product.builder()
                                 .id(id)
                                 .title(title)
                                 .price(price)
                                 .description(description)
                                 .photoLink(photoLink)
                                 .build();

        ProductServiceInterface productService = new ProductServiceImpl(req.getServletContext());

        switch(action){

            case "add":
                productService.addProduct(product);
                break;

            case "update":
                productService.updateProduct(product);
                break;

            case "delete":
                productService.deleteProduct(product.getId());
                break;

        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        List<Product> products = productService.getProducts();
        context.setVariable("products", products);

        try{
            engine.process("admin/products_admin.html", context, resp.getWriter());
            resp.sendRedirect("/admin/products");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
