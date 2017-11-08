package com.romanidze.perpenanto.controllers;

import com.romanidze.perpenanto.config.TemplateEngineUtil;
import com.romanidze.perpenanto.services.implementations.ProductServiceImpl;
import com.romanidze.perpenanto.services.interfaces.ProductServiceInterface;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 05.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@WebServlet(
        name = "ProductsCatalogController",
        description = "Каталог товаров сервиса",
        urlPatterns = {"/catalog"}
)
public class ProductsCatalogController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ProductServiceInterface productService = new ProductServiceImpl(req.getServletContext());
        productService.showProductsCatalog(req, resp, engine, context);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        ProductServiceInterface productService = new ProductServiceImpl(req.getServletContext());
        productService.addProductToBusket(req, resp, engine, context);

    }

}
